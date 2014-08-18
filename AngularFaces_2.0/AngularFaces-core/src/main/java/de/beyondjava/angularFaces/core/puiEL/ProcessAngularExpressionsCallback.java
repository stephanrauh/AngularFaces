package de.beyondjava.angularFaces.core.puiEL;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

public class ProcessAngularExpressionsCallback implements VisitCallback {
	static String[] properties = { "label", "header", "style", "styleClass", "title" };

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		for (String key : properties) {
			Object value = component.getAttributes().get(key);
			if (value != null) {
				if (value instanceof String) {
					String vs = (String) value;
					processAngularExpression(component, key, value, vs);
				}
			}
		}

		return VisitResult.ACCEPT;
	}
	private static void processAngularExpression(UIComponent component, String key, Object value, String vs) {
		if (vs.contains("{{")) {
			PuiModelSync.addJSFAttrbitute(vs.substring("{{".length(), vs.length() - 2), null);
		}
	}

}
