var app = angular.module('angularfaces', []);

app.run(function($rootScope) {
    $rootScope.afToJson = function(variable) {
		return JSON.stringify(variable);
	};
	
	$rootScope.afSendNGSyncToServer = function(ngsyncID) {
		window.setTimeout(function() {
			jsf.ajax.request(ngsyncID, null, {
				'de.beyondjava.angularfaces.behavior.event':'ngsync', 
				execute:ngsyncID,
				render:'angular'
			});
		}, 10);
	};
});

app.directive('puimessage', function($compile) {
  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    controller: function($scope, $element) {
    				var fieldId = $element.attr('af-for');
    				if (fieldId) {
    					fieldId=fieldId.replace(":", "\\:");
    				}
    				$scope.primefaces="true" == $element.attr('primefaces');
    				$scope.af_for=fieldId;
    				$scope.jqueryField = $("[name='"+$scope.af_for+"']");
    				$scope.servermessage=$element.attr("servermessage");
    				
    				$scope.serverMessageVisible = function() {
						if ($scope.jqueryField.hasClass("pristine")) {
							return "true";
						}
    					return "false"; 
    				};
    				$scope.message= function() { 
						if ($scope.jqueryField.hasClass("ng-invalid-min")) {
							var min = $scope.jqueryField.attr("min");
							var msg = angularFacesMessages["This number must be at least {}."];
							msg=msg.replace("{}", min);
							return msg;
						}
						if ($scope.jqueryField.hasClass("ng-invalid-max")) {
							var max = $scope.jqueryField.attr("max");
							var msg = angularFacesMessages["This number must be less or equal {}."];
							msg=msg.replace("{}", max);
							return msg;
						}
						if ($scope.jqueryField.hasClass("ng-invalid-number")) {
							var msg = angularFacesMessages["Please enter a valid number."];
							return msg;
						}
						if ($scope.jqueryField.hasClass("ng-invalid-required")) {
							var msg = angularFacesMessages["Please fill out this field."];
							return msg;
						}
						
						if ($scope.jqueryField.hasClass("integer")) {
							var msg = angularFacesMessages["Please enter a valid integer number."];
							return msg;
						}
						if ($scope.jqueryField.hasClass("ng-invalid-minlength")) {
							var min = $scope.jqueryField.attr("ng-minlength");
							var msg = angularFacesMessages["At least {} characters required."];
							msg=msg.replace("{}", min);
							return msg;
						}
						if ($scope.jqueryField.hasClass("ng-invalid-maxlength")) {
							var max = $scope.jqueryField.attr("ng-maxlength");
							var msg = angularFacesMessages["{} characters accepted at most."];
							msg=msg.replace("{}", max);
							return msg;
						}

						if ($scope.jqueryField.hasClass("ng-invalid")) {
							var f = $scope.jqueryField.attr("class");
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
						if ($scope.servermessage) return $scope.servermessage;
    					return "";
    				};
    				$scope.hasMessage= function() { 
						return ($scope.jqueryField.hasClass("ng-invalid"));
    				};
    				$scope.visibilityClass= function() { 
						if ($($scope.jqueryField).is(":visible")) {
							return "";
						}
    					return "hidden";
    				};

    				$scope.getTemplate = function() {
    					var t='<span class="af-message ui-state-error-text {{visibilityClass()}}">{{message()}}</span>';
    					if ($scope.primefaces) {
    						t='<div ng-show="hasMessage()" class="af-message ui-messages-error ui-corner-all"><span class="ui-messages-error-icon"></span><span class="ui-messages-error-summary">{{message()}}</span></div>';
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

app.directive('puilabel', function($compile) {
	  return {
	    restrict: 'E',
	    transclude: true,
	    scope: {},
	    controller: function($scope, $element) {
	    				$scope.label=$element.attr('label');
	    				var fieldId = $element.attr('af-for');
	    				if (fieldId) {
	    					fieldId=fieldId.replace(":", "\\:");
	    				}
	    				$scope.primefaces="true" == $element.attr('primefaces');
	    				$scope.af_for=fieldId;
	    				$scope.jqueryField = $("[name='"+$scope.af_for+"']");

	    				$scope.errorClass= function() { 
							if ($scope.jqueryField.hasClass("ng-invalid")) {
								return "ui-state-error";
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
	    },
	    replace: true
		};
	});

app.directive('puimessages', function($compile) {
	  return {
	    restrict: 'E',
	    transclude: true,
	    scope: {},
	    controller: function($scope, $element) {
	    				$scope.primefaces="true" == $element.attr('primefaces');
	    				if (typeof($scope.$parent.facesmessages)!="undefined") {
	    					$scope.messages=$scope.$parent.facesmessages;
	    				}
	    				$scope.message= function() { 
	    					if (typeof($scope.messages)=="undefined" || $scope.messages==null) {
	    						return "";
	    					}
	    					return $scope.messages.detail;
	    				};
	    				$scope.hasMessage= function() {
	    					if (typeof($scope.messages)=="undefined" || $scope.messages==null) {
	    						return false;
	    					}
	    					return true;
	    				};
	    				$scope.visibilityClass= function() {
	    					if (typeof($scope.messages)=="undefined" || $scope.messages==null) {
	    						return "hidden";
	    					}

	    					return "";
	    				};

	    				$scope.getTemplate = function() {
	    					var t='<span class="af-message ui-state-error-text {{visibilityClass()}}"><ul><li ng-repeat="msg in messages"><span style="padding-right:10px">{{msg.severity}}</span><span style="padding-right:10px">{{msg.summary}}</span><span>{{msg.detail}}</span></li></ul></span>';
	    					
	    					
	    					
	    					if ($scope.primefaces) {
	    						t='<div ng-show="hasMessage()" class="af-message ui-messages-error ui-corner-all"><div class="ui-messages ui-widget" aria-live="polite"><div class="ui-messages-info ui-corner-all"><ul><li ng-repeat="msg in messages"><span class="ui-messages-info-icon"></span><span class="ui-messages-info-summary">{{msg.summary}}</span><span class="ui-messages-info-detail">{{msg.detail}}</span></li></ul></div></div></div>';
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



