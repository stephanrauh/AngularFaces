var app = angular.module('CarShop', ["angularfaces", 'ngTable', 'ngTableExport']).
controller('CarShopController', function($scope, $filter, ngTableParams) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);
	
	
	$scope.showIfSet = function(ngmodel) {
		if (typeof(ngmodel)=='undefined') {
			return "hidden";
		}
		if (ngmodel=="") {
			return "hidden";
		}
		return "";
	}

//    $scope.tableParams = new ngTableParams({
//        page: 1,                  // show first page
//        count: 10,                // count per page
//        sorting: { brand: 'asc' } // initial sorting
//    }, {
//        total: $scope.carPool.carPool.length, // length of data
//        getData: function($defer, params) {
//        	params.settings().counts=[5, 10, 'all']; // todo: that's a hack!
//        	var rows = $scope.carPool.carPool;
//        	if (params.filter()) rows=$filter('filter')(rows, params.filter());
//        	if (params.sorting()) rows = $filter('orderBy')(rows, params.orderBy());
//        	var page = params.page();
//        	var pageLength = params.count();
//        	if (pageLength=='all') 
//    			$defer.resolve(rows);
//        	else
//        		$defer.resolve(rows.slice((page - 1) * pageLength, page * pageLength));
//        }
//    });
})


