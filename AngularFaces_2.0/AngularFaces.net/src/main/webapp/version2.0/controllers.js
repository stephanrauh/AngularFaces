'use strict';

var tutorialControllers = angular.module('tutorialControllers', []);

tutorialControllers.controller('EmptyController', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
			$scope.tutorialId = $routeParams.tutorialId;

			$scope.highlight = function() {
				try {
					var codeblocks = document.getElementsByTagName('pre');
					if (null != codeblocks) {
					for (var i = 0; i < codeblocks.length;i++)
						hljs.highlightBlock(codeblocks[i]);
					}

				} catch (e) {
					alert("error");
				}
			};
		} ]);
