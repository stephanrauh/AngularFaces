package de.beyondjava.angularFaces.core.transformation;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

public class AddNGModelAndIDCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		String attributeList = (String) component.getAttributes().get("angularfacesattributes");
		if (null != attributeList && attributeList.length() > 0) {
			String[] attributes = attributeList.split(",");
			for (String angularExpression : attributes) {
				PuiModelSync.addJSFAttrbitute(angularExpression.substring(2, angularExpression.length() - 2), component);
			}
		}
		return VisitResult.ACCEPT;
	}

}
