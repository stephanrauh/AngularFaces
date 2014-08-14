package de.beyondjava.angularFaces.flavors.kendo.puiBody;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.html.HtmlBody;

import com.google.gson.Gson;

import de.beyondjava.angularFaces.common.IAngularController;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiBody.PuiBody")
public class PuiBody extends HtmlBody implements IAngularController {
	enum propertyKeys {
		publishAs, selector
	}

	private Map<String, Object> model = new HashMap<>();

	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiBody.PuiBody");

	/**
	 * This method is not as superfluous as it seems. We need it to be able to
	 * call getStateHelper() in defender methods.
	 */
	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	/** Builds basically a JSON structure from the JSF model. */
	public void addJSFAttrbituteToAngularModel(String key, Object value) {
		String[] keys = key.split("\\.");
		Map<String, Object> currentMap = model;
		for (int i = 0; i < keys.length - 1; i++) {
			if (!currentMap.containsKey(keys[i])) {
				currentMap.put(keys[i], new HashMap<String, Object>());
			}
			currentMap = (Map<String, Object>) currentMap.get(keys[i]);
		}
		String v = null;
		if (value != null) {
			v = value.toString();
		}
		currentMap.put(keys[keys.length - 1], v);
	}

	String getFacesModel() {
		Gson gs = new Gson();
		return gs.toJson(model);
	}
}
