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
package de.beyondjava.jsf.sample.datatable;

import java.io.Serializable;

import javax.validation.constraints.*;

/**
 * This is the car object used by the data table demo
 * (http://localhost:8080/AngularFaces/AngularFaces-5/index.jsf).
 * 
 * @author Stephan Rauh
 * 
 */
public class Car implements Serializable {
   @NotNull
   @Size(min = 3, max = 7)
   String color;
   @NotNull
   @Size(min = 3, max = 10)
   String manufacturer;
   /** has the car been ordered? */
   boolean ordered;
   @Min(10000l)
   @Max(40000l)
   @NotNull
   long price;
   /** how many cars of this model have been ordered? */
   @Max(10L)
   int quantity = 0;
   @Min(1870L)
   @Max(2020L)
   int year;

   public Car(long price, int year, String manufacturer, String color) {
      this.price = price;
      this.year = year;
      this.manufacturer = manufacturer;
      this.color = color;
      this.ordered = false;
      this.quantity = 0;
   }

   /**
    * @return the color
    */
   public String getColor() {
      return this.color;
   }

   /**
    * @return the manufacturer
    */
   public String getManufacturer() {
      return this.manufacturer;
   }

   /**
    * @return the price
    */
   public long getPrice() {
      return this.price;
   }

   /**
    * @return the quantity
    */
   public int getQuantity() {
      return this.quantity;
   }

   /**
    * @return the year
    */
   public int getYear() {
      return this.year;
   }

   /**
    * @return the ordered
    */
   public boolean isOrdered() {
      return this.ordered;
   }

   /**
    * @param color
    *           the color to set
    */
   public void setColor(String color) {
      this.color = color;
   }

   /**
    * @param manufacturer
    *           the manufacturer to set
    */
   public void setManufacturer(String manufacturer) {
      this.manufacturer = manufacturer;
   }

   /**
    * @param ordered
    *           the ordered to set
    */
   public void setOrdered(boolean ordered) {
      this.ordered = ordered;
   }

   /**
    * @param price
    *           the price to set
    */
   public void setPrice(long price) {
      this.price = price;
   }

   /**
    * @param quantity
    *           the quantity to set
    */
   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   /**
    * @param year
    *           the year to set
    */
   public void setYear(int year) {
      this.year = year;
   }
}
