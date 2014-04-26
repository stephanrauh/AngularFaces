package de.beyondjava.angularFaces.puiPanel;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiPanel.PuiPanel")
public class PuiPanelRenderer extends Renderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiPanel.PuiPanel");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiPanel' is available for use.");
    }

    public PuiPanelRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        PuiPanel tab = (PuiPanel) component;
        writer.startElement("pui-panel", component);
        renderNonEmptyAttribute(writer, "header", tab.getHeader());
        renderNonEmptyAttribute(writer, "collapsed", tab.getCollapsed());
        renderNonEmptyAttribute(writer, "toggleable", tab.getToggleable());
        renderNonEmptyAttribute(writer, "toggleOrientation", tab.getToggleOrientation());
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        super.encodeEnd(context, component);
        writer.endElement("pui-panel");
    }

};
