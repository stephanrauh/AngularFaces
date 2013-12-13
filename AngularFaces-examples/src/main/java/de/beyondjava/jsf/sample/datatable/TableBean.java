package de.beyondjava.jsf.sample.datatable;

import java.io.Serializable;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;

@ManagedBean
@SessionScoped
public class TableBean implements Serializable {

   List<Car> carsSmall;

   String[] colors;

   String[] manufacturers;

   public TableBean() {
      colors = new String[10];
      colors[0] = "Black";
      colors[1] = "White";
      colors[2] = "Green";
      colors[3] = "Red";
      colors[4] = "Blue";
      colors[5] = "Orange";
      colors[6] = "Silver";
      colors[7] = "Yellow";
      colors[8] = "Brown";
      colors[9] = "Maroon";

      manufacturers = new String[10];
      manufacturers[0] = "Mercedes";
      manufacturers[1] = "BMW";
      manufacturers[2] = "Volvo";
      manufacturers[3] = "Audi";
      manufacturers[4] = "Renault";
      manufacturers[5] = "Opel";
      manufacturers[6] = "Volkswagen";
      manufacturers[7] = "Chrysler";
      manufacturers[8] = "Ferrari";
      manufacturers[9] = "Ford";
      carsSmall = new ArrayList<Car>();

      populateRandomCars(carsSmall, 9);
   }

   /**
    * @return the carsSmall
    */
   public List<Car> getCarsSmall() {
      return this.carsSmall;
   }

   /**
    * @return the colors
    */
   public String[] getColors() {
      return this.colors;
   }

   /**
    * @return the manufacturers
    */
   public String[] getManufacturers() {
      return this.manufacturers;
   }

   private String getRandomColor() {
      return colors[(int) (Math.random() * 10)];
   }

   private String getRandomManufacturer() {
      return manufacturers[(int) (Math.random() * 10)];
   }

   private long getRandomPrice() {
      return ((long) Math.floor((Math.random() * 100)) * 300) + 10000;
   }

   private int getRandomYear() {
      return (int) ((Math.random() * 50) + 1960);
   }

   public void onCellEdit(CellEditEvent event) {
      Object oldValue = event.getOldValue();
      Object newValue = event.getNewValue();

      if ((newValue != null) && !newValue.equals(oldValue)) {
         FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "AJAX: Cell Changed", "Old: " + oldValue
               + ", New:" + newValue);
         FacesContext.getCurrentInstance().addMessage(null, msg);
      }
   }

   private void populateRandomCars(List<Car> list, int size) {
      for (int i = 0; i < size; i++) {
         list.add(new Car(getRandomPrice(), getRandomYear(), getRandomManufacturer(), getRandomColor()));
      }
   }

   /**
    * @param carsSmall
    *           the carsSmall to set
    */
   public void setCarsSmall(List<Car> carsSmall) {
      this.carsSmall = carsSmall;
   }

   /**
    * @param colors
    *           the colors to set
    */
   public void setColors(String[] colors) {
      this.colors = colors;
   }

   /**
    * @param manufacturers
    *           the manufacturers to set
    */
   public void setManufacturers(String[] manufacturers) {
      this.manufacturers = manufacturers;
   }
}