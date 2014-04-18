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
package de.beyondjava.jsfComponents.selectOneMenu;

import javax.faces.render.FacesRenderer;

import org.primefaces.component.selectonemenu.SelectOneMenuRenderer;

/**
 * Add some AngularJS functionality to a standard PrimeFaces
 * SelectOneMenuRenderer (aka Combobox).
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.SelectOneMenu")
public class NGSelectOneMenuRenderer extends SelectOneMenuRenderer {
   /**
    * add the ng-model attribute to the sub-component representing the
    * select-one box
    */
   // @Override
   // public void encodeEnd(FacesContext context, UIComponent component)
   // throws IOException {
   // super.encodeEnd(context, component);
   // ResponseWriter writer = context.getResponseWriter();
   // NGSelectOneMenu combobox = (NGSelectOneMenu) component;
   // String initValue = combobox.getNotificationJS();
   // writer.append("\r\n");
   // writer.append("  <script>\r\n " + initValue + "\r\n</script>");
   // writer.append("\r\n");
   // }
}
