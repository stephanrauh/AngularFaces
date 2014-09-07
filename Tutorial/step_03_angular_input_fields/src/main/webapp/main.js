angular.module("AngularFacesExamples", [ "angularfaces" ]);

function LabelDemoController($scope) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);

	$scope.$watch('customer.lastName', function(newValue, oldValue) {
		if ($scope.customer.firstName!="" && $scope.customer.lastName!="") {
			var name = "".concat($scope.customer.firstName).concat(".").concat($scope.customer.lastName);
			$scope.customer.emailAddress = name + "@example.com";
		}
	});
	$scope.$watch('customer.firstName', function(newValue, oldValue) {
		if ($scope.customer.firstName!="" && $scope.customer.lastName!="") {
			var name = "".concat($scope.customer.firstName).concat(".").concat($scope.customer.lastName);
			$scope.customer.emailAddress = name + "@example.com";
		}
	});

}

