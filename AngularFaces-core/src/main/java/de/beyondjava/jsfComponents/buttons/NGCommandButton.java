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
package de.beyondjava.jsfComponents.buttons;

import javax.el.MethodExpression;
import javax.faces.component.*;
import javax.faces.event.ActionListener;

import org.primefaces.component.commandbutton.CommandButton;

import de.beyondjava.jsfComponents.common.NGWordUtiltites;

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

   /**
    * Adds code to call the AngularJS model function and to intercept the server
    * request if desired.
    */
   @Override
   public String getOnclick() {
      String original = super.getOnclick();
      String suppressServerRequest = null;
      if (getAttributes().containsKey("suppressServerRequest")) {
         suppressServerRequest = (String) getAttributes().get("suppressServerRequest");
      }
      if (suppressServerRequest == null) {
         suppressServerRequest = "false";
      }
      String ngFunction = null;
      if (getAttributes().containsKey("ng-function")) {
         ngFunction = (String) getAttributes().get("ng-function");
         if (!ngFunction.contains("(")) {
            ngFunction += "()";
         }
      }
      String angularFunction = "";
      if ((null != ngFunction) && (ngFunction.length() > 0)) {
         angularFunction = "var $scope = angular.element(\"body\").scope(); $scope.$apply(function() { $scope."
               + ngFunction + "; });";
      }
      if (null == original) {
         original = "";
      }
      if (!(original.endsWith(";"))) {
         original += ";";
      }
      // onClick must not return true! If it does, the request happens to be a
      // non-AJAX request. So we have to add the if condition.
      return angularFunction + original + " if (" + suppressServerRequest + ") return false;";
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
            return "reinitAngular(\"" + ngApp + "\")";
         }
         else {
            return "reinitAngular(\"" + ngApp + "\"); " + s;
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

   /**
    * if no label is provided by the XHTML file, try to guess it from the
    * ng-model attribute.
    */
   @Override
   public Object getValue() {
      Object label = super.getValue();
      if (null != label) {
         return label;
      }
      MethodExpression actionExpression = getActionExpression();
      if (null == actionExpression) {
         ActionListener[] actionListeners = getActionListeners();
         if ((null != actionListeners) && (actionListeners.length > 0)) {
            String a = actionListeners[0].toString();
            a = a.replace("#{", "").replace("}", "");
            int pos = (a.lastIndexOf('.'));
            if (pos > 0) {
               a = a.substring(pos);
            }
            return NGWordUtiltites.labelFromCamelCase(a);
         }
      }
      String a = actionExpression.getExpressionString();
      a = a.replace("#{", "").replace("}", "");
      int pos = (a.lastIndexOf('.'));
      if (pos > 0) {
         a = a.substring(pos + 1);
      }
      return NGWordUtiltites.labelFromCamelCase(a);
   }
}
