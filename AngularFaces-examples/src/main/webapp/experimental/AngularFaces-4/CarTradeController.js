function CarTradeController($scope) {
	$scope.quantity = new Array();
	$scope.year = new Array();
	$scope.price = new Array();
	$scope.orderVolume = function() {
		var sum = 0;
		for ( var i = 0; i < $scope.quantity.length; i++) {
			sum += $scope.quantity[i];
		}
		return sum;
	}
	$scope.totalCost = function() {
		var sum = 0;
		for ( var i = 0; i < $scope.quantity.length; i++) {
			sum += $scope.quantity[i] * $scope.price[i];
		}
		return sum;
	}
}
var app = angular.module('CarTradeApp', []);
