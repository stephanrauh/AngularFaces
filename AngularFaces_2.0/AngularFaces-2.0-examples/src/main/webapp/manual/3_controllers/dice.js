console.log("dice.js loaded");
var module = angular.module("DiceApp", [ "angularfaces" ]);

Dice1Controller.$inject = ['$scope'];

module.controller('Dice1Controller', Dice1Controller);

function Dice1Controller($scope, $injector) {
  console.log("initializing Dice1Controller");
  $scope.welcomeMessage = "This is the first controller.";
  // This initializes the Angular Model with the values of the JSF bean
  // attributes
  initJSFScope($scope);
}

Dice2Controller.$inject = ['$scope'];

module.controller('Dice2Controller', Dice2Controller);

function Dice2Controller($scope, $injector) {
  console.log("initializing Dice2Controller");
  $scope.welcomeMessage = "This is the second controller.";
  // This initializes the Angular Model with the values of the JSF bean
  // attributes
  initJSFScope($scope);
}
