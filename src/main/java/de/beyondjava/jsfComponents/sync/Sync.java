package de.beyondjava.jsfComponents.sync;

import java.io.IOException;

import javax.el.*;
import javax.faces.application.Application;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputHidden;
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

   /**
    * Analyzes the value attributes, explodes it to a list of attributes and
    * adds a hidden input text for each bean attribute.
    */
   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      Application app = FacesContext.getCurrentInstance().getApplication();

      ValueExpression valueExpression = getValueExpression("value");
      ELContext elContext = FacesContext.getCurrentInstance().getELContext();
      // Object value = valueExpression.getValue(elContext);
      // String v = valueExpression.getExpressionString();

      HtmlInputHidden grid = (HtmlInputHidden) app.createComponent("javax.faces.HtmlInputHidden");
      // HtmlInputText grid = (HtmlInputText)
      // app.createComponent("javax.faces.HtmlInputText");
      grid.setValueExpression("value", valueExpression);
      String attributeName = ELTools.getCoreValueExpression(this);
      if (attributeName.contains(".")) {
         int pos = attributeName.lastIndexOf('.');
         attributeName = attributeName.substring(pos + 1);
      }

      grid.getPassThroughAttributes(true).put("ng-model", attributeName);
      grid.encodeAll(context);
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
