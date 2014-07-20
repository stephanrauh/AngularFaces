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
package de.beyondjava.angularFaces.core;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

public interface JSR303RendererUtils extends RendererUtils {

    /**
     * Extracts information from the server side and uses as much as possible on the client.
     *
     * @param writer
     * @param input
     * @throws IOException
     */
    public default void renderJSR303Constraints(ResponseWriter writer, UIComponent input)  {
        NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(input);
        Object value = ELTools.evalAsObject("#{" + infos.getCoreExpression() + "}");
        renderNonEmptyAttribute(writer, "value", value == null ? null : String.valueOf(value));
        if (infos.isHasMin()) {
            renderNonEmptyAttribute(writer, "min", String.valueOf(infos.getMin()));
        }
        if (infos.isHasMax()) {
            renderNonEmptyAttribute(writer, "max", String.valueOf(infos.getMax()));
        }
        if (infos.isHasMinSize()) {
            renderNonEmptyAttribute(writer, "minlength", String.valueOf(infos.getMinSize()));
        }
        if (infos.isHasMaxSize()) {
            renderNonEmptyAttribute(writer, "maxlength", String.valueOf(infos.getMaxSize()));
        }
        if (infos.isRequired()) {
            renderNonEmptyAttribute(writer, "required", "true");
        }
        if (infos.isNumeric()) {
        	try {
            writer.writeAttribute("type", "number", "type");
        	}
        	catch (Exception e) {
        		System.out.println("TODO: an IOException has been thrown." + e.getMessage());
        		e.printStackTrace();
        	}
        }
    }

}
