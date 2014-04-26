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
package de.beyondjava.angularFaces.puiButton;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * &ltpui-button&gt; is a command button that can call Dart code (or Javascript code, albeit JS code can't interoperate
 * with Dart). &ltpui-button&gt; can contain a caption and an image that can be put in front of or behind the caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiButton.PuiButton")
public class PuiButtonRenderer extends HtmlBasicInputRenderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiButtonRenderer");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiButton' is available for use.");
    }

    public PuiButtonRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    /**
     * Generates the HTML code.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        PuiButton button = (PuiButton) component;
        writer.startElement("pui-button", component);
        String actionListener = button.getActionListener();
        if ((null != actionListener) && (!actionListener.contains("("))) {
            actionListener += "()";
        }
        renderNonEmptyAttribute(writer, "actionListener", actionListener);
        renderNonEmptyAttribute(writer, "disabled", button.getDisabled());
        renderNonEmptyAttribute(writer, "icon", button.getIcon());
        renderNonEmptyAttribute(writer, "iconPos", button.getIconPos());
        renderNonEmptyAttribute(writer, "value", button.getValue());
        writer.endElement("pui-button");
    }
}
