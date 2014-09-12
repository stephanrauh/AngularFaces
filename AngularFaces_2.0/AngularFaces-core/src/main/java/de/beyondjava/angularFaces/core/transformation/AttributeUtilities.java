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

import javax.faces.component.UIComponent;

/** Finds attributes, no matter whether they are regular or pass-through attributes. */
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
