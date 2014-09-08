'use strict';

var tutorialControllers = angular.module('tutorialControllers', []);

tutorialControllers.controller('EmptyController', ['$scope', '$routeParams',
  function($scope, $routeParams) {
    $scope.tutorialId = $routeParams.tutorialId;
  }]);
