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
package de.beyondjava.angularFaces.core;

import java.util.Map;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

/**
 * Access to variables to shared among various renderer and component objects.
 */
public class SessionUtils {
	private final static String DART_CONTROLLER = "de.beyondjava.angularFaces.generateDartController";
	private final static String DART_CONTROLLER_NAME = "de.beyondjava.angularFaces.dartControllerName";

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsfComponents.core.SessionUtils");

	/**
	 * Called when the Dart attribute is active in &lt;a:body&gt;. After activating Dart AngularDart is supported instead of AngularJS.
	 */
	public static void activateDartController() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(DART_CONTROLLER, "true");
	}

	/**
	 * Called at the end of &lt;a:body&gt; to turn of the AngularDart mode.
	 */
	public static void deactivateDartController() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(DART_CONTROLLER);
	}

	/**
	 * @return Yields the name of the Dart controller name as defined in the dart file (@NGController's publishAs attribute).
	 * 
	 */
	public static String getControllerName() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (sessionMap.containsKey(DART_CONTROLLER_NAME)) {
			return (String) sessionMap.get(DART_CONTROLLER_NAME);
		}
		return null;
	}

	/**
	 * Are we to support AngularDart instead of AngularJS?
	 * @return true if AngularDart is used
	 */
	public static boolean isDartControllerActive() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (sessionMap.containsKey(DART_CONTROLLER)) {
			return "true".equals(sessionMap.get(DART_CONTROLLER));
		}
		return false;
	}

	/**
	 * Sets the name of the Dart controller name as defined in the dart file (@NGController's publishAs attribute).
	 * @param name the name of the Dart controller
	 */
	public static void setControllerName(String name) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put(DART_CONTROLLER_NAME, name);
	}

}
