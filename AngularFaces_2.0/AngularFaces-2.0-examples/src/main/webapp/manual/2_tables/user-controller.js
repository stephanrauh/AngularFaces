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
  $scope.users = JSON.parse(jason);
  
  $scope.$watch('usersBean.usersAsJson', function(newVal, oldVal){
	    var jason = $scope.usersBean.usersAsJson.replace(/'/g, '"');
	    $scope.users = JSON.parse(jason);
	  }, true);

  
  $scope.$watch('users', function(newVal, oldVal){
    $scope.usersBean.users=$scope.users;
  }, true);
  
  $scope.addUser = function() {
    $scope.users.push({age:45, name:'Kath # ' + $scope.users.length + '.'});
  };
  
  $scope.deleteUser = function(selectedUser) {
    var shortlist = [];
    $scope.users.forEach(function(user) { if (user != selectedUser) shortlist.push(user);})
    $scope.users=shortlist;
  }
  
  $scope.editUser = function(selectedUser) {
    selectedUser.editable=true;
  }
  
  $scope.setReadOnly = function(selectedUser) {
    selectedUser.editable=false;
  }


}

