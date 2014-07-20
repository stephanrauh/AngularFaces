package de.beyondjava.kendoFaces.puiPanel;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

import de.beyondjava.angularFaces.core.RendererUtils;
import de.beyondjava.kendoFaces.puiAccordion.PuiAccordion;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiPanel.PuiPanel")
public class PuiPanelRenderer extends Renderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiPanel.PuiPanel");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiPanel' is available for use.");
    }

    public PuiPanelRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		if (!(component.getParent() instanceof PuiAccordion)) {
			writer.startElement("ul", component);
			writer.writeAttribute("kendo-panel-bar", "", null);
			renderMostCommonAttributes(writer, component);
		}
		writer.startElement("li", component);
		writer.append(((PuiPanel)component).getHeader());
		writer.startElement("div", component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeEnd(context, component);
		writer.endElement("div");
		writer.endElement("li");
		if (!(component.getParent() instanceof PuiAccordion)) {
			writer.endElement("ul");
		}
	}
};
