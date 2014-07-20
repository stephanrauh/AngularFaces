package de.beyondjava.kendoFaces.puiSlider;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.kendoFaces.puiSlider.PuiSlider")
public class PuiSliderRenderer extends BodyRenderer implements RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiButton.PuiSliderRenderer");

    static {
        LOGGER.info("KendoFaces renderer of 'PuiSlider' is available for use.");
    }

    public PuiSliderRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        // super.encodeBegin(context, component);
    	PuiSlider slider = (PuiSlider) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.append("\r\n");
        writer.startElement("span", component);

        writer.startElement("div", component);
        writer.writeAttribute("kendo-slider", "", null);
        renderMostCommonAttributes(writer, component);

        String orientation = (String) slider.getOrientation();
        if ((null != orientation) && (!orientation.equalsIgnoreCase("horizontal"))) {
            writer.writeAttribute("k-orientation", "'"+orientation+"'", null);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        writer.endElement("span");
        writer.append("\r\n");
    }
};
