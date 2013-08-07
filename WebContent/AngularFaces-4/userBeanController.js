function userBeanController($scope) {
	$scope.model = new Array();
	$scope.year = new Array();
	$scope.howdy = function(name) {
		if (name)
			alert("Howdy, " + name + "!");
		else
			alert("Howdy, Angular!");
	}
}
var app = angular.module('userBeanApp', []);
