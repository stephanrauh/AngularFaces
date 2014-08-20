angular.module('angularfaces', [])

.directive('angularfacesmessage', function() {
  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    controller: function($scope, $element) {
    				var fieldId = $element.attr('af-for');
    				if (fieldId) {
    					fieldId=fieldId.replace(":", "\\:");
    				}
    				$scope.af_for=fieldId;
//    				$scope.myField = $("#"+$scope.af_for);
    				$scope.myField = $("[name='"+$scope.af_for+"']");
    				$scope.servermessage=$element.attr("servermessage");
    				
    				
    				var name = $scope.myField.name;
    				
    				var p = $scope.myField.parentElement;
    				var count=1;
    				while (null != p)
    				{
    					var pname = p.name;
    					if (pname) {
    						name = pname + "." + name;
    					}
    					p=p.parentElement;
    				}
    				$scope.prefix=name;
    				$scope.serverMessageVisible = function() {
						if ($scope.myField.hasClass("pristine")) {
							return "true";
						}
    					return "false"; 
    				};
    				$scope.message= function() { 
						if ($scope.myField.hasClass("ng-invalid-min")) {
							var min = $scope.myField.attr("min");
							return "This number must be at least " + min + ".";
						}
						if ($scope.myField.hasClass("ng-invalid-max")) {
							var max = $scope.myField.attr("max");
							return "This number must be less or equal " + max + ".";
						}
						if ($scope.myField.hasClass("ng-invalid-number")) {
							return "Please enter a valid number.";
						}
						if ($scope.servermessage) return $scope.servermessage;
						if ($scope.myField.hasClass("ng-invalid")) {
							return "A validation rule is violated.";
						}
    					return "";
    				};
    			},
    template: '<span>{{message()}}</span>',
    replace: true
	};
});
