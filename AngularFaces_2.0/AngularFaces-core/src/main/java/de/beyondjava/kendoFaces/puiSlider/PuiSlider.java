package de.beyondjava.kendoFaces.puiSlider;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiSlider.PuiSlider")
public class PuiSlider extends UIInput {
    enum propertyKeys {
        orientation
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiSlider.PuiSlider");

    static {
        LOGGER.info("KendoFaces component 'PuiSlider' is available for use.");
    }

    public PuiSlider() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

    /**
     * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender
     * methods.
     */
    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
    }

}
