/**
 * (C) 2014 Stephan Rauh http://www.beyondjava.net
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

function GameController(grid, scope) {
	this.rows = 20;
	var columns = 10;
	var timeToDrop;
	var counter = 0;
	var gameActive = false;
	var gravity = false;
	var preview = false;
	var nextDrop = 0;
	var score= 0;

	var tetromino = null;

	// playground is a two-dimensional array of integers, which in turn are
	// color codes
	var playground = null;

	this.init = function(grid) {
		playground = grid;
		this.rows=grid.rows.length;
		this.columns = grid.rows[0].cells.length;
		tetromino = null;
		timeToDrop = 500;
	};
	
	this.settingsStyle = function() {
		if (gameActive) return "display:none";
		return "";
	}
	
	this.scoreStyle = function() {
		if (!gameActive) return "display:none";
		return "";
	}
	
	this.showHighscore = function() {
		window.alert("Highscore is yet to be implemented.");
	};

	this.dropTile = function() {
		if (!tetromino.moveTileDown(playground)) {
			tetromino = null;
			this.eliminateCompletedRows(playground);
		}
	};

	// playground is a two-dimensional array of integers, which in turn are
	// color codes
	this.eliminateCompletedRows = function(playground) {
		var r = gameController.rows - 1;
		while (r >= 0) {
			var hasEmptyCells = false;
			for (var c = 0; c < gameController.columns; c++) {
				if (playground.rows[r].cells[c].color == 0)
					hasEmptyCells = true;
			}
			if (!hasEmptyCells) {
				gameController.dropRowsAbove(r);
				gameController.timeToDrop = (this.timeToDrop * 15) >> 4;
			} else
				r--;
		}
	};

	this.applyGravity = function() {
		var movement = false;
		if (gravity) {
			for (var c = 0; c < columns; c++) {
				var column = playground.rows[c].cells;
				for (var r = (rows - 1); r > 0; r--)
					if (column[r].color == 0
							&& column[r].color != column[r - 1].color) {
						movement = true;
						column[r].color = column[r - 1].color;
						column[r - 1].color = 0;
					}
			}
		}
		return movement;
	};

	this.dropRowsAbove = function(bottomRow) {
		for (var r = bottomRow; r > 0; r--)
			for (var c = 0; c < columns; c++)
				playground.rows[r].cells[c].color = playground.rows[r - 1].cells[c].color;
		for (var c = 0; c < columns; c++) {
			playground.rows[0].cells[c].color = 0;
		}
	};

	this.updateGraphics = function() {
		if (isInternetExplorer()) {
			// IE doesn't evaluate mustaches in css styles, so we have to do this manually here
			var pg = document.getElementById("playground");
			for (var r = 0; r < this.rows; r++) {
				var row = pg.children[r];
				for (var c=0; c < this.columns; c++) {
					var cell = row.children[c];
					var color = playground.rows[r].cells[c].color;
					cell.style.backgroundColor = scope.brickColor(color);
				}
			}
		}
		else scope.$apply();
	};

	/** This method is also called as a static function! */
	this.update = function(e) {
		counter++;
		if (null == tetromino) {
			if (!gameController.applyGravity()) {
				if (!gameController.addRandomTetromino()) {
					gameController.endOfGame();
					return;
				}
			}
		}
		var now = new Date();
		if (now.getTime() > gameController.nextDrop) {
			gameController.dropTile();
			if (this != gameController) {
				gameController.updateGraphics();
			}
			gameController.nextDrop = now.getTime() + timeToDrop;
		}
		window.requestAnimationFrame(gameController.update);
	};

	this.startGame = function() {
		timeToDrop = 500;
		var now = new Date();
		this.nextDrop = now.getTime() + timeToDrop;
		gameActive = true;
		this.update(null);
	};

	/** returns false if the next tile cannot be drawn */
	this.addRandomTetromino = function() {
		tetromino = new Tetromino();
		tetromino.inittetromino(columns);
		if (tetromino.canDrawTile(playground)) {
			tetromino.drawTile(playground);
			return true;
		}
		return false;
	};

	this.endOfGame = function() {
		gameActive = false;
	};

	/** This method is called as a static function! */
	this.onKey = function(event) {
		event = event || window.event;
		var code = event.keyCode;
		console.log(code);
		if (code == 37) {
			tetromino.moveTile(-1, playground);
			// drawBricks();
			// updateGraphicsCallback();
		}
		if (code == 39) {
			tetromino.moveTile(1, playground);
			// drawBricks();
			// updateGraphicsCallback();
		}
		if (code == 40) {
			tetromino.rotateTile(playground, 90);
			// drawBricks();
			// updateGraphicsCallback();
		}
		if (code == 38) {
			tetromino.rotateTile(playground, 270);
			// drawBricks();
			// updateGraphicsCallback();
		}
		if (code == 13) {
			gameController.dropTile();
		}
		if (code == 32) {
			while (null != tetromino) {
				gameController.dropTile();
				// drawBricks();
				// updateGraphicsCallback();
			}
		}
		gameController.updateGraphics();
	};
	document.onkeydown = this.onKey;
	

	window.requestAnimFrame = (function() {
		return window.requestAnimationFrame
				|| window.webkitRequestAnimationFrame
				|| window.mozRequestAnimationFrame
				|| window.oRequestAnimationFrame
				|| window.msRequestAnimationFrame
				|| function(/* function */callback, /* DOMElement */element) {
					window.setTimeout(callback, 1000 / 60);
				};
	})();
}

// function copied and adapted from http://stackoverflow.com/questions/19999388/jquery-check-if-user-is-using-ie
function isInternetExplorer() {

    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer, return version number 
    {
        return true;
    }
    else             
        return false;
}