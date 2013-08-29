/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.mandelbrot;

import java.io.Serializable;

import javax.faces.bean.*;
import javax.validation.constraints.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@ManagedBean
@ViewScoped
public class MandelbrotController implements Serializable {
   private static final long serialVersionUID = 685843404038345451L;

   @Min(10)
   @Max(100)
   private int aperture = 25;

   private int[][] mandelbrotSet;

   private int maxIterations = 1023;

   @Min(1)
   @Max(4)
   private int quality = 1;

   @Min(128)
   @Max(2048)
   private int resolution = 256;

   @NotNull
   @Min(-2)
   @Max(1)
   private double xMax = 1;

   @NotNull
   @Min(value = -2)
   @Max(value = 1)
   private double xMin = -2;

   @NotNull
   @Min(value = -1)
   @Max(value = 1)
   private double yMax = 1;
   @NotNull
   @Min(value = -1)
   @Max(value = 1)
   private double yMin = -1;

   public void calculateAction() {
   }

   public int getAperture() {
      return aperture;
   }

   /**
    * @return the mandelbrotSet
    */
   public int[][] getMandelbrotSet() {
      setMandelbrotSet(new MandelbrotCalculator().calculate(xMin, xMax, yMin, yMax, maxIterations, resolution));
      return mandelbrotSet;
   }

   public String getMandelbrotSetAsString() {
      StringBuffer s = new StringBuffer();
      s.append("var data=[];\r\n");
      s.append("var temp=[];\r\n");
      int[][] set = getMandelbrotSet();
      for (int[] element : set) {
         StringBuffer tmp = new StringBuffer();
         for (int value : element) {
            tmp.append(",");

            tmp.append(String.valueOf(value));
         }
         s.append("temp = [" + tmp.substring(1) + "];\r\n");
         s.append("data = data.concat(temp);\r\n");
      }
      return s.toString();
   }

   /**
    * @return the maxIterations
    */
   public int getMaxIterations() {
      return maxIterations;
   }

   public int getQuality() {
      return quality;
   }

   /**
    * @return the resolution
    */
   public int getResolution() {
      return resolution;
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

   public void setAperture(int aperture) {
      this.aperture = aperture;
   }

   /**
    * @param mandelbrotSet
    *           the mandelbrotSet to set
    */
   public void setMandelbrotSet(int[][] mandelbrotSet) {
      this.mandelbrotSet = mandelbrotSet;
   }

   /**
    * @param maxIterations
    *           the maxIterations to set
    */
   public void setMaxIterations(int maxIterations) {
      this.maxIterations = maxIterations;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   /**
    * @param resolution
    *           the resolution to set
    */
   public void setResolution(int resolution) {
      this.resolution = resolution;
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
