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
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

/** This generates a simple HTML span. */
@FacesComponent("de.beyondjava.puiSpan")
public class PuiSpan extends HtmlOutputText implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String COMPONENT_FAMILY = "de.beyondjava";

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("span", this);
		String keys = (String) getAttributes().get("attributeNames");
		if (null != keys) {
			String[] keyArray = keys.split(",");
			for (String key:keyArray) {
				writer.writeAttribute(key, AttributeUtilities.getAttributeAsString(this, key), key);				
			}
		}
	}
	
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("span");
	}
}
