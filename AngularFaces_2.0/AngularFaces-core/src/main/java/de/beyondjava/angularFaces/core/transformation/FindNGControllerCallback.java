package de.beyondjava.angularFaces.core.transformation;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

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

		String labels = (String) AttributeUtilities.getAttribute(component, "addlabels");
		if (null != labels) {
			addLabels=labels.equalsIgnoreCase("true");
		}
		String messages = (String) AttributeUtilities.getAttribute(component,"addmessages");
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
			return VisitResult.COMPLETE;
		}

		return VisitResult.ACCEPT;
	}

	public PuiModelSync getPuiModelSync() {
		return puiModelSync;
	}

}
