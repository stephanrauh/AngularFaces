var app = angular.module('CarShop', [ "angularfaces" ]).controller(
		'CarShopController', function($scope) {
			// This initializes the Angular Model with the values of the JSF
			// bean attributes
			initJSFScope($scope);
			
			$scope.sendFilterToServer = function(newValue, oldValue) {
				if (newValue != oldValue) {
					try {
						var ngsync = document.getElementsByClassName('filterSyncClass')[0];
						sendNGSyncToServer(ngsync.id);
					} catch (e) {
						console.log("Ein Fehler ist aufgetreten: " + e);
					}
				}
			};

			$scope.$watch('filterBean.brand', $scope.sendFilterToServer);
			$scope.$watch('filterBean.type', $scope.sendFilterToServer);
			$scope.$watch('filterBean.price', $scope.sendFilterToServer);
			$scope.$watch('filterBean.mileage', $scope.sendFilterToServer);
			$scope.$watch('filterBean.fuel', $scope.sendFilterToServer);
			$scope.$watch('filterBean.color', $scope.sendFilterToServer);
			$scope.$watch('filterBean.yearText', $scope.sendFilterToServer);
		})

function sendNGSyncToServer(ngsyncID) {
	window.setTimeout(function() {
		jsf.ajax.request(ngsyncID, null, {
			'de.beyondjava.angularfaces.behavior.event':'ngsync', 
			execute:ngsyncID,
			render:'angular'
		});
	}, 10);
}
