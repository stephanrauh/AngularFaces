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

class Tetrimino {
  /** Position */
  int x;
  /** Position */
  int y;
  
  /** width and height of the tile */
  int width;
  
  /** random numbers */
  static Random rnd = new Random();
  
  List<List<int>> shape;
  
  Tetrimino(int columns) {
    x = columns>>1;
    y = 0;
    int tile = rnd.nextInt(7);
    shape = new List<List<int>>(4);
    for (int r = 0; r < 4; r++) {
      shape[r] = new List<int>(4);
      for (int c=0; c < 4; c++) shape[r][c]=0;
    }
    if (tile==0)
    {
      shape[0][1]=1;
      shape[1][1]=1;
      shape[2][1]=1;
      shape[3][1]=1;
      width=4;
    } else if (tile==1) {
      shape[0][0]=2;
      shape[0][1]=2;
      shape[1][1]=2;
      shape[2][1]=2;
      width=3;
    } else if (tile==2) {
      shape[0][1]=3;
      shape[1][1]=3;
      shape[2][1]=3;
      shape[2][0]=3;
      width=3;
    } else if (tile==3) {
      shape[0][0]=4;
      shape[0][1]=4;
      shape[1][0]=4;
      shape[1][1]=4;
      width=2;
    } else if (tile==4) {
      shape[0][1]=5;
      shape[1][1]=5;
      shape[1][0]=5;
      shape[2][0]=5;
      width=3;
    } else if (tile==5) {
      shape[0][0]=6;
      shape[1][0]=6;
      shape[1][1]=6;
      shape[2][1]=6;
      width=3;
    } else if (tile==6) {
      shape[1][0]=7;
      shape[0][1]=7;
      shape[1][1]=7;
      shape[2][1]=7;
      width=3;
    }
  }
  
  void drawTile(List<List<int>> playground) {
    int d = 0;
    if (width==4) d = 1;
    
    for (int c = 0; c < width; c++)
    {
      for (int r = 0; r < width; r++) {
        if (y+r-d>=0) {
          if (shape[c][r]>0)
            playground[x+c-d][y+r-d]=shape[c][r];
        }
      }
    }
  }
  
  void undrawTile(List<List<int>> playground) {
    int d = 0;
    if (width==4) d = 1;
    
    for (int c = 0; c < width; c++)
    {
      for (int r = 0; r < width; r++) {
        if (y+r-d>=0) {
          if (shape[c][r]>0)
          playground[x+c-d][y+r-d]=0;
        }
      }
    }
  }
  
  void moveTile(int offset, List<List<int>> playground) {
    undrawTile(playground);
    x += offset;
    if (canDrawTile(playground)) {
      drawTile(playground);
    }
    else {
      x -= offset;
      drawTile(playground);
    }
  }
  
  /** Lets a tetrimino drop a row, if possible. If the way is blocked, the method returns false instead of moving the tile. */
  bool moveTileDown(List<List<int>> playground){ 
    undrawTile(playground);
    y++;
    if (canDrawTile(playground)) {
      drawTile(playground);
      return true;
    }
    y--;
    drawTile(playground);
    return false;
  }
  
  void rotateTile(List<List<int>> playground, int direction) {
    undrawTile(playground);
    List<List<int>> oldshape = new List<List<int>>(4);
    for (int r = 0; r < 4; r++) {
      oldshape[r] = new List<int>(4);
      for (int c=0; c < 4; c++) oldshape[r][c]=shape[r][c];
    }
    if (direction==90)
      for (int r = 0; r < width; r++) {
         for (int c=0; c < width; c++) shape[r][c]=oldshape[c][width-1-r];
      }
    else
      for (int r = 0; r < width; r++) {
         for (int c=0; c < width; c++) shape[r][c]=oldshape[width-1-c][r];
      }
      
    if (!canDrawTile(playground)) {
      for (int r = 0; r < 4; r++) {
        for (int c=0; c < 4; c++) shape[r][c]=oldshape[r][c];
      }
    }
    drawTile(playground);
  }
  
  bool canDrawTile(List<List<int>> playground) {
    int d = 0;
    if (width==4) d = 1;
    
    for (int c = 0; c < width; c++)
    {
      for (int r = 0; r < width; r++) {
        if (shape[c][r]>0)
        {
          var pc = x+c-d;
          if (pc<0) return false;
          var pr = y+r-d;
          if (pr<0) return false;
          if (pc>=playground.length) return false;
          if (pr>=playground[pc].length) return false;
          if (playground[pc][pr]>0) return false;
        }
      }
    }
    return true;
  }


}