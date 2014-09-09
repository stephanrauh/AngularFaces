package de.beyondjava.angularFaces.core.transformation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

public class ProcessAngularExpressionsCallback implements VisitCallback {

	private final static Pattern angularExpression = Pattern.compile("\\{\\{([A-Z]|[a-z]|\\.)+\\}\\}");
	
	final static String identifier="([A-Z]|[a-z]|[0-9]|_|\\.)+";

	private final static Pattern ngRepeat = Pattern.compile("ng-repeat=\"" + identifier + "\\sin\\s(\\$)?" + identifier);

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		if (component.getClass().getName().endsWith(".UIInstructions")) {
			String html = component.toString();
			addAngularExpressionToJSFAttributeList(html);
		} else {
			for (String key : JSFAttributes.jsfAttributes) {
				extractAttribute(component, key);
			}
		}

		return VisitResult.ACCEPT;
	}

	private void extractAttribute(UIComponent component, String key) {
		Object value = AttributeUtilities.getAttribute(component,key);
		if (value != null) {
			if (value instanceof String) {
				String vs = (String) value;
				addAngularExpressionToJSFAttributeList(vs);
			}
		}
	}

	private static void addAngularExpressionToJSFAttributeList(String html) {
		Matcher matcher = angularExpression.matcher(html);
		while (matcher.find()) {
			String exp = matcher.group();
			PuiModelSync.addJSFAttrbitute(exp.substring(2, exp.length() - 2), null);
		}
		
		matcher = ngRepeat.matcher(html);
		while (matcher.find()) {
			String exp = matcher.group();
			int index = exp.indexOf(" in ");
			if (index>=0)
			{
				String var = exp.substring(index + 4).trim();
				if (var.startsWith("$")) var=var.substring(1);
				PuiModelSync.addJSFAttrbitute(var, null);
			}
		}
	}

}
