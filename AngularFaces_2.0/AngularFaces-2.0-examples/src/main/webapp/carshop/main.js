var app = angular.module('CarShop', ["angularfaces", 'ngTable', 'ngTableExport']).
controller('CarShopController', function($scope, $filter, ngTableParams) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);
	
	$scope.$watch('filterBean.brand', function(newValue, oldValue) {
		if (newValue!=oldValue) {
			window.setTimeout(function(){
				var x = document.getElementsByClassName("filterHelper");
				console.log("new:" + newValue + " old: " + oldValue);
				jsf.ajax.request(x[0].id,event,{'javax.faces.behavior.event':'valueChange',render:'angular'});
			},10);
		}
	});

    $scope.tableParams = new ngTableParams({
        page: 1,                  // show first page
        count: 10,                // count per page
        sorting: { brand: 'asc' } // initial sorting
    }, {
        total: $scope.carPool.carPool.length, // length of data
        getData: function($defer, params) {
        	params.settings().counts=[5, 10, 'all']; // todo: that's a hack!
        	var rows = $scope.carPool.carPool;
        	if (params.filter()) rows=$filter('filter')(rows, params.filter());
        	if (params.sorting()) rows = $filter('orderBy')(rows, params.orderBy());
        	var page = params.page();
        	var pageLength = params.count();
        	if (pageLength=='all') 
    			$defer.resolve(rows);
        	else
        		$defer.resolve(rows.slice((page - 1) * pageLength, page * pageLength));
        }
    });
})

