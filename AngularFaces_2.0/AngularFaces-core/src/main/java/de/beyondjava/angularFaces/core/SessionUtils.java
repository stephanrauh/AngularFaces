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
	 */
	public static void setControllerName(String name) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put(DART_CONTROLLER_NAME, name);
	}

}
