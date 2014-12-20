var app = angular.module('angularfaces', ['ngMessages']);

app.run(function($rootScope) {
    $rootScope.afToJson = function(variable) {
		return JSON.stringify(variable);
	};
	
	$rootScope.afSendNGSyncToServer = function() {
		window.setTimeout(function() {
			try {
				var ngsyncs = document.getElementsByClassName('puisync');
				if (null != ngsyncs) {
					var ids = "";
					for(var i=0; i<ngsyncs.length; i++)
	                {
						if (i>0) ids=ids+" ";
	                   ids = ids + ngsyncs[i].id;
	                }
					
					jsf.ajax.request(ngsyncs[0].id, null, {
						'de.beyondjava.angularfaces.behavior.event':'ngsync', 
						execute:ids,
						render:'angular'
					});
				}
			} catch (e) {
				console.log("Ein Fehler ist aufgetreten: " + e);
			}
		}, 10);
	};

});

app.directive('puimessage', function() {
 return {
    restrict: 'E',
    scope: {},
    template: function() {
       return "{{currentMessage}}";
    },
    controller: function($scope) {
      $scope.currentMessage="";
      this.renderMessages = function(errorMessages, inputField) {
          $scope.currentMessage=getErrorMessage(errorMessages,inputField);
      };
    },
    
    link: function($scope, $element, $attrs, ctrl) {
        var watchFieldID= $attrs['for'];
        var watchAttr = findErrorObject(watchFieldID);
        console.log(watchAttr);
        var currentScope = $scope.$parent;
        // TODO: find out which scope contains the $error object
        while (null != currentScope) {
            currentScope.$watchCollection(watchAttr, function(values) {
                ctrl.renderMessages(values, document.getElementById(watchFieldID));
            });
            currentScope=currentScope.$parent;
        }
    }
 };
});



app.directive('puilabel', ['$compile', function($compile) {
	  return {
	    restrict: 'E',
	    transclude: true,
	    scope: {},
	    controller: function($scope, $element) {
	    				$scope.label=$element.attr('label');
                        var fieldId = $element.attr('for');
                        var formname = $element.attr('formname');
	    				if (fieldId) {
	    					fieldId=fieldId.replace(":", "\\:");
	    				}
	    				$scope.primefaces="true" == $element.attr('primefaces');
	    				$scope.af_for=fieldId;
	    				$scope.jqueryField = $("[name='"+$scope.af_for+"']");
	    				

	    				$scope.errorClass= function() { 
	    				    var targetScope = angular.element("[id='"+$scope.af_for+"']").scope();
	    				    var fn = targetScope[formname];
	                        while (typeof(fn)=='undefined') {
                                targetScope =targetScope.$parent;
                                if (null==targetScope) {
                                    return "";
                                }
                                fn = targetScope[formname];
                            }
        				    var fieldId = $element.attr('for');
	    				    if (fn[fieldId]) {
	    				      //var pristine = fn[fieldId].$pristine;
	    				      var invalid = fn[fieldId].$invalid;
	    				      if (invalid) {
	    				        return "ui-state-error";
                              }
	    				    }
      					    return "";
	    				};
	    				$scope.visibilityClass= function() { 
							if ($($scope.jqueryField).is(":visible")) {
								return "";
							}
	    					return "hidden";
	    				};
	    				$scope.getTemplate = function() {
	    					var t='<span class="af-label {{errorClass()}} {{visibilityClass()}}">{{label}}</span>';
	    					if ($scope.primefaces) {
	    						t='<span class="af-label {{errorClass()}} ui-outputlabel ui-widget">{{label}}</span>';
	    					}
	    					return t; 
	    				};
	    			},
		link: function(scope, element, attrs) {
	        var el = $compile(scope.getTemplate())(scope);
	        element.replaceWith(el);
	    }
		};
	}]);

app.directive('puimessages', function($compile) {
	  return {
	    restrict: 'E',
	    transclude: true,
	    scope: {},
	    controller: function($scope, $element) {
	    				$scope.primefaces="true" == $element.attr('primefaces');
	    				$scope.message= function() {
	    					if (typeof($scope.$parent.facesmessages)=="undefined" || $scope.$parent.facesmessages==null) {
	    						return "";
	    					}
	    					return $scope.$parent.facesmessages.detail;
	    				};
	    				$scope.hasMessage= function() {
	    					if (typeof($scope.$parent.facesmessages)=="undefined" || $scope.$parent.facesmessages==null) {
	    						return false;
	    					}
	    					return true;
	    				};
	    				$scope.visibilityClass= function() {
	    					if (typeof($scope.$parent.facesmessages)=="undefined" || $scope.$parent.facesmessages==null) {
	    						return "hidden";
	    					}

	    					return "";
	    				};

	    				$scope.getTemplate = function() {
	    					var t='<span class="af-message ui-state-error-text {{visibilityClass()}}"><ul><li ng-repeat="msg in $parent.facesmessages"><span style="padding-right:10px">{{msg.severity}}</span><span style="padding-right:10px">{{msg.summary}}</span><span>{{msg.detail}}</span></li></ul></span>';
	    					
	    					
	    					
	    					if ($scope.primefaces) {
	    						t='<div ng-show="hasMessage()" class="af-message ui-messages-error ui-corner-all"><div class="ui-messages ui-widget" aria-live="polite"><div class="ui-messages-info ui-corner-all"><ul><li ng-repeat="msg in $parent.facesmessages"><span class="ui-messages-info-icon"></span><span class="ui-messages-info-summary">{{msg.summary}}</span><span class="ui-messages-info-detail">{{msg.detail}}</span></li></ul></div></div></div>';
	    					}
	    					return t; 
	    				};
	    			},
		link: function(scope, element, attrs) {
	        var el = $compile(scope.getTemplate())(scope);
	        element.replaceWith(el);
	    },
	    replace: true
		};
	});




