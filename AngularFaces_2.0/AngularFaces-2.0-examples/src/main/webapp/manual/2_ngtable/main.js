var app = angular.module('AngularFacesExamples', ["angularfaces", 'ngTable']).
controller('MyCtrl', function($scope, $filter, ngTableParams) {
	// This initializes the Angular Model with the values of the JSF bean attributes
	initJSFScope($scope);
    var data = [{name: "Moroni", age: 50},
                {name: "Tiancum", age: 43},
                {name: "Jacob", age: 27},
                {name: "Nephi", age: 29},
                {name: "Enos", age: 34},
                {name: "Tiancum", age: 43},
                {name: "Jacob", age: 27},
                {name: "Nephi", age: 29},
                {name: "Enos", age: 34},
                {name: "Tiancum", age: 43},
                {name: "Jacob", age: 27},
                {name: "Nephi", age: 29},
                {name: "Enos", age: 34},
                {name: "Tiancum", age: 43},
                {name: "Jacob", age: 27},
                {name: "Nephi", age: 29},
                {name: "Enos", age: 34}];

    $scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10,           // count per page
        sorting: {
            name: 'asc'     // initial sorting
        }
    }, {
        total: data.length, // length of data
        getData: function($defer, params) {
            var orderedData = params.sorting() ?
                    $filter('orderBy')(data, params.orderBy()) :
                    data;

			$defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });
})


