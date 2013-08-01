package de.beyondjava.jsf.sample;

import javax.faces.bean.ManagedBean
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped
import javax.faces.event.ActionEvent
import javax.validation.ValidationException
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@ManagedBean
@SessionScoped
public class CalculatorBean implements Serializable
{
   @Min(7L)
   @Max(50L)
   int number1 = 42;

   @Max(100L)
   @Min(10L)
   int number2 = 65;

   int result = 0;

   public void add()
   {
      result = number1 + number2;
   }
}
