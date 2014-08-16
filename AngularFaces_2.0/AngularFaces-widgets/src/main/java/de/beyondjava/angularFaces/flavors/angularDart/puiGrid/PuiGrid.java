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
package de.beyondjava.angularFaces.flavors.angularDart.puiGrid;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * &lt;pui-grid&gt; makes it a little easier to create simple but decently looking input dialogs. Typically it contains
 * a number of input fields that are automatically aligned to each other. More precisely, &lt;pui-grid&gt; creates a
 * column consisting of three columns. The first column is the label (which is automatically extracted from the
 * component), the second is the components itself and the third column is the field-specific error message.
 * Alternatively, the error message is placed below its field. Likewise, the label can be put above the input field.
 *
 * @ToDo &lt;pui-grid&gt; is limited to a single column of components
 * @ToDo put labels optionally above the component
 * @ToDo put error message optionally behind the component
 */
@FacesComponent("de.beyondjava.angularFaces.puiGrid.PuiGrid")
public class PuiGrid extends UIOutput {
    enum propertyKeys {
        columns, errorMessagePosition, labelPosition
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiGrid.PuiGrid");

    /**
     * How many columns of fields do you need? Please note the difference between a pui:grid column and an HTML column.
     * pui:grid columns consists of two or three HTML columns (the label, the component and optionally the error
     * message).
     */
    public int getColumns() {
        return (int) getStateHelper().eval(propertyKeys.columns, 1);
    }

    /**
     * Do you want to put the error message below or behind the field? Legal values: "below" and "behind". Default
     * value: "below".
     */
    public String getErrorMessagePosition() {
        return (String) getStateHelper().eval(propertyKeys.errorMessagePosition, "below");
    }

    /**
     * Do you want to put the label above or in front of the field? Legal values: "above" and "before". Default value:
     * "before".
     */
    public String getLabelPosition() {
        return (String) getStateHelper().eval(propertyKeys.labelPosition, "before");
    }

    /**
     * How many columns of fields do you need? Please note the difference between a pui:grid column and an HTML column.
     * pui:grid columns consists of two or three HTML columns (the label, the component and optionally the error
     * message).
     */
    public void setColumns(int columns) {
        getStateHelper().put(propertyKeys.columns, columns);
    }

    /**
     * Do you want to put the error message below or behind the field? Legal values: "below" and "behind". Default
     * value: "below".
     */
    public void setErrorMessage(String belowOrBehind) {
        getStateHelper().put(propertyKeys.errorMessagePosition, belowOrBehind);
    }

    /**
     * Do you want to put the label above or in front of the field? Legal values: "above" and "before". Default value:
     * "before".
     */
    public void setLabelPosition(String aboveOrBefore) {
        getStateHelper().put(propertyKeys.labelPosition, aboveOrBefore);
    }

}
