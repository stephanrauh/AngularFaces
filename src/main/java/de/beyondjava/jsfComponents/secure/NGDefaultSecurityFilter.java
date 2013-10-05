/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.secure;

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
