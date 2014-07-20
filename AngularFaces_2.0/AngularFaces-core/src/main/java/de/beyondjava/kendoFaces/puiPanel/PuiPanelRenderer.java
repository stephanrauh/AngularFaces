package de.beyondjava.kendoFaces.puiPanel;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiPanel.PuiPanel")
public class PuiPanelRenderer extends Renderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiPanel.PuiPanel");

    static {
        LOGGER.info("AngularFaces renderer of 'PuiPanel' is available for use.");
    }

    public PuiPanelRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        //super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        PuiPanel tab = (PuiPanel) component;
        writer.startElement("ul", component);
        writer.writeAttribute("kendo-panel-bar", "", null);
        renderMostCommonAttributes(writer, component);
        writer.startElement("li", component);
        writer.append(tab.getHeader());
//        renderNonEmptyAttribute(writer, "header", tab.getHeader());
        writer.startElement("div", component);
//        encodeChildren(context, component);
//        renderNonEmptyAttribute(writer, "collapsed", tab.getCollapsed());
//        renderNonEmptyAttribute(writer, "toggleable", tab.getToggleable());
//        renderNonEmptyAttribute(writer, "toggleOrientation", tab.getToggleOrientation());
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        super.encodeEnd(context, component);
        writer.endElement("div");
        writer.endElement("li");
        writer.endElement("ul");
        writer.append("\r\n");
    }

};
