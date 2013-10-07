package de.beyondjava.jsfComponents.secure;

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

   @Override
   public void beforePhase(PhaseEvent pe) {
      Map<String, String> parameterMap = pe.getFacesContext().getExternalContext().getRequestParameterMap();
      lookForUnexpectedParameters(parameterMap, pe.getFacesContext());
      examineParameters(parameterMap, pe.getFacesContext());
   }

   public void collectParameterList(UIComponent component, List<String> parameters) {
      if (null != component) {
         parameters.add(component.getClientId());
         for (UIComponent child : component.getChildren()) {
            collectParameterList(child, parameters);
         }
      }
   }

   public void examineParameters(Map<String, String> parameterMap, FacesContext context) {
      String token = NGSecureUtilities.getSecurityTokenFromRequest();
      List<String> originalToken = NGSecureUtilities.getSecurityToken();
      if ((token == null) || (!(originalToken.contains(token)))) {
         throw new IllegalAccessError(
               "Security breach - user tried to forge a request twice, or hit the back button twenty times");
      }

      NGSecurityFilter checkedBy = NGSecureUtilities.getCheckedBy();
      if (null != checkedBy) {
         for (String key : parameterMap.keySet()) {
            String value = parameterMap.get(key);
            if (!checkedBy.checkParameter(key, value)) {
               throw new IllegalAccessError("Security breach - user input violated a filter rule");
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
            add("javax.faces.partial.render");
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
         throw new IllegalAccessError("Security breach - requests contains unexpected parameters");
      }
   }
}
