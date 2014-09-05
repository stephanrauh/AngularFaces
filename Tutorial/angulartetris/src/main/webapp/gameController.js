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
	this.rows=25;
	var columns=10;
	var timeToDrop;
	var counter = 0;
	var gameActive = false;
	var gravity = false;
	var preview = false;

	var tetromino = null;

	// playground is a two-dimensional array of integers, which in turn are
	// color codes
	var playground = null;

	this.init=function(grid) {
		playground = grid;
		tetromino = null;
		timeToDrop = 500;
	};

	this.showHighscore=function() {
		window.alert("Highscore is yet to be implemented.");
	};

	this.dropTile=function() {
		if (!tetromino.moveTileDown(playground)) {
			tetromino = null;
			this.eliminateCompletedRows(playground);
		}
	};

	// playground is a two-dimensional array of integers, which in turn are
	// color codes
	this.eliminateCompletedRows=function(playground) {
		var r = rows - 1;
		while (r >= 0) {
			var hasEmptyCells = false;
			for (var c = 0; c < columns; c++) {
				if (playground.rows[c].cells[r].color == 0)
					hasEmptyCells = true;
			}
			if (!hasEmptyCells) {
				this.dropRowsAbove(r);
				timeToDrop = (timeToDrop * 15) >> 4;
			} else
				r--;
		}
	};

	this.applyGravity=function() {
		var movement = false;
		if (gravity) {
			for (var c = 0; c < columns; c++) {
				var column = playground.rows[c].cells;
				for (var r = (rows - 1); r > 0; r--)
					if (column[r].color == 0 && column[r].color != column[r - 1].color) {
						movement = true;
						column[r].color = column[r - 1].color;
						column[r - 1].color = 0;
					}
			}
		}
		return movement;
	};

	this.dropRowsAbove=function(bottomRow) {
		for (var r = bottomRow; r > 0; r--)
			for (var c = 0; c < columns; c++)
				playground.rows[c].cells[r].color = playground.rows[c].cells[r - 1].color;
		for (var c = 0; c < columns; c++) {
			playground.rows[c].cells[0].color = 0;
		}
	};


	this.update=function(e) {
		counter++;
		if (null == tetromino) {
			if (!this.applyGravity()) {
				if (!this.addRandomTetromino()) {
					this.endOfGame();
					return;
				}
			}
		}
//		if (watch.elapsedMilliseconds > timeToDrop) {
			this.dropTile();
//			this.drawBricks();
//			watch.reset();
//		}
//		keyboard.reset();
//		window.requestAnimationFrame(update);
	};

	this.startGame=function() {
		timeToDrop = 500;
		gameActive = true;
		this.update(null);
	};

	/** returns false if the next tile cannot be drawn */
	this.addRandomTetromino=function() {
		tetromino = new Tetromino();
		tetromino.inittetromino(columns);
		if (tetromino.canDrawTile(playground)) {
			tetromino.drawTile(playground);
			return true;
		}
		return false;
	};

	this.endOfGame=function() {
		gameActive = false;
	};
	
	this.onKey=function(event) {
		event = event || window.event;
		var code = event.keyCode;
		console.log(code);
		if (code == 37) {
			tetromino.moveTile(-1, playground);
//			drawBricks();
//			updateGraphicsCallback();
		}
		if (code == 39) {
			tetromino.moveTile(1, playground);
//			drawBricks();
//			updateGraphicsCallback();
		}
		if (code == 40) {
			tetromino.rotateTile(playground, 90);
//			drawBricks();
//			updateGraphicsCallback();
		}
		if (code == 38) {
			tetromino.rotateTile(playground, 270);
//			drawBricks();
//			updateGraphicsCallback();
		}
		if (code == 13) {
			gameController.dropTile();
		}
		if (code == 32) {
			while (null != tetromino) {
				gameController.dropTile();
//				drawBricks();
//				updateGraphicsCallback();
			}
		}
		scope.$apply();
	};
	document.onkeydown = this.onKey;

}

