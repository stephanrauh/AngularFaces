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
		if (null==value) {
			LOGGER.severe("Which value do you want to synchronize?");
			throw new FacesException("ngSync: Which value do you want to synchronize?");
		}
		writer.writeAttribute("value", "{{afToJson(" + value + ")}}", null);
		String styleClass = AttributeUtilities.getAttributeAsString(component, "styleClass");
		writer.writeAttribute("class", styleClass, null);
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
			if (rootProperty.contains(".")) {
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
//							LOGGER.log(Level.INFO, "An error occured when trying to inject the JSON object into the JSF bean", noError);
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
			LOGGER.log(Level.SEVERE, "A technical error occured when trying to read the data sent from the client (" + rootProperty + ")", anyError);
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
