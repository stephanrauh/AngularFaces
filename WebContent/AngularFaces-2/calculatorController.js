function calculatorController($scope) {
	$scope.add = function() {
		if ($scope.number1 && $scope.number2) {
			$scope.result = parseInt($scope.number1) + parseInt($scope.number2);
		} else
			$scope.result = "(undefined)";
		return $scope.result;
	};
	$scope.toString = function(number)
	{
		if (number)
			return number;
		else
			return "(empty)";
	}
}

var app = angular.module('calculatorApp', []);

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
