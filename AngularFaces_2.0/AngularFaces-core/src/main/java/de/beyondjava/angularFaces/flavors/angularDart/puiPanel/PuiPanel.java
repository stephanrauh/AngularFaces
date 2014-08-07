package de.beyondjava.angularFaces.flavors.angularDart.puiPanel;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesComponent("de.beyondjava.angularFaces.puiPanel.PuiPanel")
public class PuiPanel extends UIOutput {
    enum propertyKeys {
        collapsed, header, toggleable, toggleOrientation
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiPanel.PuiPanel");

    static {
        LOGGER.info("AngularFaces component 'PuiPanel' is available for use.");
    }

    public PuiPanel() {
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
