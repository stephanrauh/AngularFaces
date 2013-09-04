needsServer=false;

function mandelbrotController($scope) {
	$scope.$watch('resolution', function() {
		if ($scope.resolution >= 512) {
			if ($scope.quality > 2) {
				$scope.quality = 2;
				PrimeFaces.widgets.qualityWidget.selectValue(2);
				PrimeFaces.widgets.qualitySliderWidget.setValue(2);
			}
		}
		if ($scope.resolution >= 768) {
			if ($scope.quality > 1) {
				$scope.quality = 1;
				PrimeFaces.widgets.qualityWidget.selectValue(1);
				PrimeFaces.widgets.qualitySliderWidget.setValue(1);
			}
		}
	});
	$scope.$watch('showGlobeDemo', function() {
		if ($scope.showGlobeDemo) {
			activateGlobeDemo();
		} else {
			activatePlaneDemo();
		}
	});

}

var app = angular.module('mandelbrotApp', []);

var INTEGER_REGEXP = /^\-?\d*$/;
app.directive('integer', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (INTEGER_REGEXP.test(viewValue)) {
					// it is valid
					ctrl.$setValidity('integer', true);
					return viewValue;
				} else {
					// it is invalid, return undefined (no model update)
					ctrl.$setValidity('integer', false);
					return undefined;
				}
			});
		}
	};
});
