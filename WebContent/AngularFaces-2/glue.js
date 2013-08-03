function storeValues() {
	values = new Array();
	models = new Array();
	inputFields = new Array();
	try {
		var index = 0;
		var elements = document.forms.myform.elements;
		for ( var i = 0; i < elements.length; i++) {
			var element = elements[i];
			if (element.type == "text") {
				if (element.value && element.value != "") {
					values[index] = element.value;
					models[index] = element.getAttribute("ng-model");
					inputFields[index] = element;
					index++;
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
					eval(assignment);
				} catch (e) {
					// alert("AngularFaces apply Exception " + e + " " + assignment);
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

function reinitAngular() {
	storeValues();
	angular.bootstrap(document, [ 'calculatorApp' ]);
	restoreValues();
}
