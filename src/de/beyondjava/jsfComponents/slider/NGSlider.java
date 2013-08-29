/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.slider;

import javax.faces.component.FacesComponent;

import org.primefaces.component.slider.Slider;

/**
 * NGSlider is just an ordinary PrimeFaces Slider that updates the Angular Model
 * when the slider is moved.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.Slider")
public class NGSlider extends Slider {
   /**
    * Adds the code updating the Angular model when the slider is being moved.
    */
   @Override
   public String getOnSlide() {
      String activateAngular = "";
      String targets = getFor();
      if (null != targets) {
         String[] targetArray = targets.split(",");
         for (String it : targetArray) {
            activateAngular += "angular.element($('#" + it + "')).triggerHandler('input');";
         }
      }
      String original = super.getOnSlide();
      if (null == original) {
         return activateAngular;
      }
      return activateAngular + original;

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
            activateAngular += "angular.element($('#" + it + "')).triggerHandler('input');";
         }
      }
      String original = super.getOnSlideEnd();
      if (null == original) {
         return activateAngular;
      }
      return activateAngular + original;

   }

}
