/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.secure;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.validation.constraints.Size;

import org.primefaces.context.RequestContext;

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

   public void showErrors() {
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No security breach detected",
            "Maybe you\\'re not a hacker. At least AngularFaces couldn\\'t detect any malicious intent.");

      RequestContext.getCurrentInstance().showMessageInDialog(message);
   }
}
