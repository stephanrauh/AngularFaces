syncPullFunctions = new Array();
syncPushFunctions = new Array();

function isUndefined(value){return typeof value == 'undefined';}

function addSyncPullFunction(f) {
	var len = syncPullFunctions.length;
	syncPullFunctions[len] = f;
}

function addSyncPushFunction(f) {
	var len = syncPushFunctions.length;
	syncPushFunctions[len] = f;
}

function injectJSonIntoScope(json, $scope)
{
	try {
		$scope.faces= json;
//		alert($scope.angularFaces.calculatorBean.headerText);
	} catch (e) {
		alert("Couldn't inject variable angularFaces into scope.\n" + "value =" + json + "\n" + e);
	}
}

function storeValues() {
	values = new Array();
	models = new Array();
	inputFields = new Array();
	try {
		var index = 0;
		var forms = document.forms;
		for ( var f = 0; f < forms.length; f++) {
			var elements = forms[f].elements;

			for ( var i = 0; i < elements.length; i++) {
				var element = elements[i];
				if (element.type == "text" || element.type == "number" || element.type == "select-one"
					|| element.type == "checkbox"|| element.type == "hidden") {
					// console.log(element.id + "/" + element.value);
					if (element.value && element.value != "") {
						if (element.type == "select-one" || element.type == "checkbox") {
							// PrimeFaces SelectOne componenents must not have a
							// ng-model
							// (they behave unpredicably if they have one)
							var ngModel = element.id.replace("_input", "");
							ngModel=ngModel.replace(":", "_"); // fix automatically generated ids
							if (element.type == "checkbox") {
								values[index] = element.checked;
							} else {
								values[index] = '"' + element.value + '"';
							}
							models[index] = ngModel;
							inputFields[index] = element;
							index++;

						} else {
							var ngModel = element.getAttribute("ng-model");
							if (ngModel) {
								values[index] = element.value;
								models[index] = ngModel;
								inputFields[index] = element;
								index++;
							}
						}
					}
				}
			}
		}
	} catch (e) {
		alert("Couldn't store the field values " + e);
	}
}

function restoreValues() {
	for ( var i = 0; i < syncPushFunctions.length; i++) {
		syncPushFunctions[i]();
	}
	var $scope = angular.element('body').scope();
	if (!$scope) {
		alert("AngularJS hasn't been initialized properly.");
		return;
	}
	for ( var i = 0; i < models.length; i++) {
		var value = values[i];
		var model = models[i];
		var element = inputFields[i];
		try {

			$scope.$apply(function() {
				var assignment = "$scope." + model + "= " + value;
				try {
					// console.log(assignment);
					eval(assignment);
				} catch (e) {
					// under certain circumstances, this exception occurs
					// but can safely be ignored
					console.log("AngularFaces apply Exception " + e + " " + assignment);
				}
				if (!element.value) {
					element.value = value;
				}
			});
		} catch (e) {
			alert("Couldn't restore the field values. ngModel=" + model + " element=" + element + " Exception=" + e);
		}
	}
	try {
		var code = "if ($scope.init) $scope.init()";
		eval(code);
	} catch (e) {
		console.log("couldn't call the scope's init method: " + e);
	}
	
	for ( var i = 0; i < syncPullFunctions.length; i++) {
		syncPullFunctions[i]();
	}
}

function injectVariableIntoScope(model, value) {
	try {
		var $scope = angular.element('body').scope();

		$scope.$apply(function() {
			var assignment = "$scope." + model + "= " + value;
			try {
				eval(assignment);
			} catch (e) {
				// under certain circumstances, this exception occurs
				// but can safely be ignored
				// alert("AngularFaces apply Exception " + e + " " +
				// assignment);
				console.log(assignment);
				console.log(e);
			}
		});
	} catch (e) {
		alert("Couldn't inject variable " + model + " into scope.\n" + value + "=" + value + "\n" + e);
	}
}

function reinitAngular(app) {
	storeValues();
	try {
		angular.bootstrap(document, [ app ]);
		restoreValues();
	}
	catch (e) {
		console.log("Angular probably doesn't need to be initialized again");
		console.log(e);
	}
}

function updateAngularModel(model, value) {
	injectVariableIntoScope(model, value);
}

function readVariableFromScope(model) {
	var value;
	try {
		var $scope = angular.element('body').scope();

		$scope.$apply(function() {
			var assignment = "value = $scope." + model;
			try {
				eval(assignment);
			} catch (e) {
				console.log("AngularFaces apply Exception " + e + " " + assignment);
			}
		});
	} catch (e) {
		alert("Couldn't inject variable " + model + " into scope.\n" + value + "=" + value + "\n" + e);
	}
	return value;
}


function findNGAppAndReinitAngular(element) {
    var ngApp=null;
	while (element!=null && ngApp==null) {
		ngApp = element.getAttribute("ng-app");
		element = element.parentNode;
	}
	if (null != ngApp) {
		console.log("ng-app="+ngApp);
		reinitAngular(ngApp);
	}
}

function interceptAJAXRequests(data) {
    if (data.source.type != "submit") {
        return;
    }

    switch (data.status) {
        case "begin":
            break;
        case "complete":
            var element = data.source;
            findNGAppAndReinitAngular(element);
            break;
    }    
}

//if (typeof(jsf)!="undefined") {
//	jsf.ajax.addOnEvent(interceptAJAXRequests);
//}
//
//if (typeof(PrimeFaces) != "undefined") {
//	if (typeof(PrimeFaces.ajax.AjaxUtils)!= "undefined") {
//		var primeFacesOriginalSendFunction = PrimeFaces.ajax.AjaxUtils.send;
//	
//		PrimeFaces.ajax.AjaxUtils.send = function(cfg){    
//		  var callSource = null;
//		
//		  // if not string, the caller is a process - in this case we do not interfere
//		  if(typeof(cfg.source) == 'string') {
//			callSource = $(PrimeFaces.escapeClientId(cfg.source)); 
//		  }
//		  // in each case call original send
//		  primeFacesOriginalSendFunction(cfg);
//		  if (null != callSource) {
//			  findNGAppAndReinitAngular(callSource);
//		  }
//		}
//	}
//	else if (typeof(PrimeFaces.ajax.Request)!= "undefined") {
//		var primeFacesOriginalSendFunction = PrimeFaces.ajax.Request.send;
//	
//		PrimeFaces.ajax.AjaxUtils.send = function(cfg){    
//		  var callSource = null;
//		
//		  // if not string, the caller is a process - in this case we do not interfere
//		  if(typeof(cfg.source) == 'string') {
//			callSource = $(PrimeFaces.escapeClientId(cfg.source)); 
//		  }
//		  // in each case call original send
//		  primeFacesOriginalSendFunction(cfg);
//		  if (null != callSource) {
//			  findNGAppAndReinitAngular(callSource);
//		  }
//		}
//	}
//}
