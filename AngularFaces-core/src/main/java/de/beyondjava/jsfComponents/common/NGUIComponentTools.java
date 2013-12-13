/**
 *  (C) Stephan Rauh http://www.beyondjava.net
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
