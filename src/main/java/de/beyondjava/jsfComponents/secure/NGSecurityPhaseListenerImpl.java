package de.beyondjava.jsfComponents.secure;

import java.io.IOException;
import java.util.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

/**
 * Default implementation of an AngularFaces security phase listener. Default
 * implementation of a security filter to add a limited (but not sufficient)
 * level of security to your application. Note that you are still responsible
 * for your applications security. Using AngularFaces may help you to secure
 * your application, but it's not enough. AngularFaces and it's author do not
 * take any responsibilty for any security breach or any other damage occuring
 * using AngularFaces. Use at own risk.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

public class NGSecurityPhaseListenerImpl implements PhaseListener, NGSecurityPhaseListener {

   private static final long serialVersionUID = -3898083464626663562L;

   @Override
   public void afterPhase(PhaseEvent pe) {
   }

   /**
    * {@inheritDoc}
    * <p>
    * Reports {@link javax.faces.event.PhaseId} value to {@code System.out}.
    * </p>
    */
   @Override
   public void beforePhase(PhaseEvent pe) {
      Map<String, String> parameterMap = pe.getFacesContext().getExternalContext().getRequestParameterMap();
      lookForUnexpectedParameters(parameterMap, pe.getFacesContext());
      examineParameters(parameterMap, pe.getFacesContext());
      System.out.println("Before Phase: " + pe.getPhaseId().toString() + " invoked.");
   }

   public void collectParameterList(UIComponent component, List<String> parameters) {
      if (null != component) {
         parameters.add(component.getClientId());
         System.out.println(component.getClass().getName() + " " + component.getClientId());
         for (UIComponent child : component.getChildren()) {
            collectParameterList(child, parameters);
         }

      }
   }

   public void examineParameters(Map<String, String> parameterMap, FacesContext context) {
      String token = NGSecureUtilities.getSecurityTokenFromRequest();
      String originalToken = NGSecureUtilities.getSecurityToken();
      if ((token == null) || (!(token.equals(originalToken)))) {
         try {
            context.getExternalContext().responseReset();
            context.getExternalContext().responseSendError(500, "internal error");
            context.getExternalContext().invalidateSession();
            throw new IllegalAccessError("Security breach - user tried to send a request twice");
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }

      NGSecurityFilter checkedBy = NGSecureUtilities.getCheckedBy();
      if (null != checkedBy) {
         for (String key : parameterMap.keySet()) {
            String value = parameterMap.get(key);
            if (!checkedBy.checkParameter(key, value)) {
               try {
                  context.getExternalContext().responseReset();
                  context.getExternalContext().responseSendError(500, "internal error");
                  throw new IllegalAccessError("Security breach - user input violated a filter rule");
               }
               catch (IOException e) {
               }
            }
         }
      }

   }

   /**
    * {@inheritDoc}
    * 
    * @return {@link javax.faces.event.PhaseId#ANY_PHASE}
    */
   @Override
   public PhaseId getPhaseId() {
      return PhaseId.APPLY_REQUEST_VALUES;
   }

   public void lookForUnexpectedParameters(Map<String, String> parameterMap, FacesContext context) {
      List<String> expectedParameters = new ArrayList<String>() {
         {
            add("javax.faces.ViewState");
            add("javax.faces.partial.ajax");
            add("javax.faces.partial.execute");
            add("javax.faces.source");
         }
      };
      UIComponent root = context.getViewRoot();
      collectParameterList(root, expectedParameters);
      Map<String, String> receivedParams = new HashMap<String, String>();
      receivedParams.putAll(parameterMap);
      for (String p : expectedParameters) {
         if (receivedParams.containsKey(p)) {
            receivedParams.remove(p);
         }
      }
      if (receivedParams.size() > 0) {
         try {
            context.getExternalContext().responseReset();
            context.getExternalContext().responseSendError(500, "internal error");
            throw new IllegalAccessError("Security breach - requests contains unexpected parameters");
         }
         catch (IOException e) {
         }

      }
   }
}
