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
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import de.beyondjava.angularFaces.common.IModel;
import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;

public interface RendererUtils {
	/**
	 * Checks whether an attribute is empty, and adds it to the HTML code if
	 * it's not.
	 * 
	 * @param writer
	 * @param attributeValue
	 * @param input
	 * @param attibuteName
	 *
	 * @throws IOException
	 */
	default public void renderNonEmptyAttribute(ResponseWriter writer,
			final String attributeName, final Object attributeValue) {
		if (attributeValue != null) {
			try {
				writer.writeAttribute(attributeName,
						String.valueOf(attributeValue), attributeName);
			} catch (IOException exception) {
				System.out.println("TODO: an IOException has been thrown"
						+ exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	default public void renderMostCommonAttributes(ResponseWriter writer,
			UIComponent component) {
		try {
			if (component instanceof IStyle) {
				String s = ((IStyle) component).getStyle();
				if (null != s && s.length() > 0) {
					writer.writeAttribute("style", s, "style");
				}
			}
			if (component instanceof IStyleClass) {
				String s = ((IStyleClass) component).getStyleClass();
				if (null != s && s.length() > 0) {
					writer.writeAttribute("class", s, "class");
				}
			}
			if (component instanceof IModel) {
				String s = ((IModel) component).getNgModel();
				if (null != s && s.length() > 0) {
					writer.writeAttribute("ng-model", s, "ng-model");
				} else {
					String coreValueExpression = ELTools
							.getCoreValueExpression(component);
					if (null != coreValueExpression
							&& coreValueExpression.length() > 0) {
						int pos = coreValueExpression.lastIndexOf('.');
						if (pos >= 0)
							s = coreValueExpression.substring(pos + 1);
						else
							s = coreValueExpression;
						writer.writeAttribute("ng-model", s, "ng-model");
					}
				}

			}
		} catch (IOException e) {
			System.out.println("TODO: an IOException has been thrown "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

};
