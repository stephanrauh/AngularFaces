/*
 * Generated, Do Not Modify
 */
package de.beyondjava.jsfComponents.message;

import java.io.IOException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css") })
@FacesComponent("de.beyondjava.Message")
public class NGMessage extends org.primefaces.component.message.Message
{
   public static final String COMPONENT_FAMILY = "de.beyondjava.Message";

   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   private UIComponent target;

   public UIComponent getTarget()
   {
      return target;
   }

   public void setTarget(UIComponent target)
   {
      this.target = target;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.
    * FacesContext)
    */
   @Override
   public void encodeBegin(FacesContext context) throws IOException
   {
      new NGMessageRenderer().encodeBegin(context, this);
      // super.encodeBegin(context);
   }

}