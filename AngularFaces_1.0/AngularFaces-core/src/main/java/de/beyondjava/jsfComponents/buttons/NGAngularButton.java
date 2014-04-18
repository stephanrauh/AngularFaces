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

import javax.faces.component.FacesComponent;

import org.primefaces.component.commandbutton.CommandButton;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.AngularButton")
public class NGAngularButton extends CommandButton {
   public static final String COMPONENT_FAMILY = "de.beyondjava.AngularButton";

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getOnclick() {
      String ngFunction = (String) getAttributes().get("ng-function");
      if (!ngFunction.contains("(")) {
         ngFunction += "()";
      }
      String s = "var $scope = angular.element(\"body\").scope(); $scope.$apply(function() { $scope." + ngFunction
            + "; }); return false;";
      return s;
   }

   @Override
   public String getType() {
      return "button";
   }

}
