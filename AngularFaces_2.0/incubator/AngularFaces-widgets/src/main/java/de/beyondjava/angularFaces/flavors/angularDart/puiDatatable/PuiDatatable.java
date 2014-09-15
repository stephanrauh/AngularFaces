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
package de.beyondjava.angularFaces.flavors.angularDart.puiDatatable;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIOutput;

/**
 * PuiDatatable is looks like a PrimeFaces DataTable, only it's rendered on the client and it's got less features.
 */
@FacesComponent("de.beyondjava.angularFaces.puiDatatable.PuiDatatable")
public class PuiDatatable extends UIOutput {
    enum propertyKeys {
        initialsort, initialsortorder
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiDatatable.PuiDatatable");

    public String getInitialSort() {
        return (String) getStateHelper().eval("initialsort", null);
    }

    public String getInitialSortOrder() {
        return (String) getStateHelper().eval("initialsortorder", null);
    }

    /**
     * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender
     * methods.
     */
    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
    }

    public void setInitialSort(String initialSortColumn) {
        getStateHelper().put("initialsort", initialSortColumn);
    }

    public void setInitialSortOrder(String initialSortOrder) {
        getStateHelper().put("initialsortorder", initialSortOrder);
    }

}
