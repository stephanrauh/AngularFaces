function CarTradeController($scope) {
	initDatatable($scope);

	$scope.orderVolume = function() {
		var sum = 0;
		var cars = $scope.carsSmall;
		for ( var i = 0; i < cars.length; i++) {
			var car = cars[i];
			if (car.ordered) {
				sum += car.quantity;
			}
		}
		return sum;
	}
	$scope.totalCost = function() {
		var sum = 0;
		var cars = $scope.carsSmall;
		for ( var i = 0; i < cars.length; i++) {
			var car = cars[i];
			if (car.ordered) {
				sum += car.quantity * car.price;
			}
		}
		return sum;
	}
}
var app = angular.module('CarTradeApp', []);
