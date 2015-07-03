'use strict';

var tutorialControllers = angular.module('tutorialControllers', []);

tutorialControllers.controller('EmptyController', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
		    // This initializes the Angular Model with the values of the JSF bean attributes
		    initJSFScope($scope);

		} ]);
