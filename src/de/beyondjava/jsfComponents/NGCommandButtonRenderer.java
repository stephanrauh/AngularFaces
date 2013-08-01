/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.commandbutton.CommandButtonRenderer;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
@FacesRenderer(componentFamily = "de.beyondjava.CommandButton", rendererType = "de.beyondjava.CommandButton")
public class NGCommandButtonRenderer extends CommandButtonRenderer
{

   /* (non-Javadoc)
    * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
    */
   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException
   {
      // TODO Auto-generated method stub
      super.encodeBegin(context, component);
   }
}
