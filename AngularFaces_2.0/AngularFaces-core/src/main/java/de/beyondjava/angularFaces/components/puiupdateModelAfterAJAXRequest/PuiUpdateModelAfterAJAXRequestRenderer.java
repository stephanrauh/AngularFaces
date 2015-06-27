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
package de.beyondjava.angularFaces.components.puiupdateModelAfterAJAXRequest;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

/** This generates code to update the AngularJS model after an AJAX request. */
@FacesRenderer(componentFamily = "de.beyondjava", rendererType = "de.beyondjava.puiUpdateModelAfterAJAXRequests")
public class PuiUpdateModelAfterAJAXRequestRenderer extends Renderer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.puiupdateModelAfterAJAXRequest.puiUpdateModelAfterAJAXRequestRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (FacesContext.getCurrentInstance().isPostback()) {
			ResponseWriter writer = context.getResponseWriter();
			writer.writeText("\n", null);
			writer.startElement("script", component);
			writer.write("if (window.jsfScope) {\n");
			List<String> beansAsJSon = PuiModelSync.getFacesModel();
			for (String bean : beansAsJSon) {
				writer.write("puiUpdateModel(" + bean + ");");
			}
			writer.writeText("\n}", null);
			writer.endElement("script");
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	}
}
