/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.selectOneRadio;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.selectoneradio.SelectOneRadioRenderer;

import de.beyondjava.jsfComponents.common.*;

/**
 * Add some AngularJS functionality to a standard PrimeFaces SelectOneRadio (aka
 * radio button).
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.SelectOneRadio")
public class NGSelectOneRadioRenderer extends SelectOneRadioRenderer {

   /**
    * Renders ng-model according to the bean attribute's properties.
    */
   @Override
   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs)
         throws IOException {
      super.renderPassThruAttributes(context, component, attrs);
      ResponseWriter writer = context.getResponseWriter();

      NGUIComponentInfo info = NGUIComponentTools.getInfo(context, component);
      String model = info.getNGModel();
      writer.writeAttribute("ng-model", model, "ng-model");
   }

}
