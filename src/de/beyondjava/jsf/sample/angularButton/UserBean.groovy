package de.beyondjava.jsf.sample.angularButton;

import javax.faces.bean.ManagedBean
import javax.faces.bean.SessionScoped
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ManagedBean
@SessionScoped
public class UserBean implements Serializable
{
   @NotNull
   @Size(min=3, max=20)
   String firstName;
   
   @NotNull
   @Size(min=1, max=20)
   String lastName;
}
