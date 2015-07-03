'use strict';

var phonecatApp = angular.module('tutorialApp', [ 'ngRoute',
		'tutorialControllers' ]);

phonecatApp.config([ '$routeProvider', function($routeProvider) {
    console.log("Hallo");
	$routeProvider.when('/page1', {
		templateUrl : 'page1.jsf',
		controller : 'EmptyController'
	}).when('/page2', {
		templateUrl : 'page2.jsf',
		controller : 'EmptyController'
	}).otherwise({
	  templateUrl : 'other.jsf',
	});
} ]);
