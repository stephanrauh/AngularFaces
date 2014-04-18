var app = angular.module('calculatorApp', []);

app.directive(
  'addition',
  function() {
  	return {
  		restrict : 'AE',
  		transclude : true,
  		scope : {
  			numbers : '@',
  		},
  		controller : function($scope) {
  			$scope.summands = function(n) {
    return n.split(",");
  			};
  			$scope.add = function(n) {
    var s = $scope.summands(n);
    var result = 0;
    for (var i = 0; i < s.length; i++) {
    	result += parseInt(s[i]);
    }
    return result;
  			}
  		},
  		template : '<div style="width:50px" ng-transclude><div ng-repeat="summand in summands(numbers)" style="text-align:right">+{{summand}}</div><hr style="width:50px"><div style="text-align:right">{{add(numbers)}}</div></div>'
  	};
  });
