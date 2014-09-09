package de.beyondjava.angularFaces.core.transformation;

import javax.faces.component.UIComponent;

public class AttributeUtilities {
	/** Apache MyFaces make HMTL attributes of HTML elements pass-through-attributes.
	 * This method finds the attribute, no matter whether it is stored as an
	 * ordinary or as an pass-through-attribute.
	 */
	public static Object getAttribute(UIComponent component, String attributeName) {
		Object value = component.getAttributes().get(attributeName);
		if (null==value) {
			value = component.getPassThroughAttributes().get(attributeName);
		}
		return value;
	}
}
