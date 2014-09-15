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
package de.beyondjava.angularFaces.flavors.kendo.puiBody;

import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;

import org.primefaces.config.ConfigContainer;
import org.primefaces.context.RequestContext;

import com.google.gson.Gson;

import de.beyondjava.angularFaces.common.IAngularController;
import de.beyondjava.angularFaces.core.ELTools;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiBody.PuiBody")
public class PuiBody extends HtmlBody implements IAngularController {
	enum propertyKeys {
		publishAs, selector
	}

	Map<String, UIComponent> jsfAttributes = new HashMap<>();

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiBody.PuiBody");

	/**
	 * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender methods.
	 */
	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	public void addJSFAttrbitute(String key, UIComponent component) {
		jsfAttributes.put(key, component);
	}

	/** Builds basically a JSON structure from the JSF model. */
	public void addJSFAttrbituteToAngularModel(Map<String, Object> model, String key, Object value) {
		String[] keys = key.split("\\.");
		Map<String, Object> currentMap = model;
		for (int i = 0; i < keys.length - 1; i++) {
			if (!currentMap.containsKey(keys[i])) {
				currentMap.put(keys[i], new HashMap<String, Object>());
			}
			currentMap = (Map<String, Object>) currentMap.get(keys[i]);
		}
		Object v = null;
		if (value != null) {
			Class<? extends Object> type = value.getClass();
			if (type == int.class || type == long.class || type == float.class || type == double.class || type == byte.class
					|| type == short.class || Number.class.isAssignableFrom(type)) {
				v = value;
			} else
				v = value.toString();
		}
		currentMap.put(keys[keys.length - 1], v);
	}

	public String getFacesModel() {
		Map<String, Object> model = new HashMap<>();
		for (Entry<String, UIComponent> entry : jsfAttributes.entrySet()) {
			String attribute = entry.getKey();
			UIComponent comp = entry.getValue();
			if (null != comp) {
				Object value = ELTools.evalAsObject("#{" + attribute + "}");
				Object valueToRender = getValueToRender(FacesContext.getCurrentInstance(), comp);
				if (value!=null && valueToRender!=null && valueToRender instanceof String) {
					valueToRender=convertToDatatype((String) valueToRender, value.getClass());
				}
				addJSFAttrbituteToAngularModel(model, attribute, valueToRender);
			} else {
				Object value = ELTools.evalAsObject("#{" + attribute + "}");
				// vex.getValue(FacesContext.getCurrentInstance().getELContext())
				addJSFAttrbituteToAngularModel(model, attribute, value);
			}
		}

		Gson gs = new Gson();
		return gs.toJson(model);
	}

	private Object convertToDatatype(String valueToRender, Class targetClass) {
		if (targetClass==int.class) return new Integer((String)valueToRender).intValue();
		if (targetClass==long.class) return new Long((String)valueToRender).longValue();
		if (targetClass==short.class) return new Short((String)valueToRender).shortValue();
		if (targetClass==float.class) return new Float((String)valueToRender).floatValue();
		if (targetClass==double.class) return new Double((String)valueToRender).doubleValue();
		if (targetClass==byte.class) return new Byte((String)valueToRender).byteValue();
		
		if (targetClass==Integer.class) return new Integer((String)valueToRender);
		if (targetClass==Long.class) return new Long((String)valueToRender);
		if (targetClass==Short.class) return new Short((String)valueToRender);
		if (targetClass==Float.class) return new Float((String)valueToRender);
		if (targetClass==Double.class) return new Double((String)valueToRender);
		if (targetClass==Byte.class) return new Byte((String)valueToRender);
		return null;
	}

	/**
	 *  This method has been copied from the PrimeFaces 5 project.
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
				ConfigContainer config = RequestContext.getCurrentInstance().getApplicationContext().getConfig();

				if (config.isInterpretEmptyStringAsNull() && submittedValue == null && context.isValidationFailed() && !input.isValid()) {
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
}
