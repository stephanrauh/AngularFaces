package de.beyondjava.angularFaces.flavors.angularDart.puiBody;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.html.HtmlBody;

import de.beyondjava.angularFaces.common.IAngularController;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesComponent("de.beyondjava.angularFaces.puiBody.PuiBody")
public class PuiBody extends HtmlBody implements IAngularController {
    enum propertyKeys {
        publishAs, selector
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiBody.PuiBody");

    /**
     * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender
     * methods.
     */
    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
    }

}
