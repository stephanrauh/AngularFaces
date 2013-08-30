/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.slider;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.slider.Slider;
import org.primefaces.expression.SearchExpressionFacade;

import de.beyondjava.jsfComponents.common.ELTools;
import de.beyondjava.jsfComponents.common.NGBeanAttributeInfo;

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
		if (maxValue==100 /** default value */) {
			String targetIDs = getFor();
			String[] ids = targetIDs.split(",");
			UIComponent target = SearchExpressionFacade.resolveComponent(
					context, this, ids[ids.length - 1]);
			NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(target);
			if (info.isHasMax()) {
				setMaxValue((int) info.getMax());
			}
		}
		if (minValue==0 /** default value */) {
			String targetIDs = getFor();
			String[] ids = targetIDs.split(",");
			UIComponent target = SearchExpressionFacade.resolveComponent(
					context, this, ids[0]);
			NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(target);
			if (info.isHasMin()) {
				setMinValue((int) info.getMin());
			}
		}
	}

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
				activateAngular += "angular.element($('#" + it
						+ "')).triggerHandler('input');";
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
				activateAngular += "angular.element($('#" + it
						+ "')).triggerHandler('input');";
			}
		}
		String original = super.getOnSlideEnd();
		if (null == original) {
			return activateAngular;
		}
		return activateAngular + original;

	}

}
