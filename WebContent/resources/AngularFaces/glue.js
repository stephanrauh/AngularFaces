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
				if (element.type == "text" || element.type == "number" || element.type == "select-one"
						|| element.type == "checkbox") {
					console.log(element.id + "/" + element.value);
					if (element.value && element.value != "") {
						if (element.type == "select-one" || element.type == "checkbox") {
							// PrimeFaces SelectOne componenents must not have a
							// ng-model
							// (they behave unpredicably if they have one)
							var ngModel = element.id.replace("_input", "");
							if (element.type == "checkbox") {
								values[index] = element.checked;
							} else {
								values[index] = element.value;
							}
							models[index] = ngModel;
							inputFields[index] = element;
							index++;

						} else {
							var ngModel = element.getAttribute("ng-model");
							if (ngModel) {
								values[index] = element.value;
								models[index] = ngModel;
								inputFields[index] = element;
								index++;
							}
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
	var $scope = angular.element('body').scope();
	for ( var i = 0; i < models.length; i++) {
		var value = values[i];
		var model = models[i];
		var element = inputFields[i];
		try {

			$scope.$apply(function() {
				var assignment = "$scope." + model + "= " + value;
				try {
					// console.log(assignment);
					eval(assignment);
				} catch (e) {
					// under certain circumstances, this exception occurs
					// but can safely be ignored
					console.log("AngularFaces apply Exception " + e + " " + assignment);
				}
				if (!element.value) {
					element.value = value;
				}
			});
		} catch (e) {
			alert("Couldn't restore the field values. ngModel=" + model + " element=" + element + " Exception=" + e);
		}
	}
	try
	{
		var code="if ($scope.init) $scope.init()";
		eval(code);
	}
	catch (e)
	{
		console.log("couldn't call the scope's init method: "+ e);
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

function updateAngularModel(model, value) {
	injectVariableIntoScope(model, value);
}

