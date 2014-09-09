package de.beyondjava.angularFaces.core.transformation;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.ELTools;

public class AddNGModelAndIDCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		ValueExpression vex = component.getValueExpression("value");
		if (null == vex) {
			String angularExpression = null;
			Object valueAsObject = AttributeUtilities.getAttribute(component,"value");
			if (valueAsObject instanceof String) {
				angularExpression = (String) valueAsObject;
			}
			if (null == angularExpression)
				angularExpression = (String) AttributeUtilities.getAttribute(component,"ngvalue");
			if (null != angularExpression) {
				if (angularExpression.startsWith("{{") && angularExpression.endsWith("}}")) {
					// This version is a hack! It works, but even so.
					String jsfExpression = "#{" + angularExpression.substring(2, angularExpression.length() - 2) + "}";
					Class<?> type = ELTools.getType(jsfExpression);
					vex = ELTools.createValueExpression(jsfExpression, type);
					component.getAttributes().replace("value", vex.getValue(FacesContext.getCurrentInstance().getELContext()));
					component.setValueExpression("value", vex);
					// vex=component.getValueExpression("value");
					// Set<Entry<String, Object>> entrySet = component.getAttributes().keySet();
					// component.getAttributes().clear();
				}
			}
		}
		if (null != vex) {
			String vexAsString = vex.getExpressionString();
			String coreValue = vexAsString.substring(2, vexAsString.length() - 1);
			component.getPassThroughAttributes().put("ng-model", coreValue);
			// if (null != component.getId()) {
			// component.setId(NGWordUtiltites.lastTerm(coreValue));
			// }
			PuiModelSync.addJSFAttrbitute(coreValue, component);
		}
		return VisitResult.ACCEPT;
	}

}
