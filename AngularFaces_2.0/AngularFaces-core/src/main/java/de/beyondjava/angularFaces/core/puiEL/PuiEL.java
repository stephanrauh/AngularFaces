package de.beyondjava.angularFaces.core.puiEL;

import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGBeanAttributeInfo;
import de.beyondjava.angularFaces.flavors.kendo.puiBody.PuiBody;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiEL implements SystemEventListener {
	static String[] properties = { "ngvalue", "header", "style", "styleClass" };

	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.angularFaces.puiEL.PuiEL");

	static {
		LOGGER.info("AngularFaces utility class PuiEL ready for use.");
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		// ResponseStateManager.
		boolean postback = FacesContext.getCurrentInstance().isPostback();
		Object source = event.getSource();
		if (source instanceof UIForm) {

		}
		PuiBody body = findBodyTag(source);
		if (source instanceof UIComponent && null != body) {
			UIComponent component = (UIComponent) source;
			for (String key : properties) {
				Object value = component.getAttributes().get(key);
				if (value != null) {
					if (value instanceof String) {
						String vs = (String)value;
						if (vs.contains(".{")) {
							ValueExpression vex = ELTools.createValueExpression( "#{"+vs.substring(2));
							body.addJSFAttrbituteToAngularModel(vs.substring(2, vs.length()-1), vex.getValue(FacesContext.getCurrentInstance().getELContext()));
							if ("ngvalue".equals(key)) {
								vs = vs
										.replace(".{", "faces.");
								vs = vs.replace("}", "");
								component.getPassThroughAttributes().put(
										"ng-model", vs);
								component.setValueExpression("value",vex);
								if (component instanceof UIInput) {
									NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(component);
									if (infos.isNumeric()) {
										component.getPassThroughAttributes().put("type", "number");
									}
									
								}
							} else {
								vs = vs.replace(".{",
										"{{faces.");
								vs = vs.replace("}", "}}");
								component.getAttributes().put(key, vs);
							}
						}
					}
				}
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

	private PuiBody findBodyTag(Object source) {
		UIComponent c = (UIComponent) source;
		while ((c != null) && !(c instanceof PuiBody))
			c = c.getParent();
		return (PuiBody) c;
	}
}
