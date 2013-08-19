/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.Buttons;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.commandbutton.CommandButtonRenderer;

/**
 * Enhanced PrimeFaces button with AngularJS support.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "de.beyondjava.CommandButton", rendererType = "de.beyondjava.CommandButton")
public class NGCommandButtonRenderer extends CommandButtonRenderer {

   private String getNGDisabled(UIComponent component) {
      String formName = null;
      UIComponent c = component.getParent();
      while (c != null) {
         if (c instanceof UIForm) {
            String s = (String) c.getAttributes().get("name");
            if (s != null) {
               formName = s;
               break;
            }

         }
         c = c.getParent();
      }
      if (null != formName) {
         return formName + ".$invalid";
      }
      return null;
   }

   @Override
   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs,
         String[] ignoredAttrs) throws IOException {
      String ngdisabled = getNGDisabled(component);
      if (null != ngdisabled) {
         ResponseWriter writer = context.getResponseWriter();
         writer.writeAttribute("ng-disabled", ngdisabled, "ng-disabled");
      }
      super.renderPassThruAttributes(context, component, attrs, ignoredAttrs);
   }

}
