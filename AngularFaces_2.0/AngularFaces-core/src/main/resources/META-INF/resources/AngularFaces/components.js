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
						if ($scope.servermessage) return $scope.servermessage;
						if ($scope.myField.hasClass("ng-invalid")) {
							var msg = angularFacesMessages["A validation rule is violated."];
							return msg;
						}
    					return "";
    				};
    			},
    template: '<span>{{message()}}</span>',
    replace: true
	};
});
