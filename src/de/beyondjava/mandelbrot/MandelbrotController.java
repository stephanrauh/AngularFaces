/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.mandelbrot;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@ManagedBean
public class MandelbrotController implements Serializable {
   private static final long serialVersionUID = 685843404038345451L;

   double xMax = 2;

   double xMin = -2;

   double yMax = 2;

   double yMin = -2;

   public void calculateAction() {
      int[][] array = new MandelbrotCalculator().calculate();
   }

   public double getxMax() {
      return xMax;
   }

   public double getxMin() {
      return xMin;
   }

   public double getyMax() {
      return yMax;
   }

   public double getyMin() {
      return yMin;
   }

   public void setxMax(double xMax) {
      this.xMax = xMax;
   }

   public void setxMin(double xMin) {
      this.xMin = xMin;
   }

   public void setyMax(double yMax) {
      this.yMax = yMax;
   }

   public void setyMin(double yMin) {
      this.yMin = yMin;
   }

}
