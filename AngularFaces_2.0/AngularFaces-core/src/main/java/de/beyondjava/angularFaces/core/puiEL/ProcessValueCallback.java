package de.beyondjava.angularFaces.core.puiEL;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.ELTools;

public class ProcessValueCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		ValueExpression vex = component.getValueExpression("value");
		if (null == vex) {
			String s = (String) component.getAttributes().get("value");
			if (null != s) {
				if (s.startsWith("{{") && s.endsWith("}}")) {
					// This version is a hack! It works, but even so.
					String jsfExpression = "#{"+s.substring(2, s.length()-2)+"}";
					Class<?> type = ELTools.getType(jsfExpression);
					vex = ELTools.createValueExpression(jsfExpression, type);
					component.getAttributes().replace("value", vex.getValue(FacesContext.getCurrentInstance().getELContext()));
					component.setValueExpression("value", vex);
					vex=component.getValueExpression("value");
				}
			}
		}
		if (null != vex) {
			String vexAsString = vex.getExpressionString();
			String coreValue = vexAsString.substring(2, vexAsString.length() - 1);
			component.getPassThroughAttributes().put("ng-model", coreValue);
			PuiModelSync.addJSFAttrbitute(coreValue, component);
		}
		return VisitResult.ACCEPT;
	}

}
