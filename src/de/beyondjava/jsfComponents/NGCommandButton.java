/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.CommandButton")
public class NGCommandButton extends CommandButton
{
   public static final String COMPONENT_FAMILY = "de.beyondjava.CommandButton";

   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   /* (non-Javadoc)
    * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
    */
   @Override
   public void encodeBegin(FacesContext context) throws IOException
   {
      // TODO Auto-generated method stub
      super.encodeBegin(context);
   }
   @Override
   public String getOncomplete()
   {
      String s = super.getOncomplete();
      if (s == null || s.length() == 0)
      {
         return "reinitAngular()";
      }
      else
      {
         return "reinitAngular(); " + s;
      }
   }
}
