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
