package de.beyondjava.jsf.sample.datatable;

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

public class Car
{
   @NotNull
   String model;
   @Min(1870L)
   @Max(2020L)
   int year;
   @NotNull
   @Size(min=3, max=10)
   String manufacturer;
   @NotNull
   @Size(min=3, max=7)
   String color;

   public Car(String model, int year, String manufacturer, String color)
   {
      this.model = model;
      this.year = year;
      this.manufacturer = manufacturer;
      this.color = color;
   }
}

