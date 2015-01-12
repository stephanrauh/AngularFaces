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
