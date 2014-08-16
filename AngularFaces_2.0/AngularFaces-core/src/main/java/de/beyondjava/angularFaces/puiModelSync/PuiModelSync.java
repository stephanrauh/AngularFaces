package de.beyondjava.angularFaces.puiModelSync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.google.gson.Gson;

import de.beyondjava.angularFaces.common.IAngularController;
import de.beyondjava.angularFaces.core.ELTools;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiBody.PuiBody")
public class PuiModelSync extends HtmlBody implements IAngularController {

	public PuiModelSync() {
		// TODO Auto-generated constructor stub
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
		System.out.println(jsfAttributes.size() + " in " + this);
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
		if ("".equals(valueToRender)) return null;
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
	 *  This method has been copied from the PrimeFaces 5 project (and been adapted after).
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
		String json = getFacesModel();
		boolean debugMode = context.isProjectStage(ProjectStage.Development);
		if (debugMode) {writer.append("\r\n");}
		writer.startElement("script", this);
		if (debugMode) {writer.append("\r\n");}
		writer.writeText("window.jsfScope=null;", null);
		if (debugMode) {writer.append("\r\n");}
		writer.writeText("function initJSFScope($scope){", null);
		if (debugMode) {writer.append("\r\n  ");}
		writer.writeText("window.jsfScope=$scope;", null);
		if (debugMode) {writer.append("\r\n  ");}
		writer.writeText("var jsf = " + json + ";", null);
		if (debugMode) {writer.append("\r\n  ");}
		writer.writeText("injectJSonIntoScope(jsf,$scope);", null);
		if (debugMode) {writer.append("\r\n");}
		writer.writeText("}", null);
		if (debugMode) {writer.append("\r\n");}
		writer.endElement("script");
		if (debugMode) {writer.append("\r\n");}

		String main = (String) getAttributes().get("mainclassfile");
		if (main == null) {
			main = "main.js";
		}
		if (!main.endsWith(".js")) {
			main = main + ".js";
		}
		writer.append("<script src='" + main + "'></script>");

		PuiScriptRenderer r = new PuiScriptRenderer();
		r.encodeScript(context, this, "glue.js", "AngularFaces");

		// writer.append("<script src=\"../resources/AngularFaces/glue.js\">\r\n</script>\r\n");
//		writer.append("\r\n");
//		writer.append("  <script>storeValues();</script>");
//		writer.append("\r\n");
	}
	
	@Override
	public void encodeChildren(FacesContext arg0) throws IOException {
	}
	
	@Override
	public void encodeEnd(FacesContext arg0) throws IOException {
	}
}
