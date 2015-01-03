var app = angular.module('angularfaces', ['ngMessages', 'jua']);

/**
 * Adds a couple of general-purpose functions to the root scope.
 */
app.run(function($rootScope, $compile) {
    $rootScope.afToJson = function(variable) {
		return JSON.stringify(variable);
	};
	
	$rootScope.afSendNGSyncToServer = function() {
		window.setTimeout(function() {
			try {
				var ngsyncs = document.getElementsByClassName('puisync');
				if (null != ngsyncs) {
					var ids = "";
					for(var i=0; i<ngsyncs.length; i++)
	                {
						if (i>0) ids=ids+" ";
	                   ids = ids + ngsyncs[i].id;
	                }
					
					jsf.ajax.request(ngsyncs[0].id, null, {
						'de.beyondjava.angularfaces.behavior.event':'ngsync', 
						execute:ids,
						render:'angular'
					});
				}
			} catch (e) {
				console.log("Ein Fehler ist aufgetreten: " + e);
			}
		}, 10);
	};

});

function puiUpdateModel(bean, json) {
	injectJSonIntoScope(bean, json, window.jsfScope);
}


function injectJSonIntoScope(bean, json, $scope) {
	try {
		if ($scope.$$phase) {
			createOrUpdateModelVariableWithJSON($scope, bean, JSON.parse(json));
		} else {
			$scope.$apply(function() {
				createOrUpdateModelVariableWithJSON($scope, bean, JSON.parse(json));
			});
		}
	} catch (e) {
		console.log(e);
		alert("injectJSonIntoScope(bean, json, $scope): Couldn't inject variable angularFaces into scope.\n" + "value ="
				+ json + "\n" + e);
	}
}

function createOrUpdateModelVariableWithJSON(scope, bean, json) {
	if (typeof(scope[bean])=="undefined") {
		scope[bean]=json;
	} else {
		var keys = Object.keys(json);
		var theBean=scope[bean];
		for (var key in keys) {
			theBean[keys[key]] = json[keys[key]];
		}
	}
}
