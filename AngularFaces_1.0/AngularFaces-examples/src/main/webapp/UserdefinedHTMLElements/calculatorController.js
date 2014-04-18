function calculatorController($scope) {
	$scope.number1 = Math.floor((Math.random()*100)+1);
	$scope.number2 = Math.floor((Math.random()*100)+1);
	$scope.add = function() {
		$scope.result = parseInt($scope.number1) + parseInt($scope.number2);
		return $scope.result;
	};
	$scope.summands = function() {
		$scope.number1 = Math.floor((Math.random()*100)+1);
		$scope.number2 = Math.floor((Math.random()*100)+1);
		return [ $scope.number1, $scope.number2 ];
	};
}

var app = angular.module('calculatorApp', []);

app.directive(
				'addition',
				function() {
					return {
						restrict : 'AE',
						template : '<div style="width:50px"><div ng-repeat="summand in summands()" style="text-align:right">+{{summand}}</div><hr style="width:50px"><div style="text-align:right">{{add()}}</div></div>'
					};
				});