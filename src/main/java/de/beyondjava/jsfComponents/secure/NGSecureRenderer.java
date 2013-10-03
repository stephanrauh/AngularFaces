package de.beyondjava.jsfComponents.secure;

import java.io.IOException;
import java.util.Map;

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
      Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
      String token = parameterMap.get(component.getClientId());
      String originalToken = ((NGSecure) component).getSecurityToken();
      if ((token == null) || (!(token.equals(originalToken)))) {
         try {
            context.getExternalContext().responseReset();
            context.getExternalContext().responseSendError(500, "internal error");
            throw new IllegalAccessError("Security breach - user tried to send a request twice");
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }

      String checkedBy = ((NGSecure) component).getCheckedBy();
      NGSecurityFilter filter = new NGDefaultSecurityFilter();

      if (null != checkedBy) {
         try {
            Class<?> clazz = Class.forName(checkedBy);
            filter = (NGSecurityFilter) clazz.newInstance();
         }
         catch (ClassNotFoundException e) {
            // error is treated in encodeBegin()
         }
         catch (InstantiationException e) {
            // error is treated in encodeBegin()
         }
         catch (IllegalAccessException e) {
            // error is treated in encodeBegin()
         }
      }

      for (String key : parameterMap.keySet()) {
         String value = parameterMap.get(key);
         if (!filter.checkParameter(key, value)) {
            try {
               context.getExternalContext().responseReset();
               context.getExternalContext().responseSendError(500, "internal error");
               throw new IllegalAccessError("Security breach - user input violated a filter rule");
            }
            catch (IOException e) {
            }
         }
      }

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
      ((NGSecure) component).setSecurityToken(String.valueOf(token));
      super.encodeBegin(context, component);

      String checkedBy = ((NGSecure) component).getCheckedBy();
      NGSecurityFilter filter = new NGDefaultSecurityFilter();

      if (null != checkedBy) {
         try {
            Class<?> clazz = Class.forName(checkedBy);
            filter = (NGSecurityFilter) clazz.newInstance();
         }
         catch (ClassNotFoundException e) {
            context.getResponseWriter().append("<div>Security class filter not found</div>");
         }
         catch (InstantiationException e) {
            context.getResponseWriter().append("<div>Security class filter could not be instantiated</div>");
         }
         catch (IllegalAccessException e) {
            context.getResponseWriter()
                  .append("<div>Security class filter has been forbidden to be instantiated</div>");
         }
      }
      if (filter.getClass() == NGDefaultSecurityFilter.class) {
         context
               .getResponseWriter()
               .append(
                     "<div>This application is protected by AngularFaces default security. This may be better than no protection, but it must not be used in a production environment.</div>");
      }

   }
}
