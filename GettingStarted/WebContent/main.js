function calculatorController($scope) {
 initJSFScope($scope);
 $scope.add = function() {
 if ($scope.calculatorBean.number1 && $scope.calculatorBean.number2) {
   $scope.calculatorBean.result = parseInt($scope.calculatorBean.number1) + parseInt($scope.calculatorBean.number2);
 } else
   $scope.calculatorBean.result = "undef";
   return $scope.calculatorBean.result;
 };
 $scope.toString = function(number) {
 if (number)
   return number;
 else return "empty";
 }
}
 
var app = angular.module('calculatorApp', []);
