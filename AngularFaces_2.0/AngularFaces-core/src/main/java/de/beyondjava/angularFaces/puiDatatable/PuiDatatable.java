package de.beyondjava.angularFaces.puiDatatable;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * PuiDatatable is looks like a PrimeFaces DataTable, only it's rendered on the client and it's got less features.
 */
@FacesComponent("de.beyondjava.angularFaces.puiDatatable.PuiDatatable")
public class PuiDatatable extends UIOutput {
    enum propertyKeys {
        initialsort, initialsortorder
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiDatatable.PuiDatatable");

    static {
        LOGGER.info("AngularFaces component 'PuiDatatable' is available for use.");
    }

    public PuiDatatable() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

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
