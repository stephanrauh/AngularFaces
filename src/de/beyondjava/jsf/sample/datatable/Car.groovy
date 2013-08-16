package de.beyondjava.jsf.sample.datatable

import javax.faces.event.AjaxBehaviorEvent
import javax.validation.constraints.*

public class Car {
   /** has the car been ordered? */
   boolean ordered
   @Min(10000l)
   @Max(40000l)
   @NotNull
   long price
   @Min(1870L)
   @Max(2020L)
   int year
   @NotNull
   @Size(min=3, max=10)
   String manufacturer
   @NotNull
   @Size(min=3, max=7)
   String color
   /** how many cars of this model have been ordered? */
   @Max(10L)
   int quantity=0


   public Car(long price, int year, String manufacturer, String color) {
      this.price = price
      this.year = year
      this.manufacturer = manufacturer
      this.color = color
      this.ordered=true
      this.quantity=0
   }

   public void onOrderedClick(AjaxBehaviorEvent event) {
      //      ordered=!ordered
   }
}

