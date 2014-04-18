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
package de.beyondjava.jsfComponents.slider;

import java.io.IOException;

import javax.faces.component.*;
import javax.faces.context.FacesContext;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.slider.Slider;
import org.primefaces.expression.SearchExpressionFacade;

import de.beyondjava.jsfComponents.common.*;

/**
 * NGSlider is just an ordinary PrimeFaces Slider that updates the Angular Model
 * when the slider is moved.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.Slider")
public class NGSlider extends Slider {

   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      int maxValue = getMaxValue();
      int minValue = getMinValue();
      if (maxValue == 100 /** default value */
      ) {
         String targetIDs = getFor();
         String[] ids = targetIDs.split(",");
         UIComponent target = SearchExpressionFacade.resolveComponent(context, this, ids[ids.length - 1]);
         NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(target);
         if (info.isHasMax()) {
            setMaxValue((int) info.getMax());
         }
      }
      if (minValue == 0 /** default value */
      ) {
         String targetIDs = getFor();
         String[] ids = targetIDs.split(",");
         UIComponent target = SearchExpressionFacade.resolveComponent(context, this, ids[0]);
         NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(target);
         if (info.isHasMin()) {
            setMinValue((int) info.getMin());
         }
      }
      setOnSlide(context);
   }

   /**
    * Adds the code updating the Angular model when the slider has been moved.
    */
   @Override
   public String getOnSlideEnd() {
      String activateAngular = "";
      String targets = getFor();
      if (null != targets) {
         String[] targetArray = targets.split(",");
         for (String it : targetArray) {
            activateAngular += "angular.element($(\"#" + it + "\")).triggerHandler(\"input\");";
         }
      }
      String original = super.getOnSlideEnd();
      if (null == original) {
         return activateAngular;
      }
      return activateAngular + original;

   }

   /**
    * Adds the code updating the Angular model when the slider is being moved.
    */
   public void setOnSlide(FacesContext context) {
      String activateAngular = "";
      String targets = getFor();
      if (null != targets) {
         String[] targetArray = targets.split(",");
         for (String it : targetArray) {
            UIComponent target = SearchExpressionFacade.resolveComponent(context, this, it);
            if (target instanceof SelectOneMenu) {
               String widgetVar = ((SelectOneMenu) target).getWidgetVar();
               activateAngular += "PrimeFaces.widgets." + widgetVar + ".selectValue(ui.value);";
            }
            activateAngular += "angular.element($(\"#" + it + "\")).triggerHandler(\"input\");";
         }
      }
      String original = super.getOnSlide();
      if (null == original) {
         setOnSlide(activateAngular);
      }
      setOnSlide(activateAngular + original);
   }

}
