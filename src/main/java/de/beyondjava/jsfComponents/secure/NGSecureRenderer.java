package de.beyondjava.jsfComponents.secure;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.inputtext.InputTextRenderer;

/**
 * This component analyzes user input to prevent security breaches.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = NGSecure.COMPONENT_FAMILY, rendererType = "de.beyondjava.Secure")
public class NGSecureRenderer extends InputTextRenderer {

   /**
    * This method analyzes user input to prevent security breaches.
    */
   @Override
   public void decode(FacesContext context, UIComponent component) {

      super.decode(context, component);
   }

   /**
    * Stores a unique token in the session to prevent repeated submission of the
    * same form.
    * */
   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      long timer = System.nanoTime();
      long random = (long) (Math.random() * Integer.MAX_VALUE);
      long token = timer ^ random;
      NGSecureUtilities.setSecurityToken(String.valueOf(token), component.getClientId());
      super.encodeBegin(context, component);

      NGSecurityFilter filter = findAndVerifySecurityFilter(context, component);
      showLegalDisclaimer(context, filter);
   }

   private NGSecurityFilter findAndVerifySecurityFilter(FacesContext context, UIComponent component) throws IOException {
      String checkedBy = (String) component.getAttributes().get("checkedBy");
      NGSecurityFilter filter = new NGDefaultSecurityFilter();

      if (null != checkedBy) {
         try {
            Class<?> clazz = Class.forName(checkedBy);
            filter = (NGSecurityFilter) clazz.newInstance();
         }
         catch (ClassNotFoundException e) {
            context.getResponseWriter().append("<div style='color:#F00'>Configuration error: security class filter not found</div>");
         }
         catch (InstantiationException e) {
            context.getResponseWriter().append(
                  "<div style='color:#F00'>Configuration error: security class filter could not be instantiated</div>");
         }
         catch (IllegalAccessException e) {
            context.getResponseWriter().append(
                  "<div style='color:#F00'>Configuration error: security class filter has been forbidden to be instantiated</div>");
         }
         NGSecureUtilities.setCheckedBy(filter);
      }
      return filter;
   }

   private void showLegalDisclaimer(FacesContext context, NGSecurityFilter filter) throws IOException {
      if (filter.getClass() == NGDefaultSecurityFilter.class) {
         context
               .getResponseWriter()
               .append(
                     "<div style='color:#F00'>Warning: This application is only protected by AngularFaces default security.<br /> This may be better than no protection, but it must not be used in a production environment.</div>");
      }
   }
}
