package de.beyondjava.jsfComponents.body;

import javax.faces.component.*;

import de.beyondjava.jsfComponents.SearchExpressions.SearchExpressionResolverFactoryExtension;

/**
 * This is an AngularJS-enabled html body tag.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.Body")
public class NGBody extends UIComponentBase {

   public static final String COMPONENT_FAMILY = "de.beyondjava.angularFaces.body";
   static {
      SearchExpressionResolverFactoryExtension.init();
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }
}
