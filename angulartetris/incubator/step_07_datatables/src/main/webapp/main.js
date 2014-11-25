angular.module("TableDemo", [ "angularfaces" ]);

function TableController($scope) {
	// This initializes the Angular Model with the values of the JSF bean
	// attributes
	initJSFScope($scope);
	
	$scope.started = new Date().getTime();
	$scope.seconds=-1;

	
	
	$scope.counter = function() {
		var millisecondsSinceStart=new Date().getTime() - $scope.started;
		return Math.floor(millisecondsSinceStart/1000);
	};
	
	$scope.updateCounter = function() {
		$scope.seconds=$scope.counter();
		window.requestAnimationFrame($scope.updateCounter);
		if (!$scope.$$phase)
			$scope.$apply();
	};
	$scope.updateCounter();
}


