package de.beyondjava.jsf.sample.angularButton;

import javax.faces.bean.ManagedBean
import javax.faces.bean.RequestScoped
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ManagedBean
@RequestScoped
public class UserBean implements Serializable
{
 
   @NotNull
   @Size(min=3, max=20)
   String firstName;
   
   @NotNull
   @Size(min=1, max=20)
   String lastName;
}
