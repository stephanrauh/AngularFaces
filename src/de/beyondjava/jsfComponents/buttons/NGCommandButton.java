/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.buttons;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;

import org.primefaces.component.commandbutton.CommandButton;

/**
 * Enhanced PrimeFaces button with AngularJS support.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.CommandButton")
public class NGCommandButton extends CommandButton {
   public static final String COMPONENT_FAMILY = "de.beyondjava.CommandButton";

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getOncomplete() {
      String ngApp = null;
      UIComponent c = getParent();
      while (c != null) {
         if (c.getAttributes().get("ng-app") != null) {
            ngApp = (String) c.getAttributes().get("ng-app");
            break;
         }
         c = c.getParent();
      }
      if (ngApp != null) {
         String s = super.getOncomplete();
         if ((s == null) || (s.length() == 0)) {
            return "reinitAngular('" + ngApp + "')";
         }
         else {
            return "reinitAngular('" + ngApp + "'); " + s;
         }
      }
      return null;
   }

   @Override
   public String getProcess() {
      String processID = super.getProcess();
      if (null == processID) {
         processID = "@form";
      }
      return processID;
   }

   @Override
   public String getUpdate() {
      String updateID = super.getUpdate();
      if (null == updateID) {
         updateID = "@form";
      }
      return updateID;
   }

}
