package de.beyondjava.angularFaces.puiBody;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
// ToDo @ResourceDependencies({ @ResourceDependency(library = "angularPrimeDart", name = "packages/browser/dart.js",
// target = "body") })
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiBody.PuiBody")
public class PuiBodyRenderer extends BodyRenderer {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiBodyRenderer");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiBody' is available for use.");
    }

    public PuiBodyRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        // TODO Auto-generated method stub
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String controller = (String) component.getAttributes().get("controller"); // TODO
        if (null == controller) {
            controller = "controllerBean";
        }
        writer.writeAttribute(controller, "", null);

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
