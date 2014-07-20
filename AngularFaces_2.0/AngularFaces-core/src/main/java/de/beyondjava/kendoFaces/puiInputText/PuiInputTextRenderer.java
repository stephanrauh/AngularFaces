package de.beyondjava.kendoFaces.puiInputText;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import com.sun.faces.renderkit.html_basic.TextRenderer;

import de.beyondjava.angularFaces.core.JSR303RendererUtils;
import de.beyondjava.angularFaces.core.RendererUtils;
import de.beyondjava.angularFaces.puiInput.PuiInput;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.kendoFaces.puiInputText.PuiInputText")
public class PuiInputTextRenderer extends TextRenderer implements RendererUtils, JSR303RendererUtils {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiInputText.PuiInputText");

    static {
        LOGGER.info("KendoFaces renderer of 'PuiInputText' is available for use.");
    }

    public PuiInputTextRenderer() {
        LOGGER.info(getClass().getName() + " is being initialized");
    }

  
    protected String writeIdAttributeIfNecessary(FacesContext context,
            ResponseWriter writer,
            UIComponent component) {
    	try {
        renderMostCommonAttributes(writer, component);
        String type = ((PuiInputText)component).getTypeSpecificAttributes();
        if (null != type && type.length()>0) {
        	writer.writeAttribute(type, "", type);
        }
    	} catch (Exception e) {
    		LOGGER.log(Level.SEVERE, "TODO: An exception has been thrown." + e.getMessage(),e);
    	}
        return super.writeIdAttributeIfNecessary(context, writer, component);
    }

};
