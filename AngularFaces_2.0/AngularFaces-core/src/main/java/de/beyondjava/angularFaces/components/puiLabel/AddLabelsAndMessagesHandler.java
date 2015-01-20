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
package de.beyondjava.angularFaces.components.puiLabel;

import java.io.IOException;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import de.beyondjava.angularFaces.components.puiMessage.PuiMessage;
import de.beyondjava.angularFaces.components.puiSync.PuiSync;
import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGWordUtiltites;
import de.beyondjava.angularFaces.core.i18n.I18n;
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

public class AddLabelsAndMessagesHandler extends TagHandler {
	private I18n i18n = null;

	boolean addLabels = true;
	boolean addMessages = true;

	public AddLabelsAndMessagesHandler(TagConfig config) {
		super(config);
	}

	@Override
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
		String l = AttributeUtilities.getAttributeAsString(parent, "addLabels");
		if (l != null) {
			if ("false".equalsIgnoreCase(l))
				addLabels = false;
			else {
				addLabels = true;
			}
		}
		String m = AttributeUtilities.getAttributeAsString(parent, "addMessages");
		if (m != null) {
			if ("false".equalsIgnoreCase(m))
				addMessages = false;
			else {
				addMessages = true;
			}
		}

		if (addLabels || addMessages) {
			populateTree(parent, addLabels, addMessages);
		}
	}

	private void populateTree(UIComponent parent, boolean addLabels, boolean addMessages) {
		List<UIComponent> children = parent.getChildren();
		for (int index = children.size() - 1; index >= 0; index--) {
			UIComponent kid = children.get(index);
			if (kid instanceof UIInput) {
				if (addMessages)
					addSingleMessage(children, index, kid);
				if (addLabels)
					addSingleLabel(children, index, kid);
			}
			String l = AttributeUtilities.getAttributeAsString(kid, "addLabels");
			if (l != null) {
				if ("false".equalsIgnoreCase(l))
					addLabels = false;
				else {
					addLabels = true;
				}
			}
			String m = AttributeUtilities.getAttributeAsString(kid, "addMessages");
			if (m != null) {
				if ("false".equalsIgnoreCase(m))
					addMessages = false;
				else {
					addMessages = true;
				}
			}
			populateTree(kid, addLabels, addMessages);
		}
	}

	private void addSingleLabel(List<UIComponent> children, int index, UIComponent kid) {
		boolean needsLabel = true;
		String addLabelAttribute = AttributeUtilities.getAttributeAsString(kid, "addLabel");
		if (null != addLabelAttribute && "false".equalsIgnoreCase(addLabelAttribute)) {
			needsLabel = false;
		}

		if (needsLabel) {
			String caption = AttributeUtilities.getAttributeAsString(kid, "label");

			if (null == caption) {
				ValueExpression vex = kid.getValueExpression("value");
				if (null != vex) {
					String core = vex.getExpressionString();
					caption = NGWordUtiltites.labelFromELExpression(core);
				}
			}
			if (null != caption) {
				for (int j = 0; j < children.size(); j++) {
					UIComponent maybe = children.get(j);
					if (maybe instanceof HtmlOutputLabel) {
						if (kid.getId().equals(((HtmlOutputLabel) maybe).getFor())) {
							needsLabel = false;
						}
					}

				}
				if (needsLabel) {
					HtmlOutputLabel label = new PuiLabel();
					label.getPassThroughAttributes().put("for", kid.getClientId());
					kid.getAttributes().put("renderLabel", false);
					label.setFor(kid.getId());
					if (null != caption) {
						label.setValue(translate(caption));
					}
					children.add(index, label);
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

	private int addSingleMessage(List<UIComponent> children, int index, UIComponent kid) {
		if (kid instanceof UIInput && (!(kid instanceof PuiSync))) {
			boolean needsMessage = true;
			String addMessageAttribute = AttributeUtilities.getAttributeAsString(kid, "addMessage");
			if (null != addMessageAttribute && "false".equalsIgnoreCase(addMessageAttribute)) {
				needsMessage = false;
			}
			if (needsMessage) {
				if (index < children.size() - 1) {
					UIComponent maybe = children.get(index + 1);
					if (maybe instanceof PuiMessage) {
						UIComponent forComponent = maybe.findComponent(((UIMessage) maybe).getFor());
						if (kid == forComponent) {
							needsMessage = false;
						}
					}
				}

				if (needsMessage) {
					PuiMessage message;
					message = new PuiMessage(); //
					message.setFor(kid.getClientId());
					message.getPassThroughAttributes().put("for", kid.getClientId());
					children.add(index + 1, message);
				}
			}

		}
		return index;
	}
}
