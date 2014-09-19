var app = angular.module('CarShop', [ "angularfaces" ]).controller(
		'CarShopController', function($scope) {
			// This initializes the Angular Model with the values of the JSF
			// bean attributes
			initJSFScope($scope);

			$scope.$watch('filterBean.brand', sendFilterToServer);
			$scope.$watch('filterBean.type', sendFilterToServer);
			$scope.$watch('filterBean.price', sendFilterToServer);
			$scope.$watch('filterBean.mileage', sendFilterToServer);
			$scope.$watch('filterBean.fuel', sendFilterToServer);
			$scope.$watch('filterBean.color', sendFilterToServer);
			$scope.$watch('filterBean.yearText', sendFilterToServer);

			$scope.toJson = function(variable) {
				return JSON.stringify(variable);
			};
		})

function sendFilterToServer(newValue, oldValue) {
	if (newValue != oldValue) {
		try {
			if (event.target.nextElementSibling) {
				var helperID = event.target.nextElementSibling.id;
				window.setTimeout(function() {
					jsf.ajax.request(helperID, event, {
						'javax.faces.behavior.event' : 'valueChange', render : 'angular'
					});
				}, 10);
			}
		} catch (e) {
			console.log("Ein Fehler ist aufgetreten: " + e);
		}
	}
}
