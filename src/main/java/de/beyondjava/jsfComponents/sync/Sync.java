package de.beyondjava.jsfComponents.sync;

import java.io.IOException;
import java.util.*;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

@FacesComponent("de.beyondjava.Sync")
public class Sync extends org.primefaces.component.inputtext.InputText {
   public static final String COMPONENT_FAMILY = "org.primefaces.component";

   /*
    * (non-Javadoc)
    * 
    * @see
    * javax.faces.component.UIInput#decode(javax.faces.context.FacesContext)
    */
   @Override
   public void decode(FacesContext context) {
      Map<String, String> params = context.getExternalContext().getRequestParameterMap();
      List<String> everyProperty = ELTools.getEveryProperty(ELTools.getCoreValueExpression(this), false);
      for (String property : everyProperty) {

         String id = (getClientId() + property).replace(".", "").replace(":", "");
         String value = params.get(id);
         setSubmittedValue(value);
      }
   }

   /**
    * Analyzes the value attributes, explodes it to a list of attributes and
    * adds a hidden input text for each bean attribute.
    */
   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      Application app = FacesContext.getCurrentInstance().getApplication();

      String rootProperty = ELTools.getCoreValueExpression(this);
      int beanNameIndex = rootProperty.lastIndexOf('.');
      List<String> everyProperty = ELTools.getEveryProperty(rootProperty, false);
      for (String property : everyProperty) {
         HtmlInputText sychronizingHiddenInput = (HtmlInputText) app.createComponent("javax.faces.HtmlInputText");
         ValueExpression valueExpression = ELTools.createValueExpression("#{" + property + "}");
         sychronizingHiddenInput.setValueExpression("value", valueExpression);
         sychronizingHiddenInput.setId((getClientId() + property).replace(".", "").replace(":", ""));
         sychronizingHiddenInput.getPassThroughAttributes(true).put("ng-model", property.substring(beanNameIndex + 1));
         sychronizingHiddenInput.getAttributes().put("name", property);
         sychronizingHiddenInput.encodeAll(context);
      }
   }

   private String getAttributeName() {
      String attributeName = ELTools.getCoreValueExpression(this);
      if (attributeName.contains(".")) {
         int pos = attributeName.lastIndexOf('.');
         attributeName = attributeName.substring(pos + 1);
      }
      return attributeName;
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   /**
    * Sync widgets are implements as invisible InputTexts.
    */
   @Override
   public String getType() {
      return "hidden";
   }

}
