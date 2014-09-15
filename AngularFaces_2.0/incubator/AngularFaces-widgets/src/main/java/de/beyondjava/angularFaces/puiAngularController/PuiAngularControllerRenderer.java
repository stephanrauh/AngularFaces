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
package de.beyondjava.angularFaces.puiAngularController;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

import de.beyondjava.angularFaces.common.IAngularController;
import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * PuiAngularController is an HtmlBody that activates the AngularDart framework.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiAngularController.PuiAngularController")
public class PuiAngularControllerRenderer extends Renderer implements RendererUtils {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.angularFaces.puiButton.PuiAngularControllerRenderer");

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String controller = ((IAngularController) component).getSelector();
        if (null == controller) {
            controller = "controllerBean";
            LOGGER.warning("PuiAngularController: Missing attribute selector. I'm using controllerBean as default selector name.");
        }
        writer.startElement("div", component);
        renderNonEmptyAttribute(writer, controller, controller);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }
};
