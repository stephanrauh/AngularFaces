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
    * Renders ng-model according to the bean attribute\"s properties.
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
