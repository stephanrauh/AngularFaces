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

	String[] angularAttributes = { "ng-app", "ng-bind", "ng-bindhtml", "ng-bindtemplate", "ng-blur", "ng-chang-e", "ng-checked", "ng-class",
			"ng-classeven", "ng-classodd", "ng-click", "ng-cloak", "ng-controller", "ng-copy", "ng-csp", "ng-cut", "ng-dblclick", "ng-disabled",
			"ng-focus", "ng-form", "ng-hide", "ng-href", "ng-if", "ng-include", "ng-init", "ng-keydown", "ng-keypress", "ng-keyup", "ng-list",
			"ng-model", "ng-modeloptions", "ng-mousedown", "ng-mouseenter", "ng-mouseleave", "ng-mousemove", "ng-mouseover", "ng-mouseup",
			"ng-nonbindable", "ng-open", "ng-paste", "ng-pluralize", "ng-readonly", "ng-repeat", "ng-selected", "ng-show", "ng-src", "ng-srcset",
			"ng-style", "ng-submit", "ng-switch", "ng-transclude", "ng-value" };

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		for (String ngattribute: angularAttributes) {
			String value = (String) component.getAttributes().get(ngattribute);
			if (null != value) {
				component.getPassThroughAttributes().put(ngattribute, value);
				if (value.startsWith("{{") && value.endsWith("}}")) {
					PuiModelSync.addJSFAttrbitute(value.substring(2, value.length()-2), component);
					
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
