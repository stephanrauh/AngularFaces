var app = angular.module('angularfaces', []);

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
    				$scope.myField = $("[name='"+$scope.af_for+"']");
    				$scope.servermessage=$element.attr("servermessage");
    				
    				
//    				var name = $scope.myField.name;
//    				var p = $scope.myField.parentElement;
//    				var count=1;
//    				while (null != p)
//    				{
//    					var pname = p.name;
//    					if (pname) {
//    						name = pname + "." + name;
//    					}
//    					p=p.parentElement;
//    				}
//    				$scope.prefix=name;
    				$scope.serverMessageVisible = function() {
						if ($scope.myField.hasClass("pristine")) {
							return "true";
						}
    					return "false"; 
    				};
    				$scope.message= function() { 
						if ($scope.myField.hasClass("ng-invalid-min")) {
							var min = $scope.myField.attr("min");
							var msg = angularFacesMessages["This number must be at least {}."];
							msg=msg.replace("{}", min);
							return msg;
						}
						if ($scope.myField.hasClass("ng-invalid-max")) {
							var max = $scope.myField.attr("max");
							var msg = angularFacesMessages["This number must be less or equal {}."];
							msg=msg.replace("{}", max);
							return msg;
						}
						if ($scope.myField.hasClass("ng-invalid-number")) {
							var msg = angularFacesMessages["Please enter a valid number."];
							return msg;
						}
						if ($scope.myField.hasClass("ng-invalid-required")) {
							var msg = angularFacesMessages["Please fill out this field."];
							return msg;
						}
						
						if ($scope.myField.hasClass("integer")) {
							var msg = angularFacesMessages["Please enter a valid integer number."];
							return msg;
						}
						if ($scope.myField.hasClass("ng-invalid-minlength")) {
							var min = $scope.myField.attr("ng-minlength");
							var msg = angularFacesMessages["At least {} characters required."];
							msg=msg.replace("{}", min);
							return msg;
						}
						if ($scope.myField.hasClass("ng-invalid-maxlength")) {
							var max = $scope.myField.attr("ng-maxlength");
							var msg = angularFacesMessages["{} characters accepted at most."];
							msg=msg.replace("{}", max);
							return msg;
						}

						
						
						if ($scope.servermessage) return $scope.servermessage;
						if ($scope.myField.hasClass("ng-invalid")) {
							var msg = angularFacesMessages["A validation rule is violated."];
							return msg;
						}
    					return "";
    				};
    				$scope.hasMessage= function() { 
						return ($scope.myField.hasClass("ng-invalid"));
    				};
    				$scope.visibilityClass= function() { 
						if ($($scope.myField).is(":visible")) {
							return "";
						}
    					return "hidden";
    				};

    				$scope.getTemplate = function() {
    					var t='<span class="ui-state-error-text {{visibilityClass()}}">{{message()}}</span>';
    					if ($scope.primefaces) {
    						t='<div ng-show="hasMessage()" class="ui-messages-error ui-corner-all"><span class="ui-messages-error-icon"></span><span class="ui-messages-error-summary">{{message()}}</span></div>';
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
	    				$scope.myField = $("[name='"+$scope.af_for+"']");

	    				$scope.errorClass= function() { 
							if ($scope.myField.hasClass("ng-invalid")) {
								return "ui-state-error";
							}
	    					return "";
	    				};
	    				$scope.visibilityClass= function() { 
							if ($($scope.myField).is(":visible")) {
								return "";
							}
	    					return "hidden";
	    				};
	    				$scope.getTemplate = function() {
	    					var t='<span class="{{errorClass()}} {{visibilityClass()}}">{{label}}</span>';
	    					if ($scope.primefaces) {
	    						t='<span class="{{errorClass()}} ui-outputlabel ui-widget">{{label}}</span>';
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



