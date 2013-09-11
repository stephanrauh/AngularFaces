/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.selectBooleanCheckbox;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckboxRenderer;

import de.beyondjava.jsfComponents.common.NGUIComponentInfo;
import de.beyondjava.jsfComponents.common.NGUIComponentTools;

/**
 * Add some AngularJS functionality to a standard PrimeFaces
 * SelectBooleanCheckbox.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.SelectBooleanCheckbox")
public class NGSelectBooleanCheckboxRenderer extends SelectBooleanCheckboxRenderer {
 
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
