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

import java.util.*;

import javax.faces.context.FacesContext;

/**
 * Default implementation of a security filter to add a limited (but not
 * sufficient) level of security to your application. Note that you are still
 * responsible for your applications security. Using AngularFaces may help you
 * to secure your application, but it\"s not enough. AngularFaces and it\"s
 * author do not take any responsibilty for any security breach or any other
 * damage occuring using AngularFaces. Use at own risk.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGSecureUtilities {
   static NGSecurityFilter getCheckedBy() {
      return (NGSecurityFilter) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
            .get("de.beyondjava.Secure.checkedBy");
   }

   static List<java.lang.String> getSecurityToken() {
      return (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
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
      if (null != previousID) {
         sessionMap.remove("de.beyondjava.Secure.SecurityID");
      }

      sessionMap.put("de.beyondjava.Secure.SecurityID", id);

      if (!sessionMap.containsKey("de.beyondjava.Secure.securityToken")) {
         List<String> previousTokens = new ArrayList<String>();
         sessionMap.put("de.beyondjava.Secure.securityToken", previousTokens);
      }
      List<String> previousTokens = (List<String>) sessionMap.get("de.beyondjava.Secure.securityToken");
      while (previousTokens.size() > 20) {
         previousTokens.remove(0);
      }
      previousTokens.add(_token);
   }

}
