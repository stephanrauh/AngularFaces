/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
