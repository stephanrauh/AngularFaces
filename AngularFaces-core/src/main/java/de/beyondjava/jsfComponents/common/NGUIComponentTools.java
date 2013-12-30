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
package de.beyondjava.jsfComponents.common;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Extracts informations about a particular UIComponent.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGUIComponentTools {

   private static NGUIComponentInfo calculateArrayIndexAndPrefix(FacesContext context, UIComponent component) {
      String prefix = "";
      String arrayIndex = "";
      NGUIComponent ngComponent = (NGUIComponent) component;
      ngComponent.preventRecursion();
      String original = component.getClientId(context);
      ngComponent.preventRecursion(false);
      String[] parts = original.split(":");
      for (String part : parts) {
         boolean isNumeric = true;
         for (int i = 0; i < part.length(); i++) {
            if (!Character.isDigit(part.charAt(i))) {
               isNumeric = false;
               break;
            }
         }
         if (isNumeric) {
            prefix += "row" + part + "_";
            arrayIndex = arrayIndex + "[" + part + "]";
         }
      }
      String id = ELTools.getNGModel(component);

      NGUIComponentInfo info = new NGUIComponentInfo(prefix, arrayIndex, id);
      return info;
   }

   public static String getArrayIndex(FacesContext context, UIComponent component) {
      NGUIComponentInfo info = calculateArrayIndexAndPrefix(context, component);
      return info.getArrayIndex();
   }

   public static String getClientId(FacesContext context, UIComponent component) {
      NGUIComponentInfo info = calculateArrayIndexAndPrefix(context, component);
      return info.getClientID();
   }

   public static NGUIComponentInfo getInfo(FacesContext context, UIComponent component) {
      NGUIComponentInfo info = calculateArrayIndexAndPrefix(context, component);
      return info;
   }
}
