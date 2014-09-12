/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("ul", component);
		writer.writeAttribute("ng-controller", component.getClientId().replace(':', '_')
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
					+ component.getClientId().replace(':', '_')
					+ "AccordionCtrl($scope) { $scope.panelBarOptions ={ expandMode : '"
					+ expandMode + "' };}</script>";
			writer.write(s);
		}

	}
}
