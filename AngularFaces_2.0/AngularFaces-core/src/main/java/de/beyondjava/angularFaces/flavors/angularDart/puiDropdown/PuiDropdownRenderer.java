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
package de.beyondjava.angularFaces.flavors.angularDart.puiDropdown;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.beyondjava.angularFaces.core.*;

@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.angularFaces.puiDropdown.PuiDropdown")
public class PuiDropdownRenderer extends HtmlBasicInputRenderer implements RendererUtils, NGModelRendererUtils,
JSR303RendererUtils {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.angularFaces.puiDropdown.PuiDropdownTextRenderer");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiDropdown' is available for use.");
    }

    /**
     *
     */
    public PuiDropdownRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    /**
     * Generates the HTML code.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        PuiDropdown input = (PuiDropdown) component;
        writer.startElement("pui-dropdown", component);
        renderNonEmptyAttribute(writer, "label", input.getLabel());
        renderNGModel(input, writer);
        renderJSR303Constraints(writer, input);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("pui-dropdown");
    }

}
