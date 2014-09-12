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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.i18n.I18n;

public class TranslationCallback implements VisitCallback {
	I18n i18n = null;

	int duplicateLabels = 0;
	
	String[] attributesToBeTranslated={"header", "headerText"};

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		for (String attributeName: attributesToBeTranslated) {
			translateAttribute(component, attributeName);
		}
		if (component instanceof UICommand || component instanceof UIOutput) {
			translateAttribute(component, "value");
		} else if (component.getClass().getName().endsWith(".UIInstructions")) {
			// System.out.println("test");
		}

		return VisitResult.ACCEPT;
	}

	private void translateAttribute(UIComponent component, String attributeName) {
		Object value = AttributeUtilities.getAttribute(component,attributeName);
		if (null != value && value instanceof String) {
			String caption = (String) value;
			if (null != caption) {
				String translation = translate(caption);
				if (!caption.equals(translation)) {
					component.getAttributes().put(attributeName, translation);
				}
			}
		}
	}

	private String translate(String caption) {
		if (null == i18n)
			i18n = (I18n) ELTools.evalAsObject("#{i18n}");
		if (null == i18n)
			return caption;
		return i18n.translate(caption);
	}

}
