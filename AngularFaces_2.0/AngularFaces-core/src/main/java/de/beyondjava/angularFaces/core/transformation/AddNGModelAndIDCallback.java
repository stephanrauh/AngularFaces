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
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

/** Collects the attributes that have to be synchronized with the client. */
public class AddNGModelAndIDCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		String attributeList = (String) component.getAttributes().get("angularfacesattributes");
		if (null != attributeList && attributeList.length() > 0) {
			String cacheable = AttributeUtilities.getAttributeAsString(component, "cacheable");
			boolean isCacheable= ("true".equalsIgnoreCase(cacheable));
			String[] attributes = attributeList.split(",");
			for (String angularExpression : attributes) {
				PuiModelSync.addJSFAttrbitute(angularExpression.substring(2, angularExpression.length() - 2), component, isCacheable);
			}
		}
		return VisitResult.ACCEPT;
	}

}
