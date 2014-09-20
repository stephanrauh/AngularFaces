/** must be set in order to reactivate AngularJS after a JSF request. */
reactivateAngularJS=false;
syncPullFunctions = new Array();
syncPushFunctions = new Array();
counter = 1;

function isUndefined(value) {
	return typeof value == 'undefined';
}

function addSyncPullFunction(f) {
	var len = syncPullFunctions.length;
	syncPullFunctions[len] = f;
}

function addSyncPushFunction(f) {
	var len = syncPushFunctions.length;
	syncPushFunctions[len] = f;
}

function injectJSonIntoScope(bean, json, $scope) {
	try {
		if ($scope.$$phase) {
			// console.log("immediate injectIntoJson call #" + counter);
			// console.log(json);
			// eval("$scope." + bean + "=null;");
			eval("$scope." + bean + "=" + json + ";");
		} else {
			$scope.$apply(function() {
				// console.log("delayed injectIntoJson call #" + counter);
				// console.log(json);
				// eval("$scope." + bean + "=null;");
				eval("$scope." + bean + "=" + json + ";");
			});
		}
	} catch (e) {
		console.log(e);
		alert("injectJSonIntoScope(bean, json, $scope): Couldn't inject variable angularFaces into scope.\n" + "value ="
				+ json + "\n" + e);
	}
}

function storeValues() {
	values = new Array();
	models = new Array();
	inputFields = new Array();
	try {
		var index = 0;
		var forms = document.forms;
		for (var f = 0; f < forms.length; f++) {
			var elements = forms[f].elements;

			for (var i = 0; i < elements.length; i++) {
				var element = elements[i];
				if (element.type == "text" || element.type == "number"
						|| element.type == "select-one"
						|| element.type == "checkbox"
						|| element.type == "hidden") {
					// console.log(element.id + "/" + element.value);
					if (element.value && element.value != "") {
						if (element.type == "select-one"
								|| element.type == "checkbox") {
							// PrimeFaces SelectOne componenents must not have a
							// ng-model
							// (they behave unpredicably if they have one)
							var ngModel = element.id.replace("_input", "");
							ngModel = ngModel.replace(":", "_"); // fix
							// automatically
							// generated ids
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

function restoreValues(ngAppElement) {
	for (var i = 0; i < syncPushFunctions.length; i++) {
		syncPushFunctions[i]();
	}
	var $scope = ngAppElement.scope();
	if (!$scope) {
		alert("AngularJS hasn't been initialized properly.");
		return;
	}
	for (var i = 0; i < models.length; i++) {
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
					console.log("AngularFaces apply Exception " + e + " "
							+ assignment);
				}
				if (!element.value) {
					element.value = value;
				}
			});
		} catch (e) {
			alert("Couldn't restore the field values. ngModel=" + model
					+ " element=" + element + " Exception=" + e);
		}
	}
	try {
		var code = "if ($scope.init) $scope.init()";
		eval(code);
	} catch (e) {
		console.log("couldn't call the scope's init method: " + e);
	}

	for (var i = 0; i < syncPullFunctions.length; i++) {
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
		console.log(e);
		alert("injectVariableIntoScope(model, value): Couldn't inject variable " + model + " into scope.\n" + value
				+ "=" + value + "\n" + e);
	}
}

function reinitAngular(ngAppElement, ngApp) {
	storeValues();
	try {
		angular.bootstrap(ngAppElement, [ ngApp ]);
		restoreValues(ngAppElement);
	} catch (e) {
		console.log("Angular probably doesn't need to be initialized again");
		console.log(e);
	}
}

function updateAngularModel(model, value) {
	injectVariableIntoScope(model, value);
}

function findNGAppAndReinitAngular(element) {
	var s = element.getAttribute("update");

	var ngAppElement = element;
	// look for the element bearing the ng-app directive
	var ngApp = null;
	while (ngAppElement != null && ngApp == null) {
		ngApp = ngAppElement.getAttribute("ng-app");
		if (null == ngApp) {
			ngAppElement = ngAppElement.parentNode;
		}
	}
	// for some reason, the form element doesn't always admit is has a parent.
	// If so, start searching top-down.
	if (null == ngApp) {
		var f = document.forms;
		if (null != f) {
			for (var index = 0; index < f.length; index++) {
				ngAppElement = f[index];
				while (ngAppElement != null && ngApp == null) {
					ngApp = ngAppElement.getAttribute("ng-app");
					if (null == ngApp) {
						ngAppElement = ngAppElement.parentNode;
					}
				}
				if (null != ngApp) {
					break;
				}
			}
		}
	}
	if (null != ngApp) {
		console.log("ng-app=" + ngApp + " ngAppElement = " + ngAppElement);
		reinitAngular(ngAppElement, ngApp);
	}
}

function shutDownAngularJS() {
	/*
	 * Iterate through the child scopes and kill 'em all, because Angular 1.2
	 * won't let us $destroy() the $rootScope
	 */
	var scope = $rootScope.$$childHead;
	while (scope) {
		var nextScope = scope.$$nextSibling;
		scope.$destroy();
		scope = nextScope;
	}

	/*
	 * Iterate the properties of the $rootScope and delete any that possibly
	 * were set by us but leave the Angular-internal properties and functions
	 * intact so we can re-use the application.
	 */
	for ( var prop in $rootScope) {
		if (($rootScope[prop]) && (prop.indexOf('$$') != 0)
				&& (typeof ($rootScope[prop]) === 'object')) {
			$rootScope[prop] = null;
		}
	}
}

function interceptAJAXRequests(data) {
	if (!reactivateAngularJS) {
		return;
	}

	if (data.source.type != "submit") {
		return;
	}

	switch (data.status) {
	case "begin":
		break;
	case "complete":
		var element = data.source;
		reactivateAngularJS=false;
		setTimeout(function() {
			findNGAppAndReinitAngular(element);
		}, 1000);
		break;
	}
}

if (typeof(jsf)!="undefined") {
  jsf.ajax.addOnEvent(interceptAJAXRequests);
}

// if (typeof(PrimeFaces) != "undefined") {
// if (typeof(PrimeFaces.ajax.AjaxUtils)!= "undefined") {
// var primeFacesOriginalSendFunction = PrimeFaces.ajax.AjaxUtils.send;
//	
// PrimeFaces.ajax.AjaxUtils.send = function(cfg){
// var callSource = null;
//		
// // if not string, the caller is a process - in this case we do not interfere
// if(typeof(cfg.source) == 'string') {
// callSource = $(PrimeFaces.escapeClientId(cfg.source));
// }
// // in each case call original send
// primeFacesOriginalSendFunction(cfg);
// if (null != callSource) {
// findNGAppAndReinitAngular(callSource);
// }
// }
// }
// else if (typeof(PrimeFaces.ajax.Request)!= "undefined") {
// var primeFacesOriginalSendFunction = PrimeFaces.ajax.Request.send;
//	
// PrimeFaces.ajax.AjaxUtils.send = function(cfg){
// var callSource = null;
//		
// // if not string, the caller is a process - in this case we do not interfere
// if(typeof(cfg.source) == 'string') {
// callSource = $(PrimeFaces.escapeClientId(cfg.source));
// }
// // in each case call original send
// primeFacesOriginalSendFunction(cfg);
// if (null != callSource) {
// findNGAppAndReinitAngular(callSource);
// }
// }
// }
// }
