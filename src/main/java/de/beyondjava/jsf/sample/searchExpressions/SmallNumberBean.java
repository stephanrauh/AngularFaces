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
   @NotNull
   @Max(10)
   @Size(min = 1, max = 1)
   private double smallNumber = 10000;

   public double getSmallNumber() {
      return smallNumber;
   }

   public void setSmallNumber(double smallNumber) {
      this.smallNumber = smallNumber;
   }

   public void showErrors() {
   }

}
