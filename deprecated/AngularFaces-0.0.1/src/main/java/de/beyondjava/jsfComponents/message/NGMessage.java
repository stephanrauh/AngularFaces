/*
 * Generated, Do Not Modify
 */
package de.beyondjava.jsfComponents.message;

import java.io.IOException;

import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css") })
@FacesComponent("de.beyondjava.Message")
public class NGMessage extends org.primefaces.component.message.Message {
   public static final String COMPONENT_FAMILY = "de.beyondjava.Message";

   private UIComponent target;

   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      new NGMessageRenderer().encodeBegin(context, this);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public UIComponent getTarget() {
      return target;
   }

   public void setTarget(UIComponent target) {
      this.target = target;
   }

}