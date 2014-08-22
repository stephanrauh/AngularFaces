package de.beyondjava.angularFaces.core.transformation;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

public class FindNGControllerCallback implements VisitCallback {
	
	private PuiModelSync puiModelSync=null;

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent source) {
		if (!(source instanceof UIComponent))
			return VisitResult.ACCEPT;
		UIComponent component = (UIComponent) source;

		String ngApp = (String) component.getAttributes().get("ng-app");
		if (null != ngApp) {
			component.getPassThroughAttributes().put("ng-app", ngApp);
		}
		String ngController = (String) component.getAttributes().get("ng-controller");
		if (null != ngController) {
			component.getPassThroughAttributes().put("ng-controller", ngController);
			List<UIComponent> children = component.getParent().getChildren();
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
			PuiModelSync.initJSFAttributesTable();
			return VisitResult.COMPLETE;
		}

		return VisitResult.ACCEPT;
	}

	public PuiModelSync getPuiModelSync() {
		return puiModelSync;
	}

}
