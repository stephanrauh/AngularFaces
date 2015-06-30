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
package de.beyondjava.angularFaces.components.puiSync;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

@FacesRenderer(componentFamily = "de.beyondjava", rendererType = "de.beyondjava.sync")
public class PuiSyncRenderer extends Renderer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.PuiSync.PuiSyncRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		String direction = AttributeUtilities.getAttributeAsString(component, "direction");
		if ("serverToClient".equalsIgnoreCase(direction))
			return;

		ResponseWriter writer = context.getResponseWriter();

		String clientId = component.getClientId(context);
		writer.startElement("input", component);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
		writer.writeAttribute("type", "hidden", null);

		String value = AttributeUtilities.getAttributeAsString(component, "value");
		if (null == value) {
			LOGGER.severe("Which value do you want to synchronize?");
			throw new FacesException("ngSync: Which value do you want to synchronize?");
		}
		writer.writeAttribute("value", "{{"+value+"|json}}", null);
		String styleClass = AttributeUtilities.getAttributeAsString(component, "styleClass");
		if (null == styleClass)
			writer.writeAttribute("class", "puisync", "class");
		else
			writer.writeAttribute("class", "puisync " + styleClass, "class");
		writer.endElement("input");
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		String direction = AttributeUtilities.getAttributeAsString(component, "direction");
		if ("serverToClient".equalsIgnoreCase(direction))
			return;
		String rootProperty = AttributeUtilities.getAttributeAsString(component, "value");

		Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
		String json = parameterMap.get(component.getClientId());
		Object bean = ELTools.evalAsObject("#{" + rootProperty + "}");
		try {
			Object fromJson = JSONUtilities.readObjectFromJSONString(json, bean.getClass());
			if (null == fromJson) {
				LOGGER.severe("Couldn't convert the JSON object sent from the client to a JSF bean:");
				LOGGER.severe("Class of the bean: " + bean.getClass().getName());
				LOGGER.severe("JSON: " + json);
			} else if (rootProperty.contains(".")) {
				String rootBean = rootProperty.substring(0, rootProperty.lastIndexOf("."));
				injectJsonIntoBean(rootBean, rootProperty, bean, fromJson);
			} else {
				Method[] methods = fromJson.getClass().getMethods();
				for (Method m : methods) {
					if (m.getName().startsWith("get") && (m.getParameterTypes() != null || m.getParameterTypes().length == 0)) {
						try {
							Method setter = bean.getClass().getMethod("s" + m.getName().substring(1), m.getReturnType());
							Object attr = m.invoke(fromJson);
							setter.invoke(bean, attr);
						} catch (NoSuchMethodException noError) {
							// most likely this is not an error
							// LOGGER.log(Level.INFO, "An error occured when trying to inject the JSON object into the JSF bean", noError);
						}
					} else if (m.getName().startsWith("is") && (m.getParameterTypes() != null || m.getParameterTypes().length == 0)) {
						try {
							Method setter = bean.getClass().getMethod("set" + m.getName().substring(2), m.getReturnType());
							Object attr = m.invoke(fromJson);
							setter.invoke(bean, attr);
						} catch (NoSuchMethodException noError) {
							// most likely this is not an error
							// LOGGER.log(Level.INFO, "An error occured when trying to inject the JSON object into the JSF bean", noError);
						}
					}

				}
			}
		} catch (NumberFormatException error) {
			LOGGER.log(Level.SEVERE, "A number was expected, but something else was sent (" + rootProperty + ")", error);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "A number was expected, but something else was sent (" + rootProperty
							+ ")", "A number was expected, but something else was sent (" + rootProperty + ")"));

		} catch (IllegalArgumentException error) {
			LOGGER.log(Level.SEVERE, "Can't parse the data sent from the client (" + rootProperty + ")", error);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Can't parse the data sent from the client (" + rootProperty + ")",
							"Can't parse the data sent from the client (" + rootProperty + ")"));

		} catch (Exception anyError) {
			LOGGER.log(Level.SEVERE, "A technical error occured when trying to read the data sent from the client (" + rootProperty + ")",
					anyError);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"A technical error occured when trying to read the data sent from the client (" + rootProperty + ")",
							"A technical error occured when trying to read the data sent from the client (" + rootProperty + ")"));
		}
	}

	private void injectJsonIntoBean(String rootBean, String rootProperty, Object bean, Object fromJson) {
		Object root = ELTools.evalAsObject("#{" + rootBean + "}");
		String nestedBeanName = rootProperty.substring(rootProperty.lastIndexOf('.') + 1);
		String setterName = "set" + nestedBeanName.substring(0, 1).toUpperCase() + nestedBeanName.substring(1);

		try {
			if (root != null) {
				Method setter = root.getClass().getDeclaredMethod(setterName, bean.getClass());
				setter.invoke(root, fromJson);
			}

		} catch (ReflectiveOperationException e) {
			LOGGER.severe("Couln't find setter method: " + setterName);
			e.printStackTrace();
		}
	}

}
