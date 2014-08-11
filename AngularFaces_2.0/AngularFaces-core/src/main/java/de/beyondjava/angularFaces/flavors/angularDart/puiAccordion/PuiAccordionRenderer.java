package de.beyondjava.angularFaces.flavors.angularDart.puiAccordion;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * The pui-accordion component is a panel group that can be shrinked to a single line. Typically, there are multiple
 * accordions on a dialog, and expanding one of them hides the other ones.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiAccordion.PuiAccordion")
public class PuiAccordionRenderer extends Renderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiAccordion.PuiAccordion");

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        PuiAccordion tab = (PuiAccordion) component;
        writer.startElement("pui-accordion", component);
        renderNonEmptyAttribute(writer, "header", tab.getHeader());
        String collapsed = tab.getCollapsed();
        if (null == collapsed) {
            collapsed = "true";
        }
        renderNonEmptyAttribute(writer, "collapsed", collapsed);
        renderNonEmptyAttribute(writer, "toggleable", tab.getToggleable());
        renderNonEmptyAttribute(writer, "toggleOrientation", tab.getToggleOrientation());
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        super.encodeEnd(context, component);
        writer.endElement("pui-accordion");
    }

};
