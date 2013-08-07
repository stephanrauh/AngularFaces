package de.beyondjava.jsfComponents;

import java.io.IOException;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.component.outputlabel.OutputLabel;

import de.beyondjava.jsfComponents.common.ELTools;
import de.beyondjava.jsfComponents.common.NGBeanAttributeInfo;
import de.beyondjava.jsfComponents.common.NGUIComponent;
import de.beyondjava.jsfComponents.common.NGUIComponentTools;
import de.beyondjava.jsfComponents.common.NGWordUtiltites;

@FacesComponent("de.beyondjava.InputText")
public class NGInputText extends org.primefaces.component.inputtext.InputText implements SystemEventListener,
      NGUIComponent {
   public static final String COMPONENT_FAMILY = "javax.faces.Input";

   /** Prevents endless loop during calls from NGUIComponentTools */
   private boolean preventRecursion = false;

   public NGInputText() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot root = context.getViewRoot();
      root.subscribeToViewEvent(PreRenderViewEvent.class, this);
   }

   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      if ("text".equals(getType())) {
         Class<?> type = ELTools.getType(this);
         if ((int.class == type) || (Integer.class == type) || (long.class == type) || (Long.class == type)
               || (double.class == type) || (Double.class == type)) {
            setType("number");
         }
      }
      super.encodeBegin(context);
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

   @Override
   public int getMaxlength() {
      int maxlength = super.getMaxlength();
      if (maxlength <= 0) {
         NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(this);
         maxlength = (int) info.getMaxSize();
      }
      return maxlength;
   }

   @Override
   public int getSize() {
      int size = super.getSize();
      if (size <= 0) {
         NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(this);
         size = (int) info.getMaxSize();
      }
      return super.getSize();
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

   /** Prevents endless loop during calls from NGUIComponentTools */
   @Override
   public boolean preventRecursion() {
      return preventRecursion = true;
   }

   /** Stops preventing endless loop during calls from NGUIComponentTools */
   @Override
   public boolean preventRecursion(boolean reset) {
      return preventRecursion = false;
   }

   @Override
   public void processEvent(SystemEvent event) throws AbortProcessingException {
      if (!FacesContext.getCurrentInstance().isPostback()) {
         insertLabelBeforeThisInputField();
         insertMessageBehindThisInputField();
      }
   }
}
