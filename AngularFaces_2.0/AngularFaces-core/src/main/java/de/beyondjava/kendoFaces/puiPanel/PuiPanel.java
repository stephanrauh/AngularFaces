package de.beyondjava.kendoFaces.puiPanel;

import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiPanel.PuiPanel")
public class PuiPanel extends UIOutput  implements IStyle, IStyleClass {
    enum propertyKeys {
        collapsed, header, toggleable, toggleOrientation
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiPanel.PuiPanel");

    static {
        LOGGER.info("AngularFaces component 'PuiPanel' is available for use.");
    }

    public PuiPanel() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }
    /**
     * This method is not as superfluous as it seems. We need it to be able to call getStateHelper() in defender
     * methods.
     */
    @Override
    public StateHelper getStateHelper() {
        return super.getStateHelper();
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
