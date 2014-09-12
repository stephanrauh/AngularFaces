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
package de.beyondjava.angularFaces.flavors.kendo.puiSlider;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.kendoFaces.puiSlider.PuiSlider")
public class PuiSliderRenderer extends BodyRenderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiButton.PuiSliderRenderer");

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    	PuiSlider slider = (PuiSlider) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.append("\r\n");
        writer.startElement("span", component);

        writer.startElement("div", component);
        writer.writeAttribute("kendo-slider", "", null);
        renderMostCommonAttributes(writer, component);

        String orientation = (String) slider.getOrientation();
        if ((null != orientation) && (!orientation.equalsIgnoreCase("horizontal"))) {
            writer.writeAttribute("k-orientation", "'"+orientation+"'", null);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        writer.endElement("span");
        writer.append("\r\n");
    }
};
