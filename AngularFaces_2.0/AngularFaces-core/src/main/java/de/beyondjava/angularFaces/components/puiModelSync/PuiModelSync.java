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
package de.beyondjava.angularFaces.components.puiModelSync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.beyondjava.angularFaces.components.puiSync.JSONUtilities;
import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

/**
 * PuiBody is an HtmlBody that activates the AngularJS/AngularDart framework.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiBody.PuiBody")
public class PuiModelSync extends HtmlBody {

	private static final String JSF_ATTRIBUTES_SESSION_PARAMETER = "de.beyondjava.angularFaces.jsfAttributes";
	// private static final String JSF_ATTRIBUTES_SESSION_CACHE = "de.beyondjava.angularFaces.cache";

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiBody.PuiBody");

	/**
	 * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender methods.
	 */
	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	public static boolean isJSFAttributesTableEmpty() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
		return (null == jsfAttributes || jsfAttributes.isEmpty());
	}

	public static void initJSFAttributesTable() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (!sessionMap.containsKey(JSF_ATTRIBUTES_SESSION_PARAMETER)) {
			sessionMap.put(JSF_ATTRIBUTES_SESSION_PARAMETER, new HashMap<String, String>());
		} else {
			Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
			jsfAttributes.clear();
		}
	}

	public static void addJSFAttrbitute(String key, UIComponent component, boolean cacheable, boolean onlyOnce) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
		String suffix = cacheable ? "1" : "0";
		suffix += onlyOnce ? "1" : "0";
		jsfAttributes.put(key, component.getClientId() + suffix);
	}

	/** Builds basically a JSON structure from the JSF model. */
	public void addJSFAttrbituteToAngularModel(Map<String, Object> model, String key, Object value) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		// if (sessionMap.containsKey(JSF_ATTRIBUTES_SESSION_CACHE + key)) {
		// Object previousValue = sessionMap.get(JSF_ATTRIBUTES_SESSION_CACHE + key);
		// if (null == value && previousValue == null) {
		// return;
		// }
		// if (null != value && value.equals(previousValue)) {
		// return;
		// }
		// sessionMap.remove(JSF_ATTRIBUTES_SESSION_CACHE + key);
		// }

		String[] keys = key.split("\\.");
		Map<String, Object> currentMap = model;
		for (int i = 0; i < keys.length - 1; i++) {
			if (!currentMap.containsKey(keys[i])) {
				currentMap.put(keys[i], new HashMap<String, Object>());
			}
			final Object object = currentMap.get(keys[i]);
			if (!(object instanceof Map)) {
				// the parent object has already been stored
				return;
			}
			currentMap = (Map<String, Object>) object;
		}
		currentMap.put(keys[keys.length - 1], value);
		// sessionMap.put(JSF_ATTRIBUTES_SESSION_CACHE + key, value);
	}

	public List<String> getFacesModel() {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
		// sessionMap.remove(JSF_ATTRIBUTES_SESSION_PARAMETER);
		for (Entry<String, String> entry : jsfAttributes.entrySet()) {
			try {
				String attribute = entry.getKey();
				final int length = entry.getValue().length();
				String id = entry.getValue().substring(0, length - 2);
				boolean cacheable = entry.getValue().charAt(length - 2) == '1';
				boolean onlyOnce = entry.getValue().charAt(length - 1) == '1';
				if (onlyOnce && FacesContext.getCurrentInstance().isPostback())
					continue;

				UIComponent comp = findComponent(id);
				if (null != comp && comp instanceof EditableValueHolder) {
					Object value = ELTools.evalAsObject("#{" + attribute + "}");
					Object valueToRender = getValueToRender(FacesContext.getCurrentInstance(), comp);
					if (value != null && valueToRender != null && valueToRender instanceof String) {
						valueToRender = convertToDatatype((String) valueToRender, value.getClass());
					}
					if (null != valueToRender) {
						addJSFAttrbituteToAngularModel(model, attribute, valueToRender);
					} else {
						addJSFAttrbituteToAngularModel(model, attribute, value);
					}
				} else {
					Object value = ELTools.evalAsObject("#{" + attribute + "}");
					// vex.getValue(FacesContext.getCurrentInstance().getELContext())
					addJSFAttrbituteToAngularModel(model, attribute, value);
				}
			} catch (PropertyNotFoundException pureAngularAttribute) {
				// probably it's an AngularJS attribute that doesn't have a JSF counterpart, so we don't consider this an error
			}
		}

		List<String> beans = new ArrayList<String>();
		String messages = "";
		List<FacesMessage> messageList = FacesContext.getCurrentInstance().getMessageList();
		for (FacesMessage message : messageList) {
			if (!message.isRendered()) {
				String severity = message.getSeverity().toString();
				String summary = message.getSummary();
				String detail = message.getDetail();
				messages += ",{\"severity\":\"" + severity + "\", \"summary\":\"" + summary + "\", \"detail\":\"" + detail + "\"}";
			}
		}
		if (messages.length() > 0) {
			String assignment = "\"" + "facesmessages \", '[" + messages.substring(1) + "]'";
			beans.add(assignment);
		}

		for (Entry<String, Object> bean : model.entrySet()) {
			String assignment = "\"" + bean.getKey() + "\",'" + JSONUtilities.writeObjectToJSONString(bean.getValue()) + "'";
			beans.add(assignment);
		}
		return beans;
	}

	private Object convertToDatatype(String valueToRender, Class targetClass) {
		if ("".equals(valueToRender))
			return null;
		if (null == valueToRender)
			return null;
		if (valueToRender.getClass() == targetClass)
			return valueToRender;
		if (targetClass == int.class)
			return new Integer((String) valueToRender).intValue();
		if (targetClass == long.class)
			return new Long((String) valueToRender).longValue();
		if (targetClass == short.class)
			return new Short((String) valueToRender).shortValue();
		if (targetClass == float.class)
			return new Float((String) valueToRender).floatValue();
		if (targetClass == double.class)
			return new Double((String) valueToRender).doubleValue();
		if (targetClass == byte.class)
			return new Byte((String) valueToRender).byteValue();

		if (targetClass == Integer.class)
			return new Integer((String) valueToRender);
		if (targetClass == Long.class)
			return new Long((String) valueToRender);
		if (targetClass == Short.class)
			return new Short((String) valueToRender);
		if (targetClass == Float.class)
			return new Float((String) valueToRender);
		if (targetClass == Double.class)
			return new Double((String) valueToRender);
		if (targetClass == Byte.class)
			return new Byte((String) valueToRender);
		return null;
	}

	/**
	 * This method has been copied from the PrimeFaces 5 project (and been adapted after).
	 * 
	 * Algorithm works as follows; - If it's an input component, submitted value is checked first since it'd be the value to be used in case
	 * validation errors terminates jsf lifecycle - Finally the value of the component is retrieved from backing bean and if there's a
	 * converter, converted value is returned
	 *
	 * @param context
	 *            FacesContext instance
	 * @param component
	 *            UIComponent instance whose value will be returned
	 * @return End text
	 */
	public static Object getValueToRender(FacesContext context, UIComponent component) {
		if (component instanceof ValueHolder) {

			if (component instanceof EditableValueHolder) {
				EditableValueHolder input = (EditableValueHolder) component;
				Object submittedValue = input.getSubmittedValue();

				if (submittedValue == null && context.isValidationFailed() && !input.isValid()) {
					return null;
				} else if (submittedValue != null) {
					return submittedValue;
				}
			}

			ValueHolder valueHolder = (ValueHolder) component;
			Object value = valueHolder.getValue();
			return value;
		}

		// component it not a value holder
		return null;
	}

	@Override
	public void encodeAll(FacesContext arg0) throws IOException {
		encodeBegin(arg0);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		boolean debugMode = context.isProjectStage(ProjectStage.Development);
		if (debugMode) {
			writer.append("\r\n");
		}
		writer.startElement("script", this);
		if (debugMode) {
			writer.append("\r\n");
		}
		writer.writeText("window.jsfScope=null;", null);
		if (debugMode) {
			writer.append("\r\n");
		}
		writer.writeText("function initJSFScope($scope){", null);
		if (debugMode) {
			writer.append("\r\n  ");
		}
		writer.writeText("window.jsfScope=$scope;", null);
		if (debugMode) {
			writer.append("\r\n  ");
		}
		if (debugMode) {
			writer.append("\r\n  ");
		}
		List<String> beansAsJSon = getFacesModel();
		for (String bean : beansAsJSon) {
			writer.writeText("injectJSonIntoScope(" + bean + ",$scope);", null);
		}
		if (debugMode) {
			writer.append("\r\n");
		}
		writer.writeText("}", null);
		if (debugMode) {
			writer.append("\r\n");
		}
		writer.endElement("script");
		if (debugMode) {
			writer.append("\r\n");
		}

		String main = AttributeUtilities.getAttributeAsString(this, "angularJSFile");
		if (main == null) {
			main = "main.js";
		}
		if (!main.endsWith(".js")) {
			main = main + ".js";
		}
		writer.append("<script src='" + main + "'></script>");

		PuiScriptRenderer r = new PuiScriptRenderer();
		// Locale locale = context.getViewRoot().getLocale();
		Locale locale = context.getExternalContext().getRequestLocale();
		String language = locale.getLanguage();
		r.encodeScript(context, this, "messages_" + language + ".js", "AngularFaces");
		r.encodeScript(context, this, "components.js", "AngularFaces");
		r.encodeScript(context, this, "glue.js", "AngularFaces");
		r.encodeMessageBundle(context);
	}

	@Override
	public void encodeChildren(FacesContext arg0) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext arg0) throws IOException {
	}
}
