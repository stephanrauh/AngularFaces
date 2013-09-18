package de.beyondjava.jsfComponents.sync;

import java.io.IOException;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.*;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

@FacesComponent("de.beyondjava.Sync")
public class Sync extends org.primefaces.component.inputtext.InputText implements SystemEventListener {
   public static final String COMPONENT_FAMILY = "org.primefaces.component";

   /**
    * This constructor subscribes to the PreRenderViewEvent. Catching the
    * PreRenderViewEvent allows AngularFaces to modify the JSF tree by adding a
    * label and a message.
    */
   public Sync() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot root = context.getViewRoot();
      root.subscribeToViewEvent(PreRenderViewEvent.class, this);
   }

   @Override
   public void decode(FacesContext context) {
      // Map<String, String> params =
      // context.getExternalContext().getRequestParameterMap();
      // List<String> everyProperty =
      // ELTools.getEveryProperty(ELTools.getCoreValueExpression(this), false);
      // for (String property : everyProperty) {
      //
      // String id = (getClientId() + property).replace(".", "").replace(":",
      // "");
      // String value = params.get(id);
      // setSubmittedValue(value);
      // }
   }

   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      encodeChildren(context);
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

   @Override
   public boolean isListenerForSource(Object source) {
      return (source instanceof UIViewRoot);
   }

   /**
    * Catching the PreRenderViewEvent allows AngularFaces to modify the JSF tree
    * by adding a label and a message.
    */
   @Override
   public void processEvent(SystemEvent event) throws AbortProcessingException {
      FacesContext context = FacesContext.getCurrentInstance();
      if (!context.isPostback()) {
         Application app = context.getApplication();

         String rootProperty = ELTools.getCoreValueExpression(this);
         int beanNameIndex = rootProperty.lastIndexOf('.');
         List<String> everyProperty = ELTools.getEveryProperty(rootProperty, false);
         for (String property : everyProperty) {
            HtmlInputText sychronizingHiddenInput = (HtmlInputText) app.createComponent("javax.faces.HtmlInputText");
            ValueExpression valueExpression = ELTools.createValueExpression("#{" + property + "}");
            sychronizingHiddenInput.setValueExpression("value", valueExpression);
            sychronizingHiddenInput.setId((getClientId() + property).replace(".", "").replace(":", ""));
            sychronizingHiddenInput.getPassThroughAttributes(true).put("ng-model",
                  property.substring(beanNameIndex + 1));
            sychronizingHiddenInput.getAttributes().put("name", property);
            getChildren().add(sychronizingHiddenInput);
         }
      }
   }

}
