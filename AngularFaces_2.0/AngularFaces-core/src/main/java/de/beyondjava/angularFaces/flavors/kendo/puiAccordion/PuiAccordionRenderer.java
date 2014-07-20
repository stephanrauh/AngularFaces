package de.beyondjava.angularFaces.flavors.kendo.puiAccordion;

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
		writer.writeAttribute("ng-controller", component.getClientId()
				+ "AccordionCtrl", "ng-controller");
		writer.writeAttribute("kendo-panel-bar", "", null);
		String options = (String) component.getAttributes().get("k-options");
		String expandMode = (String) component.getAttributes()
				.get("expandMode");
		if (options != null && expandMode != null) {
			throw new IllegalArgumentException(
					"You can't provide k-options and expandMode at the same time.");
		}

		if (options == null) {
			options = "panelBarOptions";
		}
		renderNonEmptyAttribute(writer, "k-options", options);
		renderMostCommonAttributes(writer, component);

	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("ul");

		String expandMode = (String) component.getAttributes()
				.get("expandMode");
		String options = (String) component.getAttributes().get("k-options");
		if (options == null && expandMode == null)
			expandMode = "single";
		if (null != expandMode && expandMode.length() > 0) {
			String s = "<script>function "
					+ component.getClientId()
					+ "AccordionCtrl($scope) { $scope.panelBarOptions ={ expandMode : '"
					+ expandMode + "' };}</script>";
			writer.write(s);
		}

	}
}
