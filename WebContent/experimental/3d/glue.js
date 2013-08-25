function storeValues() {
	values = new Array();
	models = new Array();
	inputFields = new Array();
	try {
		var index = 0;
		var forms = document.forms;
		for ( var f = 0; f < forms.length; f++) {
			var elements = forms[f].elements;
			for ( var i = 0; i < elements.length; i++) {
				var element = elements[i];
				if (element.type == "text" || element.type == "select") {
					if (element.value && element.value != "") {
						var ngModel = element.getAttribute("ng-model");
						if (ngModel) {
							console.log(ngModel + " = $(" + element.id + ") = " + element.value);
							values[index] = element.value;
							models[index] = ngModel;
							inputFields[index] = element;
							index++;
						}
					}
				}
			}
		}
	} catch (e) {
		alert("Couldn't store the field values " + e);
	}
}

function restoreValues() {
	try {
		for ( var i = 0; i < models.length; i++) {
			var value = values[i];
			var model = models[i];
			var element = inputFields[i];
			var $scope = angular.element('body').scope();

			$scope.$apply(function() {
				var assignment = "$scope." + model + "= " + value;
				try {
					console.log(assignment);
					eval(assignment);
				} catch (e) {
					// under certain circumstances, this exception occurs
					// but can safely be ignored
					// alert("AngularFaces apply Exception " + e + " " +
					// assignment);
				}
				if (!element.value) {
					element.value = value;
				}
			});
		}

	} catch (e) {
		alert("Couldn't restore the field values " + e);
	}
}

function injectVariableIntoScope(model, value) {
	try {
		var $scope = angular.element('body').scope();

		$scope.$apply(function() {
			var assignment = "$scope." + model + "= " + value;
			try {
				eval(assignment);
			} catch (e) {
				// under certain circumstances, this exception occurs
				// but can safely be ignored
				// alert("AngularFaces apply Exception " + e + " " +
				// assignment);
			}
		});
	} catch (e) {
		alert("Couldn't inject variable " + model + " into scope.\n" + value + "=" + value + "\n" + e);
	}
}

function reinitAngular(app) {
	storeValues();
	angular.bootstrap(document, [ app ]);
	restoreValues();
}
