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
package de.beyondjava.angularFaces.core.transformation;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.i18n.I18n;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/** Translate texts store in a component. */
public class TranslationCallback implements VisitCallback {

	boolean isAjaxRequest;

	public TranslationCallback(boolean isAjaxRequest) {
		this.isAjaxRequest = isAjaxRequest;
	}

	I18n i18n = null;

	int duplicateLabels = 0;

	String[] attributesToBeTranslated = { "header", "headerText", "addLabel", "addAllLabel", "cancelLabel", "closeTitle", "collapseTitle",
			"converterMessage", "emptyLabel", "expandTitle", "fileLimitMessage", "goodLabel", "iframeTitle", "invalidFileMessage",
			"invalidSizeMessage", "itemLabel", "label", "menuTitle", "message", "moveButtomLabel", "moveDownLabel", "moveUpLabel",
			"moveTopLabel", "removeAllLabel", "removeLabel", "removeFrom", "removeLabel", "requiredMessage", "resizeTitle", "saveLabel",
			"summary", "titletip", "toggleTitle", "tooltip", "uploadLabel", "validatorMessage", "welcomeMessage" };

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		if (component.getAttributes().containsKey("puitranslate")) {
			if (component.getChildCount() == 1) {
				UIComponent kid = component.getChildren().get(0);
				String caption = kid.toString();
				if (null != caption) {
					String translation = translate(caption);
					if (null != translation) {
						component.getAttributes().put("value", translation);
						component.getChildren().clear();
					}
				}
			}
		}
		if (!isAjaxRequest) {
			for (String attributeName : attributesToBeTranslated) {
				translateAttribute(component, attributeName);
			}
			if (component instanceof UICommand || component instanceof UIOutput) {
				translateAttribute(component, "value");
			}
		}

		return VisitResult.ACCEPT;
	}

	private void translateAttribute(UIComponent component, String attributeName) {
		try {
		Object value = AttributeUtilities.getAttribute(component, attributeName);
		if (null != value && value instanceof String) {
			String caption = (String) value;
			if (null != caption) {
				String translation = translate(caption);
				if (!caption.equals(translation)) {
					component.getAttributes().put(attributeName, translation);
				}
			}
		}
		} catch (IllegalArgumentException doesNotExist) {
			// Glassfish throws an IllegalArgumentException if the attribute doesn't exist.
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