// Todo: check whether this directive works
var INTEGER_REGEXP = /^\-?\d*$/;
app.directive('integer', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (INTEGER_REGEXP.test(viewValue)) {
					// it is valid
					ctrl.$setValidity('integer', true);
					return viewValue;
				} else {
					// it is invalid, return undefined (no model update)
					ctrl.$setValidity('integer', false);
					return undefined;
				}
			});
		}
	};
});



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

function puiUpdateModel(bean, json) {
	injectJSonIntoScope(bean, json, window.jsfScope);
}


function injectJSonIntoScope(bean, json, $scope) {
	try {
		if ($scope.$$phase) {
			createOrUpdateModelVariableWithJSON($scope, bean, JSON.parse(json));
		} else {
			$scope.$apply(function() {
				createOrUpdateModelVariableWithJSON($scope, bean, JSON.parse(json));
			});
		}
	} catch (e) {
		console.log(e);
		alert("injectJSonIntoScope(bean, json, $scope): Couldn't inject variable angularFaces into scope.\n" + "value ="
				+ json + "\n" + e);
	}
}

function createOrUpdateModelVariableWithJSON(scope, bean, json) {
//	eval("$scope." + bean + "=" + json + ";");
	if (typeof(scope[bean])=="undefined") {
		scope[bean]=json;
	} else {
		var keys = Object.keys(json);
		var theBean=scope[bean];
		for (var key in keys) {
			theBean[keys[key]] = json[keys[key]];
		}
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

function findErrorObject(watchFieldID) {
var watchAttrName=null;
var watchField = document.getElementById(watchFieldID);
while (watchField) {
  var name =watchField.getAttribute("name");
    if (null != name && typeof(name) != 'undefined') {
    if (null == watchAttrName)
        watchAttrName = name;
    else
        watchAttr = name + "." + watchAttrName;
  }
  watchField=watchField.parentElement;
}
var watchAttr = watchAttr+".$error";
return watchAttr;
}

function getErrorMessage(errors, inputField) {
if (errors && errors['min']) {
    var min = inputField.getAttribute("min");
    var msg = angularFacesMessages["This number must be at least {}."];
    msg=msg.replace("{}", min);
    return msg;
}
if (errors && errors['max']) {
    var max = inputField.getAttribute("max");
    var msg = angularFacesMessages["This number must be less or equal {}."];
    msg=msg.replace("{}", max);
    return msg;
}
if (errors && errors['number']) {
  var msg = angularFacesMessages["Please enter a valid number."];
  return msg;
}
if (errors && errors['required']) {
   var msg = angularFacesMessages["Please fill out this field."];
   return msg;
}

if (errors && errors['integer']) {
    var msg = angularFacesMessages["Please enter a valid integer number."];
    return msg;
}
if (errors && errors['minlength']) {
    var min = inputField.getAttribute("ng-minlength");
    var msg = angularFacesMessages["At least {} characters required."];
    msg=msg.replace("{}", min);
    return msg;
}
if (errors && errors['maxlength']) {
    var max = inputField.getAttribute("ng-maxlength");
    var msg = angularFacesMessages["{} characters accepted at most."];
    msg=msg.replace("{}", max);
    return msg;
}

if (errors && errors['invalid']) {
    var f = inputField.getAttribute("class");
    if (typeof(f)!=undefined && f != null) {
        var classes = f.split(" ");
        for (var key in classes ) {
            var c = classes[key];
            if (c.indexOf("ng-invalid-") === 0) {
                var msg = angularFacesMessages[c];
                if (typeof(msg)!="undefined" && msg!=null) {
                    return msg;
                }
            }
        }
    }
    var msg = angularFacesMessages["A validation rule is violated."];
    return msg;
}
if (inputField.getAttribute('servermessage')) {
    return inputField.getAttribute('servermessage');
}
return "";
}
