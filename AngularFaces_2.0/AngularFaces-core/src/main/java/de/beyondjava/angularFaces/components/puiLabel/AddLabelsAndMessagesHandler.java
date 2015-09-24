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
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
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
		String addLabelsParam = FacesContext.getCurrentInstance().getExternalContext()
				.getInitParameter("AngularFaces.addLabels");
		if (null == addLabelsParam || "true".equalsIgnoreCase(addLabelsParam)) {
			addLabels = true;
		} else {
			addLabels = false;
		}

		boolean isPostBack = FacesContext.getCurrentInstance().isPostback();
		String l = AttributeUtilities.getAttributeAsString(parent, "addLabels");
		if (l != null) {
			if ("false".equalsIgnoreCase(l))
				addLabels = false;
		}
		String addMessagesParam = FacesContext.getCurrentInstance().getExternalContext()
				.getInitParameter("AngularFaces.addMessages");
		if (null == addMessagesParam || "true".equalsIgnoreCase(addMessagesParam)) {
			addMessages = true;
		} else {
			addMessages = false;
		}
		String m = AttributeUtilities.getAttributeAsString(parent, "addMessages");
		if (m != null) {
			if ("false".equalsIgnoreCase(m))
				addMessages = false;
		}

		if (addLabels || addMessages) {
			populateTree(parent, addLabels, addMessages, isPostBack);
		}
	}

	private void populateTree(UIComponent parent, boolean addLabels, boolean addMessages, boolean isPostBack) {
		List<UIComponent> children = parent.getChildren();

		for (int index = children.size() - 1; index >= 0; index--) {
			UIComponent kid = children.get(index);
			if (kid instanceof UIInput) {
				if (addMessages)
					addSingleMessage(children, index, kid, isPostBack);
				if (addLabels)
					addSingleLabel(children, kid, isPostBack);
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
			populateTree(kid, addLabels, addMessages, isPostBack);
		}
	}

	private void addSingleLabel(List<UIComponent> children, UIComponent kid, boolean isPostback) {
		int index = children.indexOf(kid);
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

				if (isPostback) {
					for (int i = 0; i < children.size(); i++) {
						UIComponent maybe = children.get(i);
						if (maybe instanceof PuiLabel) {
							UIComponent forComponent = maybe.findComponent(((PuiLabel) maybe).getFor());
							if (kid == forComponent && (index - 1) != i) {
								needsLabel = false;
								// System.out.println("Label at wrong location.
								// Wanted: " + (index-1) + " found at: " + i);
								children.remove(i);
								children.add(index - 1, maybe);

							}
						}
					}
				}

				if (needsLabel) {
					HtmlOutputLabel label = new PuiLabel();
					label.getPassThroughAttributes().put("for", kid.getClientId());
					kid.getAttributes().put("renderLabel", false);
					label.setFor(kid.getId());
					if (null != caption) {
						String translationParam = FacesContext.getCurrentInstance().getExternalContext()
								.getInitParameter("AngularFaces.translation");
						if (null == translationParam || "true".equalsIgnoreCase(translationParam)) {

							label.setValue(translate(caption));
						} else {
							label.setValue(caption);
						}
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

	private int addSingleMessage(List<UIComponent> children, int index, UIComponent kid, boolean isPostback) {
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

				if (needsMessage && isPostback) {
					for (int i = 0; i < children.size(); i++) {
						UIComponent maybe = children.get(i);
						if (maybe instanceof PuiMessage) {
							UIComponent forComponent = maybe.findComponent(((UIMessage) maybe).getFor());
							if (kid == forComponent) {
								needsMessage = false;
								// System.out.println("Message at wrong
								// location. Wanted: " + index + " found at: " +
								// i);
								children.remove(i);
								children.add(index, maybe);
								break;
							}
						}
					}
				}

				if (needsMessage) {
					PuiMessage message;
					message = new PuiMessage(); //
					message.setFor(kid.getId());
					message.getPassThroughAttributes().put("for", kid.getId());
					children.add(index + 1, message);
				}
			}

		}
		return index;
	}
}
