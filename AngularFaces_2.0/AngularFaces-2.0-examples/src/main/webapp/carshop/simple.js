var app = angular.module('CarShop', [ "angularfaces" ]).controller(
		'CarShopController', function($scope) {
			// This initializes the Angular Model with the values of the JSF
			// bean attributes
			initJSFScope($scope);

			$scope.toJson = function(variable) {
				return JSON.stringify(variable);
			};
			sendSyncToServer();
		});

function sendSyncToServer() {
	try {
		var ngsync = document.getElementsByClassName('filterSyncClass')[0];
		
		var helperID = ngsync.id;
		jsf.ajax.request(ngsync.id, null, {
			'javax.faces.behavior.event' : 'valueChange',
			execute : helperID,
			render : 'angular'
		});
	} catch (e) {
		console.log("Ein Fehler ist aufgetreten: " + e);
	}
}
