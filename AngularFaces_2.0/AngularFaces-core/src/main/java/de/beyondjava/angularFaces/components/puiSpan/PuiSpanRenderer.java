/**
 *  (C) 2013-2015 Stephan Rauh http://www.beyondjava.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.beyondjava.angularFaces.components.puiSpan;

import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@FacesRenderer(componentFamily = "de.beyondjava", rendererType = "de.beyondjava.puiSpan")
public class PuiSpanRenderer extends Renderer implements Serializable {
	private static final long serialVersionUID = 1L;

//	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.puiSpan.PuiSpanRenderer");

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
