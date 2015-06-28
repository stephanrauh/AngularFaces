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
package de.beyondjava.angularFaces.components.puiModelSync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
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
	private static final String JSF_ATTRIBUTES_SESSION_CACHE = "de.beyondjava.angularFaces.cache";

	// private static final Logger LOGGER =
	// Logger.getLogger("de.beyondjava.kendoFaces.puiBody.PuiBody");

	/**
	 * This method is not as superfluous as it seems. We need it to be able to
	 * call getStateHelper() in defender methods.
	 */
	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	@SuppressWarnings("unchecked")
	public static boolean isJSFAttributesTableEmpty() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
		return (null == jsfAttributes || jsfAttributes.isEmpty());
	}

	@SuppressWarnings("unchecked")
	public static void initJSFAttributesTable() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (!sessionMap.containsKey(JSF_ATTRIBUTES_SESSION_PARAMETER)) {
			sessionMap.put(JSF_ATTRIBUTES_SESSION_PARAMETER, new HashMap<String, String>());
			sessionMap.put(JSF_ATTRIBUTES_SESSION_CACHE, new HashMap<String, Object>());
		} else {
			Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
			jsfAttributes.clear();
		}
	}

	@SuppressWarnings("unchecked")
	public static void addJSFAttrbitute(String key, UIComponent component, boolean cacheable, boolean onlyOnce) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
		String suffix = cacheable ? "1" : "0";
		suffix += onlyOnce ? "1" : "0";
		jsfAttributes.put(key, component.getClientId() + suffix);
	}

	/**
	 * Builds basically a JSON structure from the JSF model.
	 * 
	 * @param model
	 *            the model to be built
	 * @param key
	 *            variable name
	 * @param value
	 *            variable value
	 * @param cacheable
	 *            if true, the value is only sent if it's different from the
	 *            value of the same attribute in the previous response
	 */
	@SuppressWarnings("unchecked")
	public static void addJSFAttrbituteToAngularModel(Map<String, Object> model, String key, Object value,
			boolean cacheable) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, Object> cache = (Map<String, Object>) sessionMap.get(JSF_ATTRIBUTES_SESSION_CACHE);
		if (cache.containsKey(key)) {
			if (cacheable)
				if (FacesContext.getCurrentInstance().isPostback()) {
					Object previousValue = cache.get(key);
					if (null == value && previousValue == null) {
						return;
					}
					if (null != value && value.equals(previousValue)) {
						return;
					}
				}
			cache.remove(key);
		}
		cache.put(key, value);

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
	}

	@SuppressWarnings("unchecked")
	public static List<String> getFacesModel() {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Map<String, String> jsfAttributes = (Map<String, String>) sessionMap.get(JSF_ATTRIBUTES_SESSION_PARAMETER);
		UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
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

				UIComponent comp = root.findComponent(id);
				if (null != comp && comp instanceof EditableValueHolder) {
					Object value = ELTools.evalAsObject("#{" + attribute + "}");
					Object valueToRender = getValueToRender(FacesContext.getCurrentInstance(), comp);
					if (value != null && valueToRender != null && valueToRender instanceof String) {
						try {
							valueToRender = convertToDatatype((String) valueToRender, value.getClass());
						} catch (Exception e) {

						}
					}
					if (null != valueToRender) {
						addJSFAttrbituteToAngularModel(model, attribute, valueToRender, cacheable);
					} else {
						addJSFAttrbituteToAngularModel(model, attribute, value, cacheable);
					}
				} else {
					Object value = ELTools.evalAsObject("#{" + attribute + "}");
					// vex.getValue(FacesContext.getCurrentInstance().getELContext())
					addJSFAttrbituteToAngularModel(model, attribute, value, cacheable);
				}
			} catch (PropertyNotFoundException pureAngularAttribute) {
				// probably it's an AngularJS attribute that doesn't have a JSF
				// counterpart, so we don't consider this an error
			}
		}

		List<String> beans = new ArrayList<String>();
		String messages = "";
		List<FacesMessage> messageList = FacesContext.getCurrentInstance().getMessageList();
		for (FacesMessage message : messageList) {
			if (!message.isRendered()) {
				String severity = message.getSeverity().toString();
				String summary = message.getSummary().replace("'", "\\'");
				String detail = message.getDetail().replace("'", "\\'");
				messages += ",{\"severity\":\"" + severity + "\", \"summary\":\"" + summary + "\", \"detail\":\""
						+ detail + "\"}";
				message.rendered();
			}
		}
		if (messages.length() > 0) {
			String assignment = "\"" + "facesmessages\", '[" + messages.substring(1) + "]'";
			beans.add(assignment);
		}

		for (Entry<String, Object> bean : model.entrySet()) {
			String assignment = "\"" + bean.getKey() + "\",'"
					+ JSONUtilities.writeObjectToJSONString(bean.getValue()).replace("'", "\\'") + "'";
			beans.add(assignment);
		}
		return beans;
	}

	private static Object convertToDatatype(String valueToRender, Class<?> targetClass) {
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
	 * This method has been copied from the PrimeFaces 5 project (and been
	 * adapted after).
	 * 
	 * Algorithm works as follows; - If it's an input component, submitted value
	 * is checked first since it'd be the value to be used in case validation
	 * errors terminates jsf lifecycle - Finally the value of the component is
	 * retrieved from backing bean and if there's a converter, converted value
	 * is returned
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
		writer.write("if (window.jsfScope==null) {");
		writer.writeText("window.jsfScope=$scope;", null);
		if (debugMode) {
			writer.append("\r\n  ");
		}
		if (debugMode) {
			writer.append("\r\n  ");
		}
		List<String> beansAsJSon = getFacesModel();
		for (String bean : beansAsJSon) {
			writer.writeText("puiUpdateModel(" + bean + ");", null);
		}
		if (debugMode) {
			writer.append("\r\n");
		}
		writer.writeText("}", null);
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
		String includeMainJS = FacesContext.getCurrentInstance().getExternalContext()
				.getInitParameter("AngularFaces.includeMainJS");
		if (null == includeMainJS || "true".equals(includeMainJS)) {
			String main = AttributeUtilities.getAttributeAsString(this, "angularJSFile");
			if (main == null) {
				main = "main.js";
			}
			if (main != null && main.length() > 0 && (!main.equals("none"))) {
				if (!main.endsWith(".js")) {
					main = main + ".js";
				}
				writer.append("<script src='" + main + "'></script>");
			}
		}

	}

	@Override
	public void encodeChildren(FacesContext arg0) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext arg0) throws IOException {
	}
}
