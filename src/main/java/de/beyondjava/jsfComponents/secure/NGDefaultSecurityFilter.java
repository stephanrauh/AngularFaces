/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.secure;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGDefaultSecurityFilter implements NGSecurityFilter {

   /**
    * 
    */
   @Override
   public boolean checkParameter(String key, String value) {
      if (value.contains("\'")) {
         return false;
      }
      return true;

   }

}
