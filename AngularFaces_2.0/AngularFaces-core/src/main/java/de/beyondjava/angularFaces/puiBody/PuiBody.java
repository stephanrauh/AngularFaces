package de.beyondjava.angularFaces.puiBody;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

@FacesComponent("de.beyondjava.angularFaces.puiBody.PuiBody")
public class PuiBody extends HtmlBody {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiBody.PuiBody");

    static {
        LOGGER.info("AngularFaces component 'PuiBody' is available for use.");
    }

    /**
     *
     */
    public PuiBody() {
        LOGGER.info(getClass().getName() + " is initialized");
        // setRendererType("de.beyondjava.angularFaces.puiButton.PuiButtonRenderer");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

}
