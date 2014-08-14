package de.beyondjava.angularFaces.flavors.angularDart.puiPanel;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiPanel.PuiPanel")
public class PuiPanelRenderer extends Renderer implements RendererUtils {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        PuiPanel tab = (PuiPanel) component;
        writer.startElement("pui-panel", component);
        renderNonEmptyAttribute(writer, "header", tab.getAttributes().get("header"));
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
