/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.component.column.Column;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;

import de.beyondjava.jsfComponents.common.ELTools;
import de.beyondjava.jsfComponents.common.NGBeanAttributeInfo;
import de.beyondjava.jsfComponents.common.NGUIComponent;
import de.beyondjava.jsfComponents.common.NGUIComponentTools;
import de.beyondjava.jsfComponents.common.NGWordUtiltites;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.SelectBooleanCheckbox")
public class NGSelectBooleanCheckbox extends SelectBooleanCheckbox implements SystemEventListener, NGUIComponent {
   public static final String COMPONENT_FAMILY = "javax.faces.Input";

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn't find
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

   /**
    * if no label is provided by the XHTML file, try to guess it from the
    * ng-model attribute.
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

   @Override
   public boolean isListenerForSource(Object source) {
      return (source instanceof UIViewRoot);
   }

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
    * variable should never be needed, no doubt about it. Guess I didn't find
    * the best algorithm yet. :)
    */
   @Override
   public boolean preventRecursion() {
      return preventRecursion = true;
   }

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn't find
    * the best algorithm yet. :)
    */
   @Override
   public boolean preventRecursion(boolean reset) {
      return preventRecursion = false;
   }

   /**
    * Catching the PreRenderViewEvent allows AngularFaces to modify the JSF tree
    * by adding a label and a message.
    */
   @Override
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
