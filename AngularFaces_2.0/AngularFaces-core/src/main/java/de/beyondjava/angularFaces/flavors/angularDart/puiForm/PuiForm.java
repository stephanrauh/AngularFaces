package de.beyondjava.angularFaces.flavors.angularDart.puiForm;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.html.HtmlForm;

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

    /**
     * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender
     * methods.
     */
    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
    }

}
