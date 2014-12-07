angular.module("AngularFacesExamples", [ "angularfaces" ])
.controller('MyCtrl', ['$scope', function($scope) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);

	$scope.width = 0;
	$scope.height = 20;
	$scope.angularModelStyle="color:blue;";

	//$scope.calculatorBean.result = $scope.calculatorBean.number1 + $scope.calculatorBean.number2;
	
	$scope.$watch('calculatorBean.number1', function(newValue, oldValue) {
	        $scope.calculatorBean.result = $scope.calculatorBean.number1 + $scope.calculatorBean.number2;
	        $scope.calculatorBean.gridStyle='font-weight:bold';
            $scope.calculatorBean.headerText = 'calculated by AngularJS watch on number1';   
      });
	$scope.$watch('calculatorBean.number2', function(newValue, oldValue) {
	        $scope.calculatorBean.result = $scope.calculatorBean.number1 + $scope.calculatorBean.number2;
	        $scope.calculatorBean.gridStyle='';
            $scope.calculatorBean.headerText = 'calculated by AngularJS watch on number2';   
      });
}]);

