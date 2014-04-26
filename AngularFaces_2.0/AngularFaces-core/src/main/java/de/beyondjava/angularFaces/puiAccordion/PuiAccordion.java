package de.beyondjava.angularFaces.puiAccordion;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

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

    static {
        LOGGER.info("AngularFaces component 'PuiAccordion' is available for use.");
    }

    public PuiAccordion() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

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
