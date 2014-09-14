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

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

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
		if (null != ngApp) {
			component.getPassThroughAttributes().put("ng-app", ngApp);
		}
		
		String ngController = (String) AttributeUtilities.getAttribute(component,"ng-controller");
		if (null != ngController) {
			component.getPassThroughAttributes().put("ng-controller", ngController);
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
			String angularJSFile= (String) AttributeUtilities.getAttribute(component,"angularJSFile");
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
