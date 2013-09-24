package de.beyondjava.jsfComponents.sync;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;
import javax.validation.ValidationException;

import org.apache.commons.beanutils.BeanUtils;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.Sync")
public class SyncRenderer extends org.primefaces.component.inputtext.InputTextRenderer {

   private final String SEPARATOR = "³³³";

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.primefaces.component.inputtext.InputTextRenderer#decode(javax.faces
    * .context.FacesContext, javax.faces.component.UIComponent)
    */
   @Override
   public void decode(FacesContext context, UIComponent component) {
      String direction = (String) component.getAttributes().get("direction");
      if ((null == direction) || "both".equals(direction) || "clientToServer".equals(direction)) {

         String rootProperty = ELTools.getCoreValueExpression(component);

         Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
         String json = parameterMap.get(component.getClientId());
         String[] assignments = json.split(SEPARATOR);
         List<String> everyProperty = ELTools.getEveryProperty(rootProperty, false);
         Object rootBean = ELTools.evalAsObject("#{" + rootProperty + "}");
         int index = 0;
         for (String property : everyProperty) {
            int pos = assignments[index].indexOf("=");
            String valueAsString = assignments[index].substring(pos + 1);
            String clientProperty = assignments[index].substring(0, pos);
            if (!clientProperty.equals(property)) {
               throw new ValidationException("Hacking detected");
            }
            try {
               BeanUtils.setProperty(rootBean, property, valueAsString);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               throw new ValidationException("Couldn't assign property " + property);
            }
            index++;
         }

      }
   }

   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      ResponseWriter writer = context.getResponseWriter();
      writer.append("\r\n\r\n\r\n");
      String direction = (String) component.getAttributes().get("direction");
      if ((null == direction) || "serverToClient".equals(direction) || "both".equals(direction)) {
         writer.append("<script type='text/javascript>\r\n");
         writer.append("function getModelValuesFromServer()\r\n {\r\n");
         String rootProperty = ELTools.getCoreValueExpression(component);
         List<String> everyProperty = ELTools.getEveryProperty(rootProperty, false);
         for (String property : everyProperty) {
            String value = ELTools.evalAsString("#{" + rootProperty + "." + property + "}");
            String line = "   injectVariableIntoScope('" + property + "', '" + value + "');\r\n";
            writer.append(line);
         }
         writer.append("}\r\n");
         writer.append("</script>\r\n");
      }
      writer.append("\r\n\r\n\r\n");
      if ((null == direction) || "both".equals(direction) || "clientToServer".equals(direction)) {

         String rootProperty = ELTools.getCoreValueExpression(component);
         List<String> everyProperty = ELTools.getEveryProperty(rootProperty, false);
         String assignments = "";
         for (String property : everyProperty) {
            String line = property + "={{" + property + "}}";
            if (assignments.length() > 0) {
               assignments += SEPARATOR;
            }
            assignments += line;
         }
         ELTools.getArrayProperties(rootProperty, false);

         ValueExpression ve = ELTools.createValueExpression(assignments);
         component.setValueExpression("value", ve);
         super.encodeBegin(context, component);
      }
      writer.append("\r\n\r\n\r\n");
   }

}
