package de.beyondjava.angularFaces.core.puiEL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGBeanAttributeInfo;
import de.beyondjava.angularFaces.puiModelSync.PuiModelSync;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiELTransformer implements SystemEventListener {
	static String[] properties = { "label", "ngvalue", "header", "style", "styleClass" };

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiEL.PuiELTransformer");

	static {
		LOGGER.info("AngularFaces utility class PuiELTransformer ready for use.");
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
		boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
		Object source = event.getSource();
		if (!ajaxRequest) {
			processUIComponent(source);
		}
	}

	private static void processUIComponent(Object source) {
		if (!(source instanceof UIComponent))
			return;
		UIComponent component = (UIComponent) source;

		String ngApp = (String) component.getAttributes().get("ng-app");
		if (null != ngApp) {
			component.getPassThroughAttributes().put("ng-app", "");
		}
		String ngController = (String) component.getAttributes().get("ng-controller");
		if (null != ngController) {
			component.getPassThroughAttributes().put("ng-controller", ngController);
			List<UIComponent> children = component.getParent().getChildren();
			children.add(new PuiModelSync());
		}

		PuiModelSync body = findModelSyncTag(source);
		if (null != body) {
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

	private static void processAngularExpression(PuiModelSync body, UIComponent component, String key, Object value, String vs) {
		if (vs.contains("{{faces.")) {
			body.addJSFAttrbitute(vs.substring("{{faces.".length(), vs.length() - 2), "ngvalue".equals(key) ? component : null);
			if ("ngvalue".equals(key)) {
				System.out.println(component.getValueExpression("value") + "/" + component.getAttributes().get("value"));
				ValueExpression vex = ELTools.createValueExpression("#{" + ((String) value).substring(2));
				component.setValueExpression("value", vex);
			}
		} else if (vs.contains(".{")) {
			body.addJSFAttrbitute(vs.substring(2, vs.length() - 1), "ngvalue".equals(key) ? component : null);
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
							Method method;
							try {
								method = component.getClass().getMethod("setType", String.class);
								method.invoke(component, "number");
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
			return true;
		}
		return false;
	}

	private static PuiModelSync findModelSyncTag(Object source) {
		UIComponent c = (UIComponent) source;
		while ((c != null) && !(c.getAttributes().containsKey("ng-controller")))
			c = c.getParent();
		if (null != c && c.getParent() != null) {
			UIComponent maybe = c.getParent().getChildren().get(c.getParent().getChildCount() - 1);
			if (maybe instanceof PuiModelSync)
				return (PuiModelSync) maybe;
		}
		return null;
	}
}
