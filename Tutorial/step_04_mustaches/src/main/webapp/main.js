angular.module("AngularTetris", [ "angularfaces" ]);

function AngularTetrisController($scope) {
	// This initializes the Angular Model with the values of the JSF bean
	// attributes
	initJSFScope($scope);

	$scope.myData = {
		"days" : [ {
			"dow" : "1",
			"templateDay" : "Monday",
			"jobs" : [ {
				"name" : "Wakeup"
			}, {
				"name" : "work 8-4"
			} ]
		}, {
			"dow" : "2",
			"templateDay" : "Tuesday",
			"jobs" : [ {
				"name" : "Wakeup"
			} ]
		} ]
	} ;
}
