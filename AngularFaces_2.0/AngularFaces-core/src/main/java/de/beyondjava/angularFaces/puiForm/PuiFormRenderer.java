package de.beyondjava.angularFaces.puiForm;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.FormRenderer;

import de.beyondjava.angularFaces.common.IAngularController;

/**
 * PuiForm is a form with an AngularDart controller.
 */
@FacesRenderer(componentFamily = "javax.faces.Form", rendererType = "de.beyondjava.angularFaces.puiForm.PuiForm")
public class PuiFormRenderer extends FormRenderer {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiFormRenderer");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiForm' is available for use.");
    }

    public PuiFormRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String controller = ((IAngularController) component).getSelector();
        if (null == controller) {
            controller = "controllerBean";
            LOGGER.warning("PuiForm: Missing attribute selector. I'm using controllerBean as default selector name.");
        }
        writer.writeAttribute(controller, controller, null);

    }
};
