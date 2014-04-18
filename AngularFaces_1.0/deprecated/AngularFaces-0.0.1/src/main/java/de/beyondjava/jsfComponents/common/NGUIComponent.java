/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.common;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public interface NGUIComponent {
   public boolean preventRecursion();

   public boolean preventRecursion(boolean reenable);
}
