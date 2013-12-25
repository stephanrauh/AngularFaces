function calculatorController($scope) {
	$scope.number1 = 3;
	$scope.number2 = 34;
	$scope.add = function() {
		if ($scope.number1 && $scope.number2) {
			$scope.result = parseInt($scope.number1) + parseInt($scope.number2);
		} else
			$scope.result = "(undefined)";
		return $scope.result;
	};
	$scope.toString = function(number) {
		if (number)
			return number;
		else
			return "(empty)";
	}
	$scope.summands = function()
	{
		return [$scope.number1, $scope.number2];
	};
}

var app = angular.module('calculatorApp', []);

app    .directive('addition', function() {
    return {
    	restrict: 'AE',
    	template: '<div style="width:50px"><div ng-repeat="summand in summands()" style="text-align:right">+{{summand}}</div><hr><div style="text-align:right">{{add()}}</div></div>'
    };
});

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

