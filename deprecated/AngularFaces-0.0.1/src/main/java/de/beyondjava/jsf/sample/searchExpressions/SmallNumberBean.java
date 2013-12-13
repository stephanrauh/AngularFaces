/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.searchExpressions;

import javax.faces.bean.*;
import javax.validation.constraints.*;

@ManagedBean
@ViewScoped
public class SmallNumberBean {
   @Max(value = 100l)
   private int rating = 50;

   @Max(value = 10l)
   @Size(min = 1, max = 1)
   private int smallNumber = 10000;

   public int getRating() {
      return rating;
   }

   public int getSmallNumber() {
      return smallNumber;
   }

   public void setRating(int rating) {
      this.rating = rating;
   }

   public void setSmallNumber(int smallNumber) {
      this.smallNumber = smallNumber;
   }

   public void showErrors() {
   }

}
