/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.secure;

import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Default implementation of a security filter to add a limited (but not
 * sufficient) level of security to your application. Note that you are still
 * responsible for your applications security. Using AngularFaces may help you
 * to secure your application, but it's not enough. AngularFaces and it's author
 * do not take any responsibilty for any security breach or any other damage
 * occuring using AngularFaces. Use at own risk.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGSecureUtilities {
   static NGSecurityFilter getCheckedBy() {
      return (NGSecurityFilter) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
            .get("de.beyondjava.Secure.checkedBy");
   }

   static java.lang.String getSecurityToken() {
      return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
            .get("de.beyondjava.Secure.securityToken");
   }

   static String getSecurityTokenFromRequest() {

      Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
      String previousID = (String) sessionMap.get("de.beyondjava.Secure.SecurityID");
      if (null != previousID) {
         return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(previousID);
      }
      return null;
   }

   static void setCheckedBy(NGSecurityFilter filter) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
            .put("de.beyondjava.Secure.checkedBy", filter);
   }

   static void setSecurityToken(java.lang.String _token, String id) {

      Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
      String previousID = (String) sessionMap.get("de.beyondjava.Secure.SecurityID");
      if (null == previousID) {
         sessionMap.put("de.beyondjava.Secure.SecurityID", id);
      }

      if (sessionMap.containsKey("de.beyondjava.Secure.securityToken")) {
         sessionMap.remove("de.beyondjava.Secure.securityToken");
      }
      sessionMap.put("de.beyondjava.Secure.securityToken", _token);
   }

}
