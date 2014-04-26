package de.beyondjava.angularFaces.puiAngularController;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.IAngularController;

/**
 * PuiAngularController is a HTML span element with an AngularDart controller.
 */
@FacesComponent("de.beyondjava.angularFaces.puiAngularController.PuiAngularController")
public class PuiAngularController extends UIOutput implements IAngularController {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.angularFaces.puiAngularController.PuiAngularController");

    static {
        LOGGER.info("AngularFaces component 'PuiAngularController' is available for use.");
    }

    public PuiAngularController() {
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
