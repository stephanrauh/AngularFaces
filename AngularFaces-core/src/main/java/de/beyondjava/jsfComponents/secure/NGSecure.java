/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsfComponents.secure;

import java.util.List;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.primefaces.component.inputtext.InputText;

/**
 * This component analyzes user input to prevent security breaches. Note that
 * you are still responsible for your applications security. Using AngularFaces
 * may help you to secure your application, but it\"s not enough. AngularFaces
 * and it\"s author do not take any responsibilty for any security breach or any
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
      final List<String> tokens = NGSecureUtilities.getSecurityToken();
      return tokens.get(tokens.size() - 1);
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
