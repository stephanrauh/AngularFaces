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
package de.beyondjava.angularFaces.components.puiSpan;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import com.google.gson.Gson;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

@FacesRenderer(componentFamily = "de.beyondjava", rendererType = "de.beyondjava.puiSpan")
public class PuiSpanRenderer extends Renderer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.puiSpan.PuiSpanRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("span", component);
		String keys = (String) component.getAttributes().get("attributeNames");
		if (null != keys) {
			String[] keyArray = keys.split(",");
			for (String key:keyArray) {
				writer.writeAttribute(key, AttributeUtilities.getAttributeAsString(component, key), key);				
			}
		}
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("span");
	}
}
