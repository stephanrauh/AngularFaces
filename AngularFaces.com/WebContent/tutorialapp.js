'use strict';

var phonecatApp = angular.module('tutorialApp', [ 'ngRoute',
		'tutorialControllers' ]);

phonecatApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'info.html',
		controller : 'EmptyController'
	}).when('/tutorial.html', {
		templateUrl : 'tutorial/info.html',
		controller : 'EmptyController'
	}).when('/step-00.html', {
		templateUrl : 'tutorial/step_00/info.html',
		controller : 'EmptyController'
	}).when('/step-01.html', {
		templateUrl : 'tutorial/step_01/info.html',
		controller : 'EmptyController'
	}).when('/step-02.html', {
		templateUrl : 'tutorial/step_02/info.html',
		controller : 'EmptyController'
	}).when('/step-03.html', {
		templateUrl : 'tutorial/step_03/info.html',
		controller : 'EmptyController'
	}).when('/step-04.html', {
		templateUrl : 'tutorial/step_04/info.html',
		controller : 'EmptyController'
	}).when('/step-05.html', {
		templateUrl : 'tutorial/step_05/info.html',
		controller : 'EmptyController'
	}).when('/step-06.html', {
		templateUrl : 'tutorial/step_06/info.html',
		controller : 'EmptyController'
	}).when('/step-07.html', {
		templateUrl : 'tutorial/step_07/info.html',
		controller : 'EmptyController'
	}).when('/step-08.html', {
		templateUrl : 'tutorial/step_08/info.html',
		controller : 'EmptyController'
	}).when('/step-09.html', {
		templateUrl : 'tutorial/step_09/info.html',
		controller : 'EmptyController'
	}).when('/step-10.html', {
		templateUrl : 'tutorial/step_10/info.html',
		controller : 'EmptyController'
	}).when('/step-11.html', {
		templateUrl : 'tutorial/step_11/info.html',
		controller : 'EmptyController'
	}).when('/step-12.html', {
		templateUrl : 'tutorial/step_12/info.html',
		controller : 'EmptyController'
	}).otherwise({
		redirectTo : '/tutorial.html'
	});
} ]);
