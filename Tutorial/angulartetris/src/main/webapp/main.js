angular.module("AngularTetris", [ "angularfaces" ]);

function AngularTetrisController($scope) {
	// This initializes the Angular Model with the values of the JSF bean
	// attributes
	initJSFScope($scope);

	this.game = new GameController($scope.grid, $scope);
	window.gameController=this.game;
	this.game.init($scope.grid);
//	document.onkeydown = game.onKey;
	this.game.startGame();

	$scope.brickColor = function(color){
		if (0==color) return "#FFFFFF";
        if (1==color) return "#00F0F0";
        if (2==color) return "#0000F0";
        if (3==color) return "#F0A000";
        if (4==color) return "#F0F000";
        if (5==color) return "#00F000";
        if (6==color) return "#F00000";
        if (7==color) return "#A000F0";
        return "#00FFFFFF";
	};
	
	$scope.onkey = function(event) {
		this.game.onKey(event);
	};
	



//	game.startGame();
}


