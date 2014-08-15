package de.beyondjava.angularFaces.core.puiEL;

import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.core.NGWordUtiltites;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiLabelDecorator implements SystemEventListener {
	static String[] properties = { "label", "ngvalue", "header", "style", "styleClass" };

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiEL.PuiEL");

	static {
		LOGGER.info("AngularFaces utility class PuiEL ready for use.");
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		// ResponseStateManager.
		boolean postback = FacesContext.getCurrentInstance().isPostback();
		// boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
		// Collection<String> renderIds = FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds();
		if (!postback) {
			UIComponent source = (UIComponent) event.getSource();
			decorateChildren(source);
		}
	}

	private void decorateChildren(UIComponent parent) {
		int index = parent.getChildCount() - 1;
		while (index >= 0) {
			UIComponent kid = parent.getChildren().get(index);
			decorateChildren(kid);
			String capture = (String) kid.getAttributes().get("label");
			if (null == capture) {
				ValueExpression vex = kid.getValueExpression("value");
				if (null != vex) {
					String core =vex.getExpressionString();
					capture = NGWordUtiltites.labelFromELExpression(core);
				}
			}
			if (null != capture) {
				HtmlOutputLabel label = new HtmlOutputLabel();
				label.setFor(kid.getId());
				if (null != capture) {
					label.setValue(capture);
				}
				parent.getChildren().add(index, label);
			}
			index--;
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}

}
