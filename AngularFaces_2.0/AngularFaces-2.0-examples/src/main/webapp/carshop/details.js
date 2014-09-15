var app = angular.module('CarShop', ["angularfaces"]).
controller('CarShopController', function($scope, $filter) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);
})


