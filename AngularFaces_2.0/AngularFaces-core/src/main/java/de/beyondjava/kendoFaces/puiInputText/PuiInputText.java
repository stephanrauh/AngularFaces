package de.beyondjava.kendoFaces.puiInputText;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIInput;

import de.beyondjava.angularFaces.common.ILabel;
import de.beyondjava.angularFaces.common.IModel;
import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiInputText.PuiInputText")
public class PuiInputText extends UIInput implements IModel, IStyle, IStyleClass, ILabel {

	public StateHelper getStateHelper() {
        return super.getStateHelper();
    }
	
	protected String getTypeSpecificAttributes() {
		return "";
	}
}
