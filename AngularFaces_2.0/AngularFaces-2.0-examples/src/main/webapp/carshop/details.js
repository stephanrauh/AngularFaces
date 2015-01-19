var app = angular.module('CarShop', ["angularfaces"]).
controller('CarShopController', function($scope, $filter) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);
	$scope.doShowDetails = function() {
		if (typeof($scope.customerBean)=="undefined"||
			typeof($scope.customerBean.showDetails)=="undefined") {
			return false;
		}
		return $scope.customerBean.showDetails;
	}
	$scope.showDetailsClass = function() {
		if ($scope.doShowDetails()) 
			return "";
		else
			return "hidden";
	}
	$scope.showFinishedTransactionClass = function() {
		if ($scope.doShowDetails()) 
			return "hidden";
		else
			return "";
	}
	
	$scope.buttonEnabledStyle = function() {
		if ($scope.myForm.$invalid) 
			return "color:#888";
		else
			return "";
	}

});

app.directive('captcha', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (typeof(viewValue)=="undefined" || viewValue==null) {
					ctrl.$setValidity('captcha', false);
					return viewValue;
				}
				if (viewValue!=attrs["captcha"]) {
					ctrl.$setValidity('captcha', false);
					return viewValue;
				} else {
					ctrl.$setValidity('captcha', true);
					return viewValue;
				}
			});
		}
	};
});
