package de.beyondjava.jsf.sample.additions

import javax.faces.bean.*
import javax.validation.constraints.*

@ManagedBean
@SessionScoped
public class CalculatorBean implements Serializable {
   @Min(7L)
   @Max(50L)
   int number1 = 42

   @Max(100L)
   @Min(10L)
   int number2 = 65

   int result = 0

   public String add() {
      result = number1 + number2
      return null
   }
}
