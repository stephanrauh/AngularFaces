/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.AngularButton")
public class NGAngularButton extends CommandButton
{
   public static final String COMPONENT_FAMILY = "de.beyondjava.AngularButton";

   public String getFamily()
   {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getOnclick()
   {
      String ngFunction = (String) getAttributes().get("ng-function");
      if (!ngFunction.contains("("))
      {
         ngFunction += "()";
      }
      String s = "var $scope = angular.element('body').scope(); $scope.$apply(function() { $scope." + ngFunction
            + "; }); return false;";
      return s;
   }

   @Override
   public String getUpdate()
   {
      String updateID = super.getUpdate();
      if (null == updateID)
         updateID = "@form";
      return updateID;
   }

   @Override
   public String getProcess()
   {
      String processID = super.getProcess();
      if (null == processID)
         processID = "@form";
      return processID;
   }

}
