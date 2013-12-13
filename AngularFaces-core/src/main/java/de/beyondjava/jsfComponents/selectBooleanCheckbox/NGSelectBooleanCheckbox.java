/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.selectBooleanCheckbox;

import java.util.List;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.primefaces.component.column.Column;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;

import de.beyondjava.jsfComponents.common.*;
import de.beyondjava.jsfComponents.message.NGMessage;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.SelectBooleanCheckbox")
public class NGSelectBooleanCheckbox extends SelectBooleanCheckbox implements SystemEventListener, NGUIComponent {
   public static final String COMPONENT_FAMILY = "javax.faces.Input";

   /**
    * if the ID has not been set by the application, we define our own default
    * id (which equals the ngModel attribute)
    */
   private boolean isDefaultId = true;
   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn\"t find
    * the best algorithm yet. :)
    */
   private boolean preventRecursion = false;

   /**
    * This constructor subscribes to the PreRenderViewEvent. Catching the
    * PreRenderViewEvent allows AngularFaces to modify the JSF tree by adding a
    * label and a message.
    */
   public NGSelectBooleanCheckbox() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot root = context.getViewRoot();
      root.subscribeToViewEvent(PreRenderViewEvent.class, this);
   }

   @Override
   public String getClientId(FacesContext context) {
      if (preventRecursion) {
         return super.getClientId(context);
      }
      return NGUIComponentTools.getClientId(context, this);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getId() {
      if (isDefaultId) {
         setId(ELTools.getNGModel(this));
      }
      return super.getId();
   }

   /**
    * Try to guess the label from the ng-model attribute if the label attribute
    * has been omitted.
    */
   @Override
   public String getLabel() {
      String label = super.getLabel();
      if (null == label) {
         String ngModel;
         ngModel = ELTools.getNGModel(this);
         return NGWordUtiltites.labelFromCamelCase(ngModel);
      }
      return label;
   }

   /**
    * Adds code updating the AngularJS model when the checkbox is clicked.
    */
   @Override
   public String getOnchange() {
      String updateAngularModel = "updateAngularModel(\"showGlobeDemo\", this.checked);";
      // TODO Auto-generated method stub
      String original = super.getOnchange();
      if (null == original) {
         return updateAngularModel;
      }
      else {
         return updateAngularModel + original;
      }
   }

   /**
    * Add a label in front of the current component.
    */
   private void insertLabelBeforeThisInputField() {
      OutputLabel l = new OutputLabel();
      l.setFor(getId());
      l.setValue(getLabel());
      List<UIComponent> tree = getParent().getChildren();
      for (int i = 0; i < tree.size(); i++) {
         if (tree.get(i) == this) {
            tree.add(i, l);
            break;
         }
      }
   }

   /**
    * Add an error or hint message component behind the current component.
    */
   private void insertMessageBehindThisInputField() {
      NGMessage l = new NGMessage();
      l.setFor(getId());
      l.setDisplay("text");
      l.setTarget(this);
      List<UIComponent> tree = getParent().getChildren();
      for (int i = 0; i < tree.size(); i++) {
         if (tree.get(i) == this) {
            tree.add(i + 1, l);
            break;
         }
      }
   }

   public boolean isListenerForSource(Object source) {
      return (source instanceof UIViewRoot);
   }

   /**
    * If the bean attribute is annotated by NotNull, mark this component as
    * mandatory.
    */
   @Override
   public boolean isRequired() {
      NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(this);
      if (info.isRequired()) {
         return true;
      }
      return super.isRequired();
   }

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn\"t find
    * the best algorithm yet. :)
    */
   public boolean preventRecursion() {
      return preventRecursion = true;
   }

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn\"t find
    * the best algorithm yet. :)
    */
   public boolean preventRecursion(boolean reset) {
      return preventRecursion = false;
   }

   /**
    * Catching the PreRenderViewEvent allows AngularFaces to modify the JSF tree
    * by adding a label and a message.
    */
   public void processEvent(SystemEvent event) throws AbortProcessingException {
      if (!FacesContext.getCurrentInstance().isPostback()) {
         boolean tableMode = false;
         UIComponent parent = getParent();
         if ((parent instanceof Column)) {
            tableMode = true;
         }
         if (null != parent) {
            UIComponent grandpa = parent.getParent();
            if ((grandpa instanceof Column)) {
               tableMode = true;
            }

         }
         if (!tableMode) {
            insertLabelBeforeThisInputField();
            insertMessageBehindThisInputField();
         }
      }
   }
}
