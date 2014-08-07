package de.beyondjava.angularFaces.flavors.kendo.puiInputText;

import javax.faces.component.FacesComponent;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiDate.puiDate")
public class PuiDate extends PuiInputText {
	protected String getTypeSpecificAttributes() {
		return "kendo-date-picker";
	}
}
