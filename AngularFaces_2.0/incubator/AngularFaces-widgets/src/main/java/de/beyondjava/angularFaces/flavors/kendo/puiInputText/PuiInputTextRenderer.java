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
package de.beyondjava.angularFaces.flavors.kendo.puiInputText;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import com.sun.faces.renderkit.html_basic.TextRenderer;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.JSR303RendererUtils;
import de.beyondjava.angularFaces.core.NGBeanAttributeInfo;
import de.beyondjava.angularFaces.core.RendererUtils;
import de.beyondjava.angularFaces.flavors.angularDart.puiInput.PuiInput;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.kendoFaces.puiInputText.PuiInputText")
public class PuiInputTextRenderer extends TextRenderer implements
		RendererUtils, JSR303RendererUtils {
	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiInputText.PuiInputText");


	protected String writeIdAttributeIfNecessary(FacesContext context,
			ResponseWriter writer, UIComponent component) {
		try {
			renderMostCommonAttributes(writer, component);
			renderJSR303Constraints(writer, component);
			String type = ((PuiInputText) component)
					.getTypeSpecificAttributes();
			if (null != type && type.length() > 0) {
				writer.writeAttribute(type, "", type);
			}
			else {
				NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(component);
				if (infos.isDate()) {
					writer.writeAttribute("kendo-date-picker", "", "kendo-date-picker");
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"TODO: An exception has been thrown." + e.getMessage(), e);
		}
		return super.writeIdAttributeIfNecessary(context, writer, component);
	}

};
