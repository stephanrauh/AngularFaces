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
package de.beyondjava.angularFaces.flavors.angularDart.puiInput;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.beyondjava.angularFaces.core.JSR303RendererUtils;
import de.beyondjava.angularFaces.core.NGModelRendererUtils;
import de.beyondjava.angularFaces.core.RendererUtils;

@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.angularFaces.puiInput.PuiInput")
public class PuiInputTextRenderer extends HtmlBasicInputRenderer implements RendererUtils, NGModelRendererUtils,
JSR303RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiInput.PuiInputTextRenderer");

    /**
     * Generates the HTML code.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        PuiInput input = (PuiInput) component;
        writer.startElement("pui-input", component);
        renderNonEmptyAttribute(writer, "label", input.getLabel());
        renderNGModel(input, writer);
        renderJSR303Constraints(writer, input);
        writer.endElement("pui-input");
    }

}
