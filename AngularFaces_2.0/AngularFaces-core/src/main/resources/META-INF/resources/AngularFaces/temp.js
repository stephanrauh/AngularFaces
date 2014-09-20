function readVariableFromScope(model) {
	var value;
	try {
		var $scope = angular.element('body').scope();
		if ($scope) { 
			$scope.$apply(function() {
			var assignment = "value = $scope." + model;
			try {
				eval(assignment);
			} catch (e) {
				console.log("AngularFaces apply Exception " + e + " "
						+ assignment);
			}
			});
		}
		else {
			console.log("The body doesn't have a scope.");
		}
	} catch (e) {
		console.log(e);
		alert("readVariableFromScope(): Couldn't extract variable " + model + " from scope.\n" + value
				+ "=" + value + "\n" + e);
	}
	return value;
}