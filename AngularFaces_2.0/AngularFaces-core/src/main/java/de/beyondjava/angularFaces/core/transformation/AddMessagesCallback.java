package de.beyondjava.angularFaces.core.transformation;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.components.puiMessage.PuiMessage;

public class AddMessagesCallback implements VisitCallback {
	int duplicateLabels = 0;

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent parent) {
		if (!(parent instanceof UIComponent))
			return VisitResult.ACCEPT;
		boolean needsMessage = true;
		List<UIComponent> children = parent.getChildren();
		for (int index = children.size() - 1; index >= 0; index--) {
			UIComponent kid = children.get(index);
			if (kid instanceof UIInput) {
				for (int j = 0; j < children.size(); j++) {
					UIComponent maybe = children.get(j);
					if (maybe instanceof PuiMessage) {
						if (kid.getClientId().equals(((PuiMessage) maybe).getFor())) {
							duplicateLabels++;
							if (j != index + 1) {
								System.out.println("Message has been restored at the wrong position");
								children.remove(j);
								if (j < index)
									index--;
							}
							continue;
						}
					} else if (maybe instanceof UIMessage) {
						UIComponent forComponent = maybe.findComponent(((UIMessage) maybe).getFor());
						if (kid==forComponent) {
							needsMessage = false;
						}
					}

				}
				if (index < children.size() - 1) {
					UIComponent maybe = children.get(index + 1);
					if (maybe instanceof PuiMessage) {
						UIComponent forComponent = maybe.findComponent(((UIMessage) maybe).getFor());
						if (kid==forComponent) {
							duplicateLabels++;
							continue;
						}
					}

				}

				if (needsMessage) {
					PuiMessage message;
					if (kid.getClass().getName().contains("primefaces")) {
						// todo: use the PrimeFaces Message
						message = new PuiMessage(); //
					} else {
						message = new PuiMessage();
					}
					message.setFor(kid.getClientId());
					message.getPassThroughAttributes().put("af-for", kid.getClientId());
					children.add(index + 1, message);
				}

			}
		}

		return VisitResult.ACCEPT;
	}

}
