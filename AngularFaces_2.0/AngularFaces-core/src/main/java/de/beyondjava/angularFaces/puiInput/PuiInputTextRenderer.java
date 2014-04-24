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
package de.beyondjava.angularFaces.puiInput;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.beyondjava.angularFaces.core.*;

@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.angularFaces.puiInput.PuiInput")
public class PuiInputTextRenderer extends HtmlBasicInputRenderer implements RendererUtils, NGModelUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiInput.PuiInputTextRenderer");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiInput' is available for use.");
    }

    /**
     *
     */
    public PuiInputTextRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    /**
     * Generates the HTML code.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        PuiInput input = (PuiInput) component;
        StringBuffer html = new StringBuffer();
        html.append("<pui-input ");
        renderNonEmptyAttribute(html, "label", input.getLabel());
        final String ngModel = getNGModel(input, html);
        renderNonEmptyAttribute(html, "ng-model", String.valueOf(ngModel));

        NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(input);
        Object value = ELTools.evalAsObject("#{" + infos.getCoreExpression() + "}");
        renderNonEmptyAttribute(html, "value", value == null ? null : String.valueOf(value));
        if (infos.isHasMin()) {
            renderNonEmptyAttribute(html, "min", String.valueOf(infos.getMin()));
        }
        if (infos.isHasMax()) {
            renderNonEmptyAttribute(html, "max", String.valueOf(infos.getMax()));
        }
        if (infos.isHasMinSize()) {
            renderNonEmptyAttribute(html, "minlength", String.valueOf(infos.getMinSize()));
        }
        if (infos.isHasMaxSize()) {
            renderNonEmptyAttribute(html, "maxlength", String.valueOf(infos.getMaxSize()));
        }
        if (infos.isRequired()) {
            renderNonEmptyAttribute(html, "required", "true");
        }
        if (infos.isNumeric()) {
            html.append("type='number' ");
        }
        html.append(">");
        html.append("</pui-input>");

        responseWriter.append(html.toString());
        responseWriter.append(html.toString().replace("<", "&lt;").replace(">", "&gt;"));
        responseWriter.append("<br />");
    }

}
