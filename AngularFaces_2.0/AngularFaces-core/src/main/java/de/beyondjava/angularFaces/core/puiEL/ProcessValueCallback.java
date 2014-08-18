package de.beyondjava.angularFaces.core.puiEL;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

public class ProcessValueCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		ValueExpression vex = component.getValueExpression("value");
		if (null != vex) {
			String vexAsString = vex.getExpressionString();
			String coreValue = vexAsString.substring(2, vexAsString.length() - 1);
			component.getPassThroughAttributes().put("ng-model", coreValue);
			PuiModelSync.addJSFAttrbitute(coreValue, component);
		}
		return VisitResult.ACCEPT;
	}

}
