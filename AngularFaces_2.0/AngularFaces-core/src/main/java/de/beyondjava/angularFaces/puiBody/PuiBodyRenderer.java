package de.beyondjava.angularFaces.puiBody;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;

// ToDo @ResourceDependencies({ @ResourceDependency(library = "angularPrimeDart", name = "packages/browser/dart.js", target = "body") })
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiBody.PuiBody")
public class PuiBodyRenderer extends BodyRenderer {
	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.angularFaces.puiButton.PuiBodyRenderer");

	static {
		LOGGER.info("AngularFaces renderer of 'PuiBody' is available for use.");
	}

	public PuiBodyRenderer() {
		LOGGER.info(getClass().getName() + " is being initialized");
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.append("<script type='application/dart' src='main.dart'></script>");
		writer.append("<script type='text/javascript' src='packages/browser/dart.js'></script>");
		super.encodeEnd(context, component);
	}

}
