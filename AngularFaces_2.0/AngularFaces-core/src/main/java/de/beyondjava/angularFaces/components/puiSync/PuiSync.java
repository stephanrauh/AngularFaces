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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import com.google.gson.Gson;

import de.beyondjava.angularFaces.core.ELTools;

/**
 * Sends AngularJS model attributes back to the JSF model.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

@FacesComponent("de.beyondjava.sync")
public class PuiSync extends UIInput implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String COMPONENT_FAMILY = "de.beyondjava.sync";

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public void updateModel(FacesContext context) {
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		String direction = (String) getAttributes().get("direction");
		if (!"serverToClient".equals(direction)) {
			super.encodeBegin(context);
		}
	}

	@Override
	public void decode(FacesContext context) {
		String rootProperty = ELTools.getCoreValueExpression(this);

		Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
		String json = parameterMap.get(this.getClientId());
		Object bean = ELTools.evalAsObject("#{" + rootProperty + "}");
		if (rootProperty.contains(".")) {
			try {
				Object fromJson = new Gson().fromJson(json, bean.getClass());
				String rootBean = rootProperty.substring(0, rootProperty.lastIndexOf("."));
				Object root = ELTools.evalAsObject("#{" + rootBean + "}");
				String nestedBeanName = rootProperty.substring(rootProperty.lastIndexOf('.') + 1);
				String setterName = "set" + nestedBeanName.substring(0, 1).toUpperCase() + nestedBeanName.substring(1);

				try {
					if (root != null) {
						Method setter = root.getClass().getDeclaredMethod(setterName, bean.getClass());
						setter.invoke(root, fromJson);
					}

				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NumberFormatException error) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "A number was expected, but something else was sent (" + rootProperty
								+ ")", "A number was expected, but something else was sent (" + rootProperty + ")"));

			} catch (IllegalArgumentException error) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Can't parse the data sent from the client (" + rootProperty + ")",
								"Can't parse the data sent from the client (" + rootProperty + ")"));

			} catch (Exception anyError) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"A technical error occured when trying to read the data sent from the client (" + rootProperty + ")",
								"A technical error occured when trying to read the data sent from the client (" + rootProperty + ")"));
			}
		}
	}
}