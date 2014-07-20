package de.beyondjava.kendoFaces.puiInputText;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.ILabel;
import de.beyondjava.angularFaces.common.IModel;
import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;
import de.beyondjava.angularFaces.common.IValue;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiInputText.PuiInputText")
public class PuiInputText extends UIInput implements IModel, IStyle, IStyleClass, ILabel {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiInputText.PuiInputText");

    static {
        LOGGER.info("KendoFaces component 'PuiInputText' is available for use.");
    }

    public PuiInputText() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
    }
}
