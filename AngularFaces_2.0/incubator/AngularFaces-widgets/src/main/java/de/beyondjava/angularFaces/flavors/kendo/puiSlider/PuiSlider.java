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
package de.beyondjava.angularFaces.flavors.kendo.puiSlider;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.IModel;
import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiSlider.PuiSlider")
public class PuiSlider extends UIInput implements IModel, IStyle, IStyleClass {
	enum propertyKeys {
		orientation
	}

	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiSlider.PuiSlider");

	@Override
	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	public String getOrientation() {
		return (String) getStateHelper().eval(propertyKeys.orientation, null);
	}

	public void setOrientation(String isOrientation) {
		getStateHelper().put(propertyKeys.orientation, isOrientation);
	}

}
