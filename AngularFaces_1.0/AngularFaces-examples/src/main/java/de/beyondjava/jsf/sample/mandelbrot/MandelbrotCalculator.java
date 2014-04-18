/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
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
package de.beyondjava.jsf.sample.mandelbrot;

public class MandelbrotCalculator {

   public static int calculatePixel(int infinity, double cx, double cy) {
      int n = 0;
      double x = 0;
      double y = 0;
      double x2 = 0.0d;
      double y2 = 0.0d;
      if ((cx >= -0.55d) && (cx < 0.25d) && (cy >= -0.47) && (cy <= 0.47)) {
         return infinity;
      }
      else {
         while (++n < infinity) {
            y = (2.0d * x * y) + cy;
            x = (x2 - y2) + cx;
            x2 = x * x;
            y2 = y * y;
            if ((x2 + y2) >= 4.0d) {
               break;
            }
         }
      }
      return n;
   }

   public int[][] calculate(double cxmin, double cxmax, double cymin, double cymax, int iter, int resolution) {
      long timer = System.nanoTime();
      int width = resolution;
      int height = resolution;
      final int[][] image = new int[width][height];
      for (int x = 0; x < width; x++) {
         image[x] = new int[height];
         double cx = cxmin + (((cxmax - cxmin) * x) / width);

         for (int y = 0; y < height; y++) {

            double cy = cymin + (((cymax - cymin) * y) / height);

            image[x][y] = calculatePixel(iter, cx, cy);
         }
      }
      System.out.println((((System.nanoTime() - timer) / 1000) / 1000.0d) + " ms");
      return image;
   }

}
