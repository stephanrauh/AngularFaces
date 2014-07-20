package de.beyondjava.kendoFaces.puiAccordion;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiAccordion.puiAccordion")
public class PuiAccordionRenderer extends Renderer implements RendererUtils {
	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiAccordion.puiAccordion");

	static {
		LOGGER.info("AngularFaces renderer of 'PuiAccordion' is available for use.");
	}

	public PuiAccordionRenderer() {
		LOGGER.info(getClass().getName() + " is being initialized");
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("ul", component);
		writer.writeAttribute("kendo-panel-bar", "", null);
		renderMostCommonAttributes(writer, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("ul");
	}
};
