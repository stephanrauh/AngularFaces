/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.selectOneMenu;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.selectonemenu.*;
import org.primefaces.util.ComponentUtils;

import de.beyondjava.jsfComponents.common.*;

/**
 * Add some AngularJS functionality to a standard PrimeFaces
 * SelectOneMenuRenderer (aka Combobox).
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.SelectOneMenu")
public class NGSelectOneMenuRenderer extends SelectOneMenuRenderer {

   @Override
   protected void encodeLabel(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems)
         throws IOException {
      ResponseWriter writer = context.getResponseWriter();
      String valueToRender = ComponentUtils.getValueToRender(context, menu);

      if (menu.isEditable()) {
         writer.startElement("input", null);
         writer.writeAttribute("type", "text", null);
         writer.writeAttribute("name", menu.getClientId() + "_editableInput", null);
         writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);

         if (menu.getTabindex() != null) {
            writer.writeAttribute("tabindex", menu.getTabindex(), null);
         }

         if (menu.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
         }

         if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
         }

         if (menu.getMaxlength() != Integer.MAX_VALUE) {
            writer.writeAttribute("maxlength", menu.getMaxlength(), null);
         }
         renderNGModel(context, menu);

         writer.endElement("input");
      }
      else {
         writer.startElement("label", null);
         writer.writeAttribute("id", menu.getClientId() + "_label", null);
         writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);
         writer.write("&nbsp;");
         writer.endElement("label");
      }
   }

   /**
    * Renders ng-model according to the bean attribute's properties.
    */
   protected void renderNGModel(FacesContext context, UIComponent component) throws IOException {
      ResponseWriter writer = context.getResponseWriter();

      NGUIComponentInfo info = NGUIComponentTools.getInfo(context, component);
      String model = info.getNGModel();
      writer.writeAttribute("ng-model", model, "ng-model");
   }

}
