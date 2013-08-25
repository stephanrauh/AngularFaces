package de.beyondjava.mandelbrot;

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

   public int[][] calculate() {
      long timer = System.nanoTime();
      double cxmin = -2;
      double cxmax = 2;
      double cymin = -1;
      double cymax = 1;
      int iter = 255;
      int width = 1024;
      int height = 1024;
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
