package de.beyondjava.jsfComponents.secure;

import javax.faces.component.FacesComponent;

import org.primefaces.component.inputtext.InputText;

/**
 * This component analyzes user input to prevent security breaches.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.Secure")
public class NGSecure extends InputText {

   protected enum PropertyKeys {
      checkedBy, securityToken;

      String toString;

      PropertyKeys() {
      }

      PropertyKeys(String toString) {
         this.toString = toString;
      }

      @Override
      public String toString() {
         return ((this.toString != null) ? this.toString : super.toString());
      }
   }

   public static final String COMPONENT_FAMILY = "de.beyondjava.angularFaces";

   public java.lang.String getCheckedBy() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.checkedBy, null);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public java.lang.String getSecurityToken() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.securityToken, null);
   }

   /**
    * This component should always be invisible.
    */
   @Override
   public String getStyle() {
      return "display:none";
   }

   /**
    * This components value is the security token.
    */
   @Override
   public Object getValue() {
      return getSecurityToken();
   }

   public void setCheckedBy(java.lang.String _token) {
      getStateHelper().put(PropertyKeys.checkedBy, _token);
   }

   public void setSecurityToken(java.lang.String _token) {
      getStateHelper().put(PropertyKeys.securityToken, _token);
   }

}
