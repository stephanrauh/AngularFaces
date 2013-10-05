package de.beyondjava.jsfComponents.secure;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.primefaces.component.inputtext.InputText;

/**
 * This component analyzes user input to prevent security breaches. Note that
 * you are still responsible for your applications security. Using AngularFaces
 * may help you to secure your application, but it's not enough. AngularFaces
 * and it's author do not take any responsibilty for any security breach or any
 * other damage occuring using AngularFaces. Use at own risk.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.Secure")
public class NGSecure extends InputText implements SystemEventListener {

   public static final String COMPONENT_FAMILY = "de.beyondjava.angularFaces";

   public NGSecure() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot root = context.getViewRoot();
      root.subscribeToViewEvent(PreRenderViewEvent.class, this);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   /**
    * This component should always be invisible.
    */
   @Override
   public String getStyle() {
      return "display:none";
   }

   /**
    * This components value is the security token.
    */
   @Override
   public Object getValue() {
      return NGSecureUtilities.getSecurityToken();
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
      registerSecurityPhaseListener();
   }

   public void registerSecurityPhaseListener() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot viewRoot = context.getViewRoot();

      boolean alreadyRegistered = false;
      for (PhaseListener pl : viewRoot.getPhaseListeners()) {
         if (pl instanceof NGSecurityPhaseListener) {
            alreadyRegistered = true;
            break;
         }
      }
      if (!alreadyRegistered) {
         PhaseListener listener = new NGSecurityPhaseListenerImpl();
         viewRoot.addPhaseListener(listener);
      }
   }

}
