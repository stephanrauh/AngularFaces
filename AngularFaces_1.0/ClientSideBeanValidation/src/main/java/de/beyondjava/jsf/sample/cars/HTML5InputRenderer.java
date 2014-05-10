/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsf.sample.cars;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.*;
import javax.validation.constraints.*;
import javax.validation.metadata.*;

import org.primefaces.component.inputtext.InputTextRenderer;
import org.primefaces.context.RequestContext;
import org.primefaces.metadata.BeanValidationMetadataExtractor;

/**
 * Reads Bean Validation annotations to add HTML5 code to add spinners and min/max attributes to numeric input fields.
 * This class also converts input fields to date pickers if the bean attribute is a Date or a Calendar. Input fields
 * displaying an URL are converted to HTML5 URL input fields.
 *
 */
public class HTML5InputRenderer extends InputTextRenderer {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.sample.cars.HTML5InputRenderer");

    /**
     * Captures the rendering of the input field. Before the field is rendered, we verify the bean attribute trying to
     * make the HTML input field type attribute more specific.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        UIInput o = ((UIInput) component);
        if ("text".equals(o.getAttributes().get("type"))) {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            UIComponent comp = component;
            PropertyDescriptor propertyDescriptor = BeanValidationMetadataExtractor.extractPropertyDescriptor(context,
                    requestContext, comp.getValueExpression("value"));
            if (null != propertyDescriptor) {
                Class<?> type = propertyDescriptor.getElementClass();
                if ((type == int.class) || (type == Integer.class) || (type == long.class) || (type == Long.class)
                        || (type == short.class) || (type == Short.class) || (type == byte.class)
                        || (type == Byte.class) || (type == float.class) || (type == Float.class)
                        || (type == double.class) || (type == Double.class)) {
                    setSpecificType(o, "number");
                }
                else if (((type == Date.class) || (type == Calendar.class))) {
                    setSpecificType(o, "date");
                }
                else if (type == URL.class) {
                    setSpecificType(o, "url");
                }
            }
        }
        super.encodeBegin(context, component);
    }

    /**
     * Extracts attributes such as @Min and @Max from an attributes bean annotations and adds the corresponding HTML5
     * attributes.
     */
    @Override
    protected void renderValidationMetadata(FacesContext context, EditableValueHolder component) throws IOException {
        super.renderValidationMetadata(context, component);
        RequestContext requestContext = RequestContext.getCurrentInstance();
        if (requestContext.getApplicationContext().getConfig().isBeanValidationAvailable()) {
            ResponseWriter writer = context.getResponseWriter();
            UIComponent comp = (UIComponent) component;
            Set<ConstraintDescriptor<?>> desc = BeanValidationMetadataExtractor.extractConstraintDescriptors(context,
                    requestContext, comp.getValueExpression("value"));
            for (ConstraintDescriptor<?> d : desc) {
                Annotation anno = d.getAnnotation();
                if (anno instanceof Max) {
                    writer.writeAttribute("max", ((Max) anno).value(), "max");
                }
                else if (anno instanceof Min) {
                    writer.writeAttribute("min", ((Min) anno).value(), "min");
                }
            }
        }

    }

    /**
     * Replaces the original type of the input field by a more specific input type.
     *
     * @param o
     * @param typeOfInputTag
     */
    private void setSpecificType(UIInput o, final String typeOfInputTag) {
        if (o.getAttributes().containsKey("type")) {
            o.getAttributes().remove("type");
        }
        o.getAttributes().put("type", typeOfInputTag);
    }
}
