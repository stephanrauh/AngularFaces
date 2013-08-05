package de.beyondjava.jsf.sample.additions;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.validation.ValidationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ManagedBean
@ViewScoped
public class AdditionBean implements Serializable
{
   @Max(5)
   private String number1 = "42";

   @Size(min = 3, max = 5)
   private String number2 = "67";
   private int result = 0;

   public String getNumber1()
   {
      return number1;
   }

   public void setNumber1(String number1)
   {
      this.number1 = number1;
   }

   public String getNumber2()
   {
      return number2;
   }

   public void setNumber2(String number2)
   {
      this.number2 = number2;
   }

   public int getResult()
   {
      return result;
   }

   public void setResult(int result)
   {
      this.result = result;
   }

   public void calc(String operator)
   {
      switch (operator) {
      case "add":
         add(null);
         break;
      case "subtract":
         minus(null);
         break;
      default:
         throw new ValidationException("undefined operator");
      }
      result = Integer.valueOf(number1) + Integer.valueOf(number2);
   }

   public void add(ActionEvent evt)
   {
      result = Integer.valueOf(number1) + Integer.valueOf(number2);
   }

   public void minus(ActionEvent evt)
   {
      result = Integer.valueOf(number1) + Integer.valueOf(number2);
   }

}
