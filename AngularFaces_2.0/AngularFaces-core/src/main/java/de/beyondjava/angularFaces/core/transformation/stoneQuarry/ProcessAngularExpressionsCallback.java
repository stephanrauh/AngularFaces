/**
 *  (C) 2013-2015 Stephan Rauh http://www.beyondjava.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.beyondjava.angularFaces.core.transformation.stoneQuarry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

/** Special treatment of ng-repeat statements containing JSF attributes. */
public class ProcessAngularExpressionsCallback implements VisitCallback {

	private final static Pattern angularExpression = Pattern.compile("\\{\\{([A-Z]|[a-z]|\\.)+\\}\\}");
	
	final static String identifier="([A-Z]|[a-z]|[0-9]|_|\\.)+";

	private final static Pattern ngRepeat = Pattern.compile("ng-repeat=\"" + identifier + "\\sin\\s(\\$)?" + identifier);

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		if (component.getClass().getName().endsWith(".UIInstructions")) {
			String html = component.toString();
			addAngularExpressionToJSFAttributeList(html, false);
		} else {
			boolean onlyOnce=false;
			String once = AttributeUtilities.getAttributeAsString(component, "once");
			if (null != once && "true".equalsIgnoreCase(once)) {
				onlyOnce = true;
			}
				String cacheable = AttributeUtilities.getAttributeAsString(component, "cacheable");
				boolean isCacheable= ("true".equalsIgnoreCase(cacheable));
				for (String key : JSFAttributes.jsfAttributes) {
					extractAttribute(component, key, isCacheable);
				}
			}

		return VisitResult.ACCEPT;
	}

	private void extractAttribute(UIComponent component, String key, boolean isCacheable) {
		Object value = AttributeUtilities.getAttribute(component,key);
		if (value != null) {
			if (value instanceof String) {
				String vs = (String) value;
				addAngularExpressionToJSFAttributeList(vs, isCacheable);
			}
		}
	}

	private static void addAngularExpressionToJSFAttributeList(String html, boolean cacheable) {
		Matcher matcher = angularExpression.matcher(html);
		while (matcher.find()) {
			String exp = matcher.group();
			PuiModelSync.addJSFAttrbitute(exp.substring(2, exp.length() - 2), null, cacheable, false);
		}
		
		matcher = ngRepeat.matcher(html);
		while (matcher.find()) {
			String exp = matcher.group();
			int index = exp.indexOf(" in ");
			if (index>=0)
			{
				String var = exp.substring(index + 4).trim();
				if (var.startsWith("$")) var=var.substring(1);
				PuiModelSync.addJSFAttrbitute(var, null, cacheable, false);
			}
		}
	}

}
