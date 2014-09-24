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
package de.beyondjava.angularFaces.core.transformation;

import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/** Finds attributes, no matter whether they are regular or pass-through attributes. */
public class AttributeUtilities {
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.core.transformation.AttributeUtilities");

	/**
	 * Apache MyFaces make HMTL attributes of HTML elements pass-through-attributes. This method finds the attribute, no matter whether it
	 * is stored as an ordinary or as an pass-through-attribute.
	 */
	public static Object getAttribute(UIComponent component, String attributeName) {
		Object value = component.getPassThroughAttributes().get(attributeName);
		if (null == value) {
			value = component.getAttributes().get(attributeName);
		}
		if (null==value) {
			if (!attributeName.equals(attributeName.toLowerCase())) {
				return getAttribute(component, attributeName.toLowerCase());
			}
		}
		return value;
	}

	/**
	 * Apache MyFaces sometimes returns a ValueExpression when you read an attribute. Mojarra does not, but requires a second call. The
	 * method treats both frameworks in a uniform way and evaluates the expression, if needed, returning a String value.
	 * 
	 */
	public static String getAttributeAsString(UIComponent component, String attributeName) {
		try {
		Object attribute = getAttribute(component, attributeName);
		if (null != attribute) {
			if (attribute instanceof ValueExpression) {
				return (String) ((ValueExpression) attribute).getValue(FacesContext.getCurrentInstance().getELContext());
			} else if (attribute instanceof String) {
				return (String) attribute;
			} else {
				LOGGER.severe("unexpected data type of label: " + attributeName + " type:" + attribute.getClass().getName());
				return "unexpected data type of label: " + attributeName + " type:" + attribute.getClass().getName();
			}
		}
		if (null == attribute) {
			ValueExpression vex = component.getValueExpression(attributeName);
			if (null != vex) {
				return (String) vex.getValue(FacesContext.getCurrentInstance().getELContext());
			}
		}
		}
		catch (ClassCastException error) {
			LOGGER.fine("Attribute is not a String: " + attributeName);
		}
		return null;
	}
}
