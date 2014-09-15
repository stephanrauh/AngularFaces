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
package de.beyondjava.angularFaces.flavors.angularDart.puiAccordion;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIOutput;

/**
 * The pui-accordion component is a panel group that can be shrinked to a single line. Typically, there are multiple
 * accordions on a dialog, and expanding one of them hides the other ones.
 */
@FacesComponent("de.beyondjava.angularFaces.puiAccordion.PuiAccordion")
public class PuiAccordion extends UIOutput {
    enum propertyKeys {
        collapsed, header, toggleable, toggleOrientation
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiAccordion.PuiAccordion");

    public String getCollapsed() {
        return (String) getStateHelper().eval(propertyKeys.collapsed, null);
    }

    public String getHeader() {
        return (String) getStateHelper().eval(propertyKeys.header, null);
    }

    /**
     * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender
     * methods.
     */
    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
    }

    public String getToggleable() {
        return (String) getStateHelper().eval(propertyKeys.toggleable, null);
    }

    public String getToggleOrientation() {
        return (String) getStateHelper().eval(propertyKeys.toggleOrientation, null);
    }

    public void setCollapsed(String isCollapsed) {
        getStateHelper().put(propertyKeys.collapsed, isCollapsed);
    }

    public void setHeader(String closeable) {
        getStateHelper().put(propertyKeys.header, closeable);
    }

    public void setToggleable(String selected) {
        getStateHelper().put(propertyKeys.toggleable, selected);
    }

    public void setToggleOrientation(String toggleOrientation) {
        getStateHelper().put(propertyKeys.toggleOrientation, toggleOrientation);
    }

}
