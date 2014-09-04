/**
 * (C) 2014 Stephan Rauh  http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

part of angularTetris;

@Controller(selector: '[MainController]', publishAs: 'ctrl')
class MainController {
  List<int> bricks = null;
  int rows;
  int columns;
  int timeToDrop;
  int counter=0;
  bool gameActive=false;
  bool gravity=false;
  bool preview=false;

  Tetrimino tetrimino = null;

  List<List<int>> playground=null;

  var updateGraphicsCallback=null;



  Keyboard keyboard;
  Stopwatch watch;

  MainController() {
    keyboard = new Keyboard();
  }

  init() {
    if (null == bricks)
    {
      bricks = new List<int>(rows*columns);
      playground = new List<List<int>>(columns);
      for (int c = 0; c < columns; c++)
      {
        List<int> column = new List<int>(rows);
        playground[c]=column;
      }
    }
    for (int c = 0; c < columns; c++)
    {
      List<int> column = playground[c];
      for (int r = 0; r < column.length; r++) {
        column[r]=0;
      }
    }
    tetrimino=null;
    timeToDrop=500;
  }

  drawBricks() {
    int index=0;
    for (int r = 0; r < rows; r++)
      for (int c = 0; c < columns; c++)
        bricks[index++]= playground[c][r];

  }

  void showHighscore() {
    window.alert("Highscore is yet to be implemented.");
  }

  void dropTile() {
    if (!tetrimino.moveTileDown(playground)) {
      tetrimino=null;
      eliminateCompletedRows(playground);
    }
  }

  void eliminateCompletedRows(List<List<int>> playground) {
    int r = rows-1;
    while(r>=0) {
      bool hasEmptyCells=false;
      for (int c=0; c < columns; c++) {
         if (playground[c][r]==0) hasEmptyCells=true;
      }
      if (!hasEmptyCells) {
        dropRowsAbove(r);
        timeToDrop=(timeToDrop*15)>>4;
      }
      else r--;
    }
  }

  bool applyGravity()
  {
    bool movement=false;
    if (gravity) {
      for (int c = 0; c < columns; c++) {
        List<int> column = playground[c];
        for (int r = (rows-1); r > 0; r--)
        if (column[r]==0 && column[r]!=column[r-1]) {
          movement=true;
          column[r]=column[r-1];
          column[r-1]=0;
        }
      }
    }
    return movement;
   }

  void dropRowsAbove(int bottomRow)
  {
    for (int r = bottomRow; r > 0; r--)
      for (int c = 0; c < columns; c++)
        playground[c][r]=playground[c][r-1];
    for (int c = 0; c < columns; c++) {
      playground[c][0]=0;
    }
  }

  void startGame() {
    timeToDrop=500;
    init();
    gameActive=true;
    watch = new Stopwatch();
    watch.start();
    update(null);
  }

  void update(e) {
    counter++;
    if (null == tetrimino) {
      if (!applyGravity()) {
        if (!addRandomTetrimino()) {
          endOfGame();
          return;
        }
      }
    }
    if (keyboard.isPressed(KeyCode.LEFT)) { tetrimino.moveTile(-1, playground);  drawBricks(); updateGraphicsCallback(); }
    if (keyboard.isPressed(KeyCode.RIGHT)) { tetrimino.moveTile(1, playground); drawBricks(); updateGraphicsCallback(); }
    if (keyboard.isPressed(KeyCode.DOWN)) { tetrimino.rotateTile(playground, 90); drawBricks(); updateGraphicsCallback(); }
    if (keyboard.isPressed(KeyCode.UP)) {tetrimino.rotateTile(playground, 270); drawBricks(); updateGraphicsCallback(); }
    if (keyboard.isPressed(KeyCode.SPACE)) {
      while (null != tetrimino) {
        dropTile();
        drawBricks();
        updateGraphicsCallback();
      }
      watch.reset();
    }
    if (watch.elapsedMilliseconds>timeToDrop) {
      dropTile();
      drawBricks();
      updateGraphicsCallback();
      watch.reset();
    }
    keyboard.reset();
    window.requestAnimationFrame(update);
  }

  /** returns false if the next tile cannot be drawn */
  bool addRandomTetrimino() {
    tetrimino=new Tetrimino(columns);
    if (tetrimino.canDrawTile(playground)) {
      tetrimino.drawTile(playground);
      return true;
    }
    return false;
  }

  void endOfGame() {
    gameActive=false;
  }
}