var app = angular.module('CarShop', ["angularfaces"]).
controller('CarShopController', function($scope, $filter) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);
	$scope.doShowDetails = function() {
		if (typeof($scope.customerBean)=="undefined"||
			typeof($scope.customerBean.showDetails)=="undefined") {
			return false;
		}
		console.log("show: " + $scope.customerBean.showDetails);
		return $scope.customerBean.showDetails;
	}
	$scope.showDetailsClass = function() {
		if ($scope.doShowDetails()) 
			return "";
		else
			return "hidden";
	}
})


