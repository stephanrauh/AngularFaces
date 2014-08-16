package de.beyondjava.angularFaces.flavors.angularDart.puiDatatable;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * PuiDatatable is looks like a PrimeFaces DataTable, only it's rendered on the client. It's got less features, too.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.angularFaces.puiDatatable.PuiDatatable")
public class PuiDatatableRenderer extends Renderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiDatatableRenderer");

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        PuiDatatable d = (PuiDatatable) component;
        writer.startElement("pui-datatable", component);
        renderNonEmptyAttribute(writer, "value", d.getValue());
        renderNonEmptyAttribute(writer, "initialSort", d.getInitialSort());
        renderNonEmptyAttribute(writer, "initialSortOrder", d.getInitialSortOrder());
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("pui-datatable");
    }
};
