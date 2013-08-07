function userBeanController($scope) {
	$scope.howdy = function(name) {
		if (name)
			alert("Howdy, " + name + "!");
		else
			alert("Howdy, Angular!");
	}
}
var app = angular.module('userBeanApp', []);
