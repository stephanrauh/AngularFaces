package de.beyondjava.jsf.sample.mixins;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class SilentBarker {
   public void bark() {
      String s = "Woof woof!";
      // prevent optimization
      s = s.substring(1).toLowerCase();
   }
}
