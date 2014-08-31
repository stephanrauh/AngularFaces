package de.beyondjava.angularFaces.components.puiSync;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import com.google.gson.Gson;

import de.beyondjava.angularFaces.core.ELTools;

/**
 * Sends AngularJS model attributes back to the JSF model.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

@FacesComponent("de.beyondjava.Sync")
public class Sync extends HtmlInputText {
	public static final String this_FAMILY = "org.primefaces.this";

	@Override
	public String getFamily() {
		return this_FAMILY;
	}

	@Override
	public String getStyle() {
		return "display:none";
	}

	@Override
	public void updateModel(FacesContext context) {
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