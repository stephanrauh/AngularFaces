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
	boolean recursive = true;

	public AddLabelsAndMessagesHandler(TagConfig config) {
		super(config);
		TagAttribute recursiveTag = config.getTag().getAttributes().get("recursive");
		if (null != recursiveTag) {
			if ("false".equalsIgnoreCase(recursiveTag.getValue()))
				recursive = false;
		}
		TagAttribute labels = config.getTag().getAttributes().get("addLabels");
		if (null != labels) {
			if ("false".equalsIgnoreCase(labels.getValue()))
				addLabels = false;
		}
		TagAttribute messages = config.getTag().getAttributes().get("addMessages");
		if (null != messages) {
			if ("false".equalsIgnoreCase(messages.getValue()))
				addMessages = false;
		}
	}

	@Override
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
		if (addLabels || addMessages) {
				populateTree(parent);
		}
//		nextHandler.apply(ctx, parent); // Delegate job further to first next tag in tree hierarchy.
	}

	private void populateTree(UIComponent parent) {
		List<UIComponent> children = parent.getChildren();
		for (int index = children.size() - 1; index >= 0; index--) {
			UIComponent kid = children.get(index);
			if (kid instanceof UIInput) {
				if (addMessages)
					addSingleMessage(children, index, kid);
				if (addLabels)
					addSingleLabel(children, index, kid);
			}
			if (recursive)
				populateTree(kid);
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
