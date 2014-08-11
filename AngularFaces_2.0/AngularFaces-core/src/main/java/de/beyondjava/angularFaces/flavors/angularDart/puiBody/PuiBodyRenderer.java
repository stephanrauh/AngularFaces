package de.beyondjava.angularFaces.flavors.angularDart.puiBody;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;

import de.beyondjava.angularFaces.common.IAngularController;
import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
// ToDo @ResourceDependencies({ @ResourceDependency(library = "angularPrimeDart", name = "packages/browser/dart.js",
// target = "body") })
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiBody.PuiBody")
public class PuiBodyRenderer extends BodyRenderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiBodyRenderer");

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String controller = ((IAngularController) component).getSelector();
        String publishAs = ((IAngularController) component).getPublishAs();
        if ((null == controller) ^ (null == publishAs)) {
            if (null == controller) {
                controller = "controllerBean";
                LOGGER.warning("PuiBody: Missing attribute 'controller'. Using 'controllerBean' as default.");
            }
            else {
                publishAs = "ctrl";
                LOGGER.warning("PuiBody: Missing attribute 'publishAs'. Using 'publishAs' as default.");
            }
        }
        renderNonEmptyAttribute(writer, controller, controller);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String main = (String) component.getAttributes().get("mainclassfile");
        if (main == null) {
            main = "main.dart";
        }
        if (!main.endsWith(".dart")) {
            main = main + ".dart";
        }
        writer.append("<script type='application/dart' src='" + main + "'></script>");

        int index = main.lastIndexOf("/");
        String mainFolder = "";
        if (index >= 0) {
            mainFolder = main.substring(0, index + 1);
        }
        writer.append("<script type='text/javascript' src='" + mainFolder + "packages/browser/dart.js'></script>");
        super.encodeEnd(context, component);
    }

};
