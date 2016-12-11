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

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/** Collects the attributes that have to be synchronized with the client. */
public class AddNGModelAndIDCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		String attributeList = (String) component.getAttributes().get("angularfacesattributes");
		if (null != attributeList && attributeList.length() > 0) {
			boolean onlyOnce = false;
			String once = AttributeUtilities.getAttributeAsString(component, "once");
			if (null != once && "true".equalsIgnoreCase(once)) {
				onlyOnce = true;
			}
			String cacheable = AttributeUtilities.getAttributeAsString(component, "cacheable");
			boolean isCacheable = ("true".equalsIgnoreCase(cacheable));
			String[] attributes = attributeList.split(",");
			for (String angularExpression : attributes) {
				PuiModelSync.addJSFAttrbitute(angularExpression.substring(2, angularExpression.length() - 2), component, isCacheable,
						onlyOnce);
			}
		}
		return VisitResult.ACCEPT;
	}

}
