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
    * returns true if the parameter seems to be ok.
    */
   @Override
   public boolean checkParameter(String key, String value) {
      if (null == value) {
         return true;
      }
      value = value.toLowerCase();
      if (value.contains("\'")) {
         return false;
      }
      if (value.contains("--")) {
         return false;
      }

      if (value.contains("drop ")) {
         return false;
      }
      if (value.contains("insert ")) {
         return false;
      }
      if (value.contains("select ")) {
         return false;
      }
      if (value.contains("delete ")) {
         return false;
      }
      return true;
   }

}
