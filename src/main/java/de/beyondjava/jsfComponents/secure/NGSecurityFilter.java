/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.secure;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public interface NGSecurityFilter {

   public boolean checkParameter(String key, String value);
}
