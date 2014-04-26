package de.beyondjava.angularFaces.puiForm;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.IAngularController;

/**
 * PuiForm is a form with an AngularDart controller.
 */
@FacesComponent("de.beyondjava.angularFaces.puiForm.PuiForm")
public class PuiForm extends HtmlForm implements IAngularController {
    enum propertyKeys {
        publishAs, selector
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiForm.PuiForm");

    static {
        LOGGER.info("AngularFaces component 'PuiForm' is available for use.");
    }

    public PuiForm() {
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
