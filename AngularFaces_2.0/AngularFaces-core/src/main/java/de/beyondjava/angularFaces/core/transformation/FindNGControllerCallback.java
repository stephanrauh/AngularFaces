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

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/** Looks for the AngularJS application and controller tags. */
public class FindNGControllerCallback implements VisitCallback {
	
	private PuiModelSync puiModelSync=null;
	private boolean addLabels=true;
	public boolean isAddLabels() {
		return addLabels;
	}

	public boolean isAddMessages() {
		return addMessages;
	}

	private boolean addMessages=true;

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent source) {
		if (!(source instanceof UIComponent))
			return VisitResult.ACCEPT;
		UIComponent component = (UIComponent) source;

		String labels = (String) AttributeUtilities.getAttribute(component, "addLabels");
		if (null != labels) {
			addLabels=labels.equalsIgnoreCase("true");
		}
		String messages = (String) AttributeUtilities.getAttribute(component,"addMessages");
		if (null != messages) {
			addMessages=messages.equalsIgnoreCase("true");
		}

		String ngApp = (String) AttributeUtilities.getAttribute(component,"ng-app");
//		if (null != ngApp) {
//			component.getPassThroughAttributes().put("ng-app", ngApp);
//		}
		
		String ngController = (String) AttributeUtilities.getAttribute(component,"ng-controller");
		if (null != ngController) {
//			component.getPassThroughAttributes().put("ng-controller", ngController);
			List<UIComponent> children = component.getChildren();
			boolean needsToBeAdded = true;
//			int index = 0;
			for (UIComponent maybe : children) {
				if (maybe instanceof PuiModelSync) {
					needsToBeAdded = false;
					puiModelSync=(PuiModelSync) maybe;
				}
//				index++;
			}
			if (needsToBeAdded) {
				puiModelSync=new PuiModelSync();
				children.add(puiModelSync);
			}
			String angularJSFile= (String) AttributeUtilities.getAttributeAsString(component,"angularJSFile");
			if (null != angularJSFile) {
				puiModelSync.getAttributes().put("angularJSFile", angularJSFile);
			}

			return VisitResult.COMPLETE;
		}

		return VisitResult.ACCEPT;
	}

	public PuiModelSync getPuiModelSync() {
		return puiModelSync;
	}

}
