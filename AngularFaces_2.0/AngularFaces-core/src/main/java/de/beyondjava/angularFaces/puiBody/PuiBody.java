package de.beyondjava.angularFaces.puiBody;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesComponent("de.beyondjava.angularFaces.puiBody.PuiBody")
public class PuiBody extends HtmlBody {
    enum propertyKeys {
        publishAs, selector
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiBody.PuiBody");

    static {
        LOGGER.info("AngularFaces component 'PuiBody' is available for use.");
    }

    public PuiBody() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

    public String getPublishAs() {
        return (String) getStateHelper().eval(propertyKeys.publishAs, null);
    }

    public String getSelector() {
        return (String) getStateHelper().eval(propertyKeys.selector, null);
    }

    public void setPublishAs(String publishAs) {
        getStateHelper().put(propertyKeys.publishAs, publishAs);
    }

    public void setSelector(String selector) {
        getStateHelper().put(propertyKeys.selector, selector);
    }

}
