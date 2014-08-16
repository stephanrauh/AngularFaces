package de.beyondjava.angularFaces.core.puiEL;

import java.util.Collection;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.component.inputtext.InputText;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGBeanAttributeInfo;
import de.beyondjava.angularFaces.flavors.kendo.puiBody.PuiBody;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiELTransformer implements SystemEventListener {
	static String[] properties = { "label", "ngvalue", "header", "style", "styleClass" };

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiEL.PuiEL");

	static {
		LOGGER.info("AngularFaces utility class PuiEL ready for use.");
	}
	
	public static void processEverything(UIComponent parent) {
		int index = 0;
		while (index < parent.getChildCount()) {
			UIComponent kid = parent.getChildren().get(index);
			if (kid instanceof UIComponent) {
				processUIComponent(kid);
			}
			processEverything(kid);
			index++;
		}
	}


	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		boolean postback = FacesContext.getCurrentInstance().isPostback();
		boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
		Collection<String> renderIds = FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds();
		Object source = event.getSource();
		if (!ajaxRequest) {
			processUIComponent(source);
		}
	}


	private static void processUIComponent(Object source) {
		PuiBody body = findBodyTag(source);
		if (source instanceof UIComponent && null != body) {
			UIComponent component = (UIComponent) source;
			for (String key : properties) {
				Object value = component.getAttributes().get(key);
				if (value != null) {
					if (value instanceof String) {
						String vs = (String) value;
						processAngularExpression(body, component, key, value, vs);
					}
				}
			}
		}
	}

	private static void processAngularExpression(PuiBody body, UIComponent component, String key, Object value, String vs) {
		if (vs.contains("{{faces.")) {
			body.addJSFAttrbitute(vs.substring("{{faces.".length(), vs.length() - 2));
			if ("ngvalue".equals(key)) {
				System.out.println(component.getValueExpression("value") + "/" + component.getAttributes().get("value"));
				ValueExpression vex = ELTools.createValueExpression("#{" + ((String) value).substring(2));
				component.setValueExpression("value", vex);
			}
		}
		else if (vs.contains(".{")) {
			body.addJSFAttrbitute(vs.substring(2, vs.length() - 1));
			if ("ngvalue".equals(key)) {
				vs = vs.replace(".{", "faces.");
				vs = vs.replace("}", "");
				component.getPassThroughAttributes().put("ng-model", vs);
				ValueExpression vex = ELTools.createValueExpression("#{" + ((String) value).substring(2));
				component.setValueExpression("value", vex);
				if (component instanceof UIInput) {
					NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(component);
					if (infos.isNumeric()) {
						if (component.getClass().getName().equals("org.primefaces.component.inputtext.InputText")) {
							((InputText)component).setType("number");
						}

						else {

							component.getPassThroughAttributes().put("type", "number");
						}
					}
				}
			} else {
				vs = vs.replace(".{", "{{faces.");
				vs = vs.replace("}", "}}");
				component.getAttributes().put(key, vs);
			}
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		if (source instanceof UIComponent) {
			UIComponent c = findBodyTag(source);
			return c != null && c instanceof PuiBody;
		}
		return false;
	}

	private static PuiBody findBodyTag(Object source) {
		UIComponent c = (UIComponent) source;
		while ((c != null) && !(c instanceof PuiBody))
			c = c.getParent();
		return (PuiBody) c;
	}
}
