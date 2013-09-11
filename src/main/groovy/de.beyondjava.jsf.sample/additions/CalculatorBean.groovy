package de.beyondjava.jsf.sample.additions

import javax.faces.bean.*
import javax.validation.constraints.*

@ManagedBean
@SessionScoped
public class CalculatorBean implements Serializable {
   @Min(7L)
   @Max(50L)
   @NotNull
   int number1 = 42

   @Max(100L)
   @Min(10L)
   @NotNull
   int number2 = 65

   @Max(100L)
   @Min(10L)
   @NotNull
   int number3 = 33


   int result = 0

   public String add() {
      result = number1 + number2
      return null
   }
}
