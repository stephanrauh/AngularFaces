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
package de.beyondjava.angularFaces.flavors.kendo.puiTabview;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

/**
 * A &lt;pui-tabview&gt; consists of a number of &lt;pui-tabs&gt;, each
 * containing content that's hidden of shown depending on whether the tab is
 * active or not. Only one tab can be active at a time.
 *
 * The programming model is much like the API of the PrimeFaces &lt;tabView&gt;
 * component.
 *
 * Usage:<br />
 * &lt;pui-tabview&gt; <br />
 * &lt;pui-tab title="first tab"&gt;<br />
 * content of first tab<br />
 * &lt;/pui-tab&gt; <br />
 * &lt;pui-tab title="default tab" selected="true"&gt;<br />
 * content of second tab <br />
 * &lt;/pui-tab&gt;<br />
 * &lt;pui-tab title="closable tab" closeable="true"&gt; <br />
 * content of closeable tab<br />
 * &lt;/pui-tab&gt;<br />
 * &lt;/pui-tabview&gt;
 *
 * Kudos: This component's development was helped a lot by a stackoverflow
 * answer:
 * http://stackoverflow.com/questions/20531349/struggling-to-implement-tabs
 * -in-angulardart.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiTabview.PuiTabView")
public class PuiTabViewRenderer extends Renderer {
	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiTabview.PuiTabView");

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		// TODO Auto-generated method stub
		// super.encodeBegin(context, component);
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
			throws IOException {
		// TODO Auto-generated method stub
		// super.encodeChildren(context, component);
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("kendo-tab-strip", "", "kendo-tab-strip");
		writer.writeAttribute("k-content-urls", "[ null, null]",
				"k-content-urls");
		writer.startElement("ul", component);
		for (UIComponent tab : component.getChildren()) {
			if (tab instanceof PuiTab) {
				String title = ((PuiTab) tab).getTitle();
				writer.startElement("li", tab);
				String isSelected = ((PuiTab) tab).getSelected();
				if ("true".equalsIgnoreCase(isSelected)) {
					writer.writeAttribute("class", "k-state-active", "class");
				}

				writer.append(title);
				writer.endElement("li");
			}
		}
		writer.endElement("ul");
		for (UIComponent tab : component.getChildren()) {
			if (tab instanceof PuiTab) {
				writer.startElement("div", tab);
				writer.writeAttribute("style", "padding: 1em", "style");
				tab.encodeChildren(context);
				writer.endElement("div");
			}
		}
		writer.endElement("div");
	}

	/**
	 * <p>
	 * Return a flag indicating whether this {@link Renderer} is responsible for
	 * rendering the children the component it is asked to render. The default
	 * implementation returns <code>false</code>.
	 * </p>
	 */

	public boolean getRendersChildren() {
		return true;
	}
};
