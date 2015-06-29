console.log("ng-table-controller.js loaded");
var module = angular.module("userControllerApp", [ 'angularfaces', 'ngTable' ]);

userController.$inject = ['$scope', 'ngTableParams'];

module.controller('userController', userController);

function userController($scope, ngTableParams) {
  console.log("initializing userController");
  // This initializes the Angular Model with the values of the JSF bean
  // attributes
  initJSFScope($scope);
  var jason = $scope.usersBean.usersAsJson.replace(/'/g, '"');
  console.log(jason);
  $scope.users = JSON.parse(jason);
}

