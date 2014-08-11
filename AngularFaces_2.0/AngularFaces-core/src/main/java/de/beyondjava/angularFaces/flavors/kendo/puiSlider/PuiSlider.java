package de.beyondjava.angularFaces.flavors.kendo.puiSlider;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.IModel;
import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiSlider.PuiSlider")
public class PuiSlider extends UIInput implements IModel, IStyle, IStyleClass {
	enum propertyKeys {
		orientation
	}

	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiSlider.PuiSlider");

	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	public String getOrientation() {
		return (String) getStateHelper().eval(propertyKeys.orientation, null);
	}

	public void setOrientation(String isOrientation) {
		getStateHelper().put(propertyKeys.orientation, isOrientation);
	}

}
