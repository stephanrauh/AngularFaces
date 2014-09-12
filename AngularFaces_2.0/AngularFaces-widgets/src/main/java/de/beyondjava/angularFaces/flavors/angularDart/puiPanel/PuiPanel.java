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
package de.beyondjava.angularFaces.flavors.angularDart.puiPanel;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesComponent("de.beyondjava.angularFaces.puiPanel.PuiPanel")
public class PuiPanel extends UIOutput {
	enum propertyKeys {
		collapsed, header, toggleable, toggleOrientation
	}

	public String getCollapsed() {
		return (String) getStateHelper().eval(propertyKeys.collapsed, null);
	}

	public String getToggleable() {
		return (String) getStateHelper().eval(propertyKeys.toggleable, null);
	}

	public String getToggleOrientation() {
		return (String) getStateHelper().eval(propertyKeys.toggleOrientation,
				null);
	}

	public void setCollapsed(String isCollapsed) {
		getStateHelper().put(propertyKeys.collapsed, isCollapsed);
	}

	public void setToggleable(String selected) {
		getStateHelper().put(propertyKeys.toggleable, selected);
	}

	public void setToggleOrientation(String toggleOrientation) {
		getStateHelper().put(propertyKeys.toggleOrientation, toggleOrientation);
	}

}
