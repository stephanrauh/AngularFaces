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
package de.beyondjava.angularFaces.components.puiActivateAngularFaces;

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
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

/** This generates code to reactive AngualarFaces after an AJAX request. Possibly duplicate to PuiSyncRenderer. */
@FacesRenderer(componentFamily = "de.beyondjava", rendererType = "de.beyondjava.puiActivateAngularFaces")
public class PuiActivateAngularFacesRenderer extends Renderer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.puiActivateAngularFaces.puiActivateAngularFacesRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (FacesContext.getCurrentInstance().isPostback()) {
			ResponseWriter writer = context.getResponseWriter();
			writer.writeText("\n", null);
			writer.startElement("script", component);
//			writer.writeText("window.jsfScope=null;", null);
//			writer.writeText("function initJSFScope($scope){", null);
//			writer.writeText("window.jsfScope=$scope;", null);
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
