package de.beyondjava.angularFaces.core.transformation;

import java.util.Map.Entry;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGWordUtiltites;

public class AddNGModelAndIDCallback implements VisitCallback {

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
//					vex=component.getValueExpression("value");
//					Set<Entry<String, Object>> entrySet = component.getAttributes().keySet();
//					component.getAttributes().clear();
				}
			}
		}
		if (null != vex) {
			String vexAsString = vex.getExpressionString();
			String coreValue = vexAsString.substring(2, vexAsString.length() - 1);
			component.getPassThroughAttributes().put("ng-model", coreValue);
//			if (null != component.getId()) {
//				component.setId(NGWordUtiltites.lastTerm(coreValue));
//			}
			PuiModelSync.addJSFAttrbitute(coreValue, component);
		}
		return VisitResult.ACCEPT;
	}

}
