package de.beyondjava.angularFaces.flavors.kendo.puiBody;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.IAngularController;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiBody.PuiBody")
public class PuiBody extends HtmlBody implements IAngularController {
	enum propertyKeys {
		publishAs, selector
	}

	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiBody.PuiBody");

	/**
	 * This method is not as superfluous as it seems. We need it to be able to
	 * call getStateHelper() in defender methods.
	 */
	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

}
