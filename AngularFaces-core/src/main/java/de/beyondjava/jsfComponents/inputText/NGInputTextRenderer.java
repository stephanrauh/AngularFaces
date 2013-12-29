/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsfComponents.inputText;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import de.beyondjava.jsfComponents.common.*;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.InputText")
public class NGInputTextRenderer extends org.primefaces.component.inputtext.InputTextRenderer {

   private void readJSR303Annotations(UIComponent component, ResponseWriter writer) throws IOException {
      NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(component);
      if (info.isHasMax()) {
         writer.writeAttribute("max", info.getMax(), "max");

      }
      if (info.isHasMin()) {
         writer.writeAttribute("min", info.getMin(), "min");
      }
      if (info.isInteger()) {
         writer.writeAttribute("integer", "", "integer");
      }
      if (info.isRequired()) {
         writer.writeAttribute("required", "", "required");
      }
   }

   /**
    * Renders ng-model, min, max, integer and required according to the bean
    * attribute\"s properties.
    */
   @Override
   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs)
         throws IOException {
      super.renderPassThruAttributes(context, component, attrs);
      ResponseWriter writer = context.getResponseWriter();

      NGUIComponentInfo info = NGUIComponentTools.getInfo(context, component);
      String model = info.getNGModel();
      writer.writeAttribute("ng-model", model, "ng-model");

      readJSR303Annotations(component, writer);
   }
}
