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

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/** Converts HTML-like option tags to f:selectItem. */
public class PuiSelectItemTagHandler implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		if (component instanceof UISelectItem) {
			UISelectItem option = (UISelectItem) component;
			if ("true".equals(option.getAttributes().get("isoption"))) { // don't override traditional values
				if (option.getChildCount() > 0) {
					System.out.println(option.getChildCount());
					UIComponent label = option.getChildren().get(0);
					if (label.getClass().getName().contains("UIInstructions")) {
						String itemLabel = label.toString();
						option.setItemLabel(itemLabel);
					}
					Object o = option.getValue();
					if (o instanceof String) {
						option.setItemValue(o);
					}
					option.getChildren().clear();
				}
			}
		}
		return VisitResult.ACCEPT;
	}

}
