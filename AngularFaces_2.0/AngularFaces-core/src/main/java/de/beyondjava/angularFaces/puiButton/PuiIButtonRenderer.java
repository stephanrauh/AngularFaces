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

import de.beyondjava.angularFaces.common.*;

@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiButton.PuiButton")
public class PuiIButtonRenderer extends HtmlBasicInputRenderer {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiButtonRenderer");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiButton' is available for use.");
    }

    /**
     *
     */
    public PuiIButtonRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    /**
     * Generates the HTML code.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        PuiButton input = (PuiButton) component;
        StringBuffer html = new StringBuffer();
        html.append("<pui-button ");
        renderNonEmptyAttribute(html, "label", input.getLabel());
        renderNonEmptyAttribute(html, "value", ELTools.getCoreValueExpression(input));
        NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(input);
        if (infos.isHasMin()) {
            renderNonEmptyAttribute(html, "min", String.valueOf(infos.getMin()));
        }
        if (infos.isHasMax()) {
            renderNonEmptyAttribute(html, "max", String.valueOf(infos.getMin()));
        }
        if (infos.isHasMinSize()) {
            renderNonEmptyAttribute(html, "minlength", String.valueOf(infos.getMin()));
        }
        if (infos.isHasMaxSize()) {
            renderNonEmptyAttribute(html, "maxlength", String.valueOf(infos.getMin()));
        }
        if (infos.isRequired()) {
            renderNonEmptyAttribute(html, "required", "true");
        }
        html.append(">");
        html.append("</pui-button>");

        // responseWriter.append(html.toString());
        responseWriter.append(html.toString().replace("<", "&lt;").replace(">", "&gt;"));
        responseWriter.append("<br />");

    }

    /**
     * Checks whether an attibute is empty, and adds it to the HTML code if it's not.
     *
     * @param input
     * @param html
     * @param attibuteName
     * @param attributeValue
     */
    private void renderNonEmptyAttribute(StringBuffer html, final String attibuteName, final String attributeValue) {
        if (attributeValue != null) {
            html.append(attibuteName);
            html.append("='");
            html.append(attributeValue);
            html.append("' ");
        }
    }

}
