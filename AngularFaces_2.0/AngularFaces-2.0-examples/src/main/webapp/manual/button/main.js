angular.module("KendoDemos", [ "kendo.directives" ]);
function MyCtrl($scope) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);

	$scope.width = 0;
	$scope.height = 20;
	

	//$scope.faces.calculatorBean.result = $scope.faces.calculatorBean.number1 + $scope.faces.calculatorBean.number2;
	
	$scope.$watch('faces.calculatorBean.number1', function(newValue, oldValue) {
	        $scope.faces.calculatorBean.result = $scope.faces.calculatorBean.number1 + $scope.faces.calculatorBean.number2;
	        $scope.faces.calculatorBean.gridStyle='font-weight:bold';
               
      });
	$scope.$watch('faces.calculatorBean.number2', function(newValue, oldValue) {
	        $scope.faces.calculatorBean.result = $scope.faces.calculatorBean.number1 + $scope.faces.calculatorBean.number2;
	        $scope.faces.calculatorBean.gridStyle='';
      });
}

