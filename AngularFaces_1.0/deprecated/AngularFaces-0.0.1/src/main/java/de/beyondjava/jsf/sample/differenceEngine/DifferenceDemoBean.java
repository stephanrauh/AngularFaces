/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.differenceEngine;

import javax.faces.bean.*;
import javax.faces.event.ValueChangeEvent;
import javax.validation.constraints.Size;

@ManagedBean
@SessionScoped
public class DifferenceDemoBean {
   private String city;
   @Size(max = 30)
   private String firstName = "John";

   private boolean firstSectionVisible = true;
   @Size(max = 30)
   private String lastName = "Doe";
   private boolean secondSectionVisible = true;

   private String street = this.toString();
   private boolean thirdSectionVisible = true;
   private String zipcode;

   public void changeCity() {
      String[] cities = new String[] { "Armsheim", "Biebelsheim", "Crumstadt", "Dolgesheim", "Ebersheim",
            "Farmersheim", "Gundersheim", "Hahnheim", "Immesheim", "Jugenheim", "Köngernheim", "Laubenheim",
            "Monsheim", "Oppenheim", "Pfeddersheim" };
      long index = (long) Math.floor(Math.random() * cities.length);
      city = cities[(int) index];
   }

   /**
    * @return the city
    */
   public String getCity() {
      return city;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   /**
    * @return the street
    */
   public String getStreet() {
      return street;
   }

   /**
    * @return the zipcode
    */
   public String getZipcode() {
      return zipcode;
   }

   /**
    * @return the firstSectionVisible
    */
   public boolean isFirstSectionVisible() {
      return firstSectionVisible;
   }

   /**
    * @return the secondSectionVisible
    */
   public boolean isSecondSectionVisible() {
      return secondSectionVisible;
   }

   /**
    * @return the thirdSectionVisible
    */
   public boolean isThirdSectionVisible() {
      return thirdSectionVisible;
   }

   /**
    * @param city
    *           the city to set
    */
   public void setCity(String city) {
      this.city = city;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   /**
    * @param firstSectionVisible
    *           the firstSectionVisible to set
    */
   public void setFirstSectionVisible(boolean firstSectionVisible) {
      this.firstSectionVisible = firstSectionVisible;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   /**
    * @param secondSectionVisible
    *           the secondSectionVisible to set
    */
   public void setSecondSectionVisible(boolean secondSectionVisible) {
      this.secondSectionVisible = secondSectionVisible;
   }

   /**
    * @param street
    *           the street to set
    */
   public void setStreet(String street) {
      this.street = street;
   }

   /**
    * @param thirdSectionVisible
    *           the thirdSectionVisible to set
    */
   public void setThirdSectionVisible(boolean thirdSectionVisible) {
      this.thirdSectionVisible = thirdSectionVisible;
   }

   /**
    * @param zipcode
    *           the zipcode to set
    */
   public void setZipcode(String zipcode) {
      this.zipcode = zipcode;
   }

   public String showErrors() {
      return "ok";
   }

   public void toggleFirstSection(javax.faces.event.AjaxBehaviorEvent event) {
      firstSectionVisible = !firstSectionVisible;
   }

   public void toggleFirstSectionPrime() {
      // firstSectionVisible = !firstSectionVisible;
   }

   public void toggleSecondSectionAndChangeCity() {
      secondSectionVisible = !secondSectionVisible;
      changeCity();
   }

   public void toggleSecondSectionAndChangeCity(javax.faces.event.AjaxBehaviorEvent event) {
      // secondSectionVisible = !secondSectionVisible;
      changeCity();
   }

   public void toggleThirdSection(javax.faces.event.AjaxBehaviorEvent event) {
      // thirdSectionVisible = !thirdSectionVisible;
      firstSectionVisible = !firstSectionVisible;
   }

   public void toggleThirdSectionPrime() {
      // thirdSectionVisible = !thirdSectionVisible;
      firstSectionVisible = !firstSectionVisible;

   }

   public void valueChangeListener(ValueChangeEvent event) {

   }

}
