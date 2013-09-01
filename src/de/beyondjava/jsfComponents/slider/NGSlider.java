/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.slider;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.selectonemenu.SelectOneMenu;
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
		if (maxValue == 100 /** default value */
		) {
			String targetIDs = getFor();
			String[] ids = targetIDs.split(",");
			UIComponent target = SearchExpressionFacade.resolveComponent(
					context, this, ids[ids.length - 1]);
			NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(target);
			if (info.isHasMax()) {
				setMaxValue((int) info.getMax());
			}
		}
		if (minValue == 0 /** default value */
		) {
			String targetIDs = getFor();
			String[] ids = targetIDs.split(",");
			UIComponent target = SearchExpressionFacade.resolveComponent(
					context, this, ids[0]);
			NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(target);
			if (info.isHasMin()) {
				setMinValue((int) info.getMin());
			}
		}
		setOnSlide(context);
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
				UIComponent target = SearchExpressionFacade.resolveComponent(
						context, this, it);
				if (target instanceof SelectOneMenu) {
					String widgetVar = ((SelectOneMenu) target).getWidgetVar();
					activateAngular +="PrimeFaces.widgets." + widgetVar + ".selectValue(ui.value);";
				} else {
					activateAngular += "angular.element($('#" + it
							+ "')).triggerHandler('input');";
				}
			}
		}
		// onSlide="PrimeFaces.widgets.qualityWidget.selectValue('2');"
		String original = super.getOnSlide();
		if (null == original) {
			setOnSlide(activateAngular);
		}
		setOnSlide(activateAngular + original);
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
