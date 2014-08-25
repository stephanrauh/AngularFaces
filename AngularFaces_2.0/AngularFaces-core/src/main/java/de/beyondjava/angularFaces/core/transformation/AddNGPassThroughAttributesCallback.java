package de.beyondjava.angularFaces.core.transformation;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGWordUtiltites;

/** See to it that standard AngularJS attributes are written to the HTML code as pass-through attributes */
public class AddNGPassThroughAttributesCallback implements VisitCallback {

	String[] angularAttributes = { "ngApp", "ngBind", "ngBindHtml", "ngBindTemplate", "ngBlur", "ngChange", "ngChecked", "ngClass",
			"ngClassEven", "ngClassOdd", "ngClick", "ngCloak", "ngController", "ngCopy", "ngCsp", "ngCut", "ngDblclick", "ngDisabled",
			"ngFocus", "ngForm", "ngHide", "ngHref", "ngIf", "ngInclude", "ngInit", "ngKeydown", "ngKeypress", "ngKeyup", "ngList",
			"ngModel", "ngModelOptions", "ngMousedown", "ngMouseenter", "ngMouseleave", "ngMousemove", "ngMouseover", "ngMouseup",
			"ngNonBindable", "ngOpen", "ngPaste", "ngPluralize", "ngReadonly", "ngRepeat", "ngSelected", "ngShow", "ngSrc", "ngSrcset",
			"ngStyle", "ngSubmit", "ngSwitch", "ngTransclude", "ngValue" };

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		for (String ngattribute: angularAttributes) {
			String value = (String) component.getAttributes().get(ngattribute);
			if (null != value) {
				component.getPassThroughAttributes().put(ngattribute, value);
				if (value.startsWith("{{") && value.endsWith("}}")) {
					PuiModelSync.addJSFAttrbitute(value, component);
					
				}
			}
			else {
				ValueExpression vex = component.getValueExpression(ngattribute);
				if (null != vex) {
					String vexAsString = vex.getExpressionString();
					String coreValue = vexAsString.substring(2, vexAsString.length() - 1);
					component.getPassThroughAttributes().put(ngattribute, coreValue);
					PuiModelSync.addJSFAttrbitute(coreValue, component);
				}

			}
			// TODO: ng-attributes can be JSF value expressions or AngularJS mustaches representing a JSF bean
			// addAttributeToListOfSynchedAttributes(component, attribute);
		}
		return VisitResult.ACCEPT;
	}
}
