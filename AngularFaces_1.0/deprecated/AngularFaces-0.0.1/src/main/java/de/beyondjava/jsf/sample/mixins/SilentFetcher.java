package de.beyondjava.jsf.sample.mixins;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class SilentFetcher {
   public void fetch() {
      String s = "...got the stick!";
      // prevent optimization
      s = s.substring(1).toLowerCase();
   }

}
