package de.beyondjava.jsf.sample.angularButton;

import java.io.Serializable;

import javax.faces.bean.*;
import javax.validation.constraints.*;

@ManagedBean
@RequestScoped
public class UserBean implements Serializable {

   @NotNull
   @Size(min = 3, max = 20)
   String firstName;

   @NotNull
   @Size(min = 1, max = 20)
   String lastName;

   /**
    * @return the firstName
    */
   public String getFirstName() {
      return this.firstName;
   }

   /**
    * @return the lastName
    */
   public String getLastName() {
      return this.lastName;
   }

   /**
    * @param firstName
    *           the firstName to set
    */
   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   /**
    * @param lastName
    *           the lastName to set
    */
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }
}
