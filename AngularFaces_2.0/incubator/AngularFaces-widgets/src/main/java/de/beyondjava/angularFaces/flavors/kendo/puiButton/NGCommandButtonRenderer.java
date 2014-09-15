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
package de.beyondjava.angularFaces.flavors.kendo.puiButton;

import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

/**
 * Enhanced PrimeFaces button with AngularJS support.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "de.beyondjava.kendoFaces.puiButton.PuiButton", rendererType = "de.beyondjava.kendoFaces.puiButton.PuiButton")
public class NGCommandButtonRenderer extends HtmlBasicInputRenderer {

//   private String getNGDisabled(UIComponent component) {
//      String formName = null;
//      UIComponent c = component.getParent();
//      while (c != null) {
//         if (c instanceof UIForm) {
//            String s = (String) c.getAttributes().get("name");
//            if (s != null) {
//               formName = s;
//               break;
//            }
//
//         }
//         c = c.getParent();
//      }
//      if (null != formName) {
//         return formName + ".$invalid";
//      }
//      return null;
//   }
//
//   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs,
//         String[] ignoredAttrs) throws IOException {
//      String ngdisabled = getNGDisabled(component);
//      if (null != ngdisabled) {
//         ResponseWriter writer = context.getResponseWriter();
//         writer.writeAttribute("ng-disabled", ngdisabled, "ng-disabled");
//      }
//      super.renderPassThruAttributes(context, component, attrs, ignoredAttrs);
//   }

}
