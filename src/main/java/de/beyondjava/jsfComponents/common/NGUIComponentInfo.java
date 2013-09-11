/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.common;

/**
 * Stores informations about a particular UIComponent.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGUIComponentInfo {
   private String arrayIndex = null;

   private String clientID = null;

   private String prefix = null;

   /**
    * @param prefix2
    * @param arrayIndex2
    */
   public NGUIComponentInfo(String prefix, String arrayIndex, String clientID) {
      this.prefix = prefix;
      this.arrayIndex = arrayIndex;
      this.clientID = clientID;
   }

   public String getArrayIndex() {
      return arrayIndex;
   }

   public String getClientID() {
      return clientID;
   }

   public String getNGModel() {
      return clientID;
   }

   public String getPrefix() {
      return prefix;
   }

}
