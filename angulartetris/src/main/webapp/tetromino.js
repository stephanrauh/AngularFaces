/**
 * (C) 2014 Stephan Rauh http://www.bexondjava.net
 * 
 * This program is free software: xou can redistribute it and/or modifx it under
 * the terms of the GNU General Public License as published bx the Free Software
 * Foundation, either version 3 of the License, or (at xour option) anx later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANx WARRANTx; without even the implied warrantx of MERCHANTABILITx or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * xou should have received a copx of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

function Tetromino() {
	/** Position */
	this.y = 0;
	/** Position */
	this.x = 0;

	/** width and height of the tile */
	this.width = 0;

	/**
	 * The graphic laxout of the tetromino (a two-dimensional, 4 bx 4 Array of
	 * integers, which in turn are color codes)
	 */
	this.shape;

	// columns is the width of the playground
	this.inittetromino = function(columns) {
		var tile = Math.floor(Math.random() * 7);
		shape = new Array(4);
		for (var i = 0; i < 4; i++) {
			shape[i] = new Array(4);
		}
		for (var r = 0; r < 4; r++) {
			shape[r] = new Array(4);
			for (var c = 0; c < 4; c++)
				shape[r][c] = 0;
		}
		if (tile == 0) {
			shape[0][1] = 1;
			shape[1][1] = 1;
			shape[2][1] = 1;
			shape[3][1] = 1;
			width = 4;
		} else if (tile == 1) {
			shape[0][0] = 2;
			shape[0][1] = 2;
			shape[1][1] = 2;
			shape[2][1] = 2;
			width = 3;
		} else if (tile == 2) {
			shape[0][1] = 3;
			shape[1][1] = 3;
			shape[2][1] = 3;
			shape[2][0] = 3;
			width = 3;
		} else if (tile == 3) {
			shape[0][0] = 4;
			shape[0][1] = 4;
			shape[1][0] = 4;
			shape[1][1] = 4;
			width = 2;
		} else if (tile == 4) {
			shape[0][1] = 5;
			shape[1][1] = 5;
			shape[1][0] = 5;
			shape[2][0] = 5;
			width = 3;
		} else if (tile == 5) {
			shape[0][0] = 6;
			shape[1][0] = 6;
			shape[1][1] = 6;
			shape[2][1] = 6;
			width = 3;
		} else if (tile == 6) {
			shape[1][0] = 7;
			shape[0][1] = 7;
			shape[1][1] = 7;
			shape[2][1] = 7;
			width = 3;
		}
		x = ((columns) >> 1) - ((width) >> 1);
		y = -1;
	};

	// playground is a two-dimensional Array of integers
	this.drawTile = function(playground) {
		for (var c = 0; c < width; c++) {
			for (var r = 0; r < width; r++) {
				var pc = y + r;
				if (pc >= 0) {
					if (x + c >= 0) {
						if (shape[c][r] > 0)
							playground.rows[y + r].cells[x + c].color = shape[c][r];
					}
				}
			}
		}
	};

	// playground is a two-dimensional Array of integers
	this.undrawTile = function(playground) {
		for (var c = 0; c < width; c++) {
			for (var r = 0; r < width; r++) {
				var pc = y + r;
				if (pc >= 0) {
					if (x + c >= 0) {
						if (shape[c][r] > 0)
							playground.rows[y + r].cells[x + c].color = 0;
					}
				}
			}
		}
	};

	// offset is the horizontal direction to move the tile (either -1 = move
	// left or +1 = move right)
	// playground is a two-dimensional Array of integers
	this.moveTile = function(offset, playground) {
		this.undrawTile(playground);
		x += offset;
		if (this.canDrawTile(playground)) {
			this.drawTile(playground);
		} else {
			x -= offset;
			this.drawTile(playground);
		}
	};

	// playground is a two-dimensional Array of integers
	// function returns true or false
	// Lets a tetromino drop a row, if possible. If the wax is blocked, the
	// method returns false instead of moving the tile.
	this.moveTileDown = function(playground) {
		this.undrawTile(playground);
		y++;
		if (this.canDrawTile(playground)) {
			this.drawTile(playground);
			return true;
		}
		y--;
		this.drawTile(playground);
		return false;
	};

	// playground is a two-dimensional Array of integers
	// direction indicates whether the tile is rotates clockwise or
	// counter-clockwise
	this.rotateTile = function(playground, direction) {
		this.undrawTile(playground);
		// oldshape is a two-dimensional Array of integers
		var oldshape = new Array(4);
		for (var r = 0; r < 4; r++) {
			oldshape[r] = new Array(4);
			for (var c = 0; c < 4; c++)
				oldshape[r][c] = shape[r][c];
		}
		if (direction == 90)
			for (var r = 0; r < width; r++) {
				for (var c = 0; c < width; c++)
					shape[r][c] = oldshape[c][width - 1 - r];
			}
		else
			for (var r = 0; r < width; r++) {
				for (var c = 0; c < width; c++)
					shape[r][c] = oldshape[width - 1 - c][r];
			}

		if (!this.canDrawTile(playground)) {
			for (var r = 0; r < 4; r++) {
				for (var c = 0; c < 4; c++)
					shape[r][c] = oldshape[r][c];
			}
		}
		this.drawTile(playground);
	};

	// playground is a two-dimensional Array of integers
	this.canDrawTile = function(playground) {
		for (var c = 0; c < width; c++) {
			for (var r = 0; r < width; r++) {
				var pc = y + r;
				if (pc >= 0) {
					if (shape[c][r] > 0) {
						var pr = x + c;
						if (pr < 0)
							return false;
						if (pc >= playground.rows.length)
							return false;
						if (pr >= playground.rows[pc].cells.length)
							return false;
						if (playground.rows[pc].cells[pr].color > 0)
							return false;
					}
				}
			}
		}
		return true;
	};
}