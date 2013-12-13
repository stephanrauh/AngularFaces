/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.secure;

import javax.faces.bean.*;
import javax.validation.constraints.Size;

@ManagedBean
@RequestScoped
public class SecurityDemoBean {
   @Size(max = 30)
   private String firstName = "John";
   @Size(max = 30)
   private String lastName = "Doe";

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String showErrors() {
      return "ok";
   }
}
