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
package de.beyondjava.angularFaces.flavors.kendo.puiGrid;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * &lt;pui-grid&gt; makes it a little easier to create simple but decently
 * looking input dialogs. Typically it contains a number of input fields that
 * are automatically aligned to each other. More precisely, &lt;pui-grid&gt;
 * creates a column consisting of three columns. The first column is the label
 * (which is automatically extracted from the component), the second is the
 * components itself and the third column is the field-specific error message.
 * Alternative, the error message is placed below its field. Likewise, the label
 * can be put above the input field.
 *
 * @ToDo &lt;pui-grid&gt; is limited to a single column of components
 * @ToDo put labels optionally above the component
 * @ToDo put error message optionally behind the component
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiGrid.PuiGrid")
public class PuiGridRenderer extends HtmlBasicInputRenderer implements
		RendererUtils {
	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiGrid.PuiGridRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		final ResponseWriter responseWriter = context.getResponseWriter();
		PuiGrid grid = (PuiGrid) component;
		int columns = grid.getColumns();
		if (columns<1) columns=1;
		responseWriter.startElement("table", component);

		List<UIComponent> children = component.getChildren();
		int column = 0;
		for (UIComponent c:children){
			try {
				if (0 == column) {
					responseWriter.startElement("tr", component);
				}
				responseWriter.startElement("td", component);
				int colspan=1;
				String scolspan = (String) c.getAttributes().get("colspan");
				if (null != scolspan) colspan=Integer.parseInt(scolspan);
				if (colspan>1) {
					responseWriter.writeAttribute("colspan", colspan, "colspan");
				}
				c.encodeAll(context);
				responseWriter.endElement("td");
				column += colspan;
				if (column>=columns) {
					columns=0;
					responseWriter.endElement("tr");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		if (columns>0) {
			responseWriter.endElement("tr");
		}

		responseWriter.endElement("table");

	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
			throws IOException {
	}
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
	}
}
