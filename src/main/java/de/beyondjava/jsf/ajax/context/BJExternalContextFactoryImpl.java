/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.context;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;

import com.sun.faces.context.ExternalContextFactoryImpl;

import de.beyondjava.jsf.ajax.context.BJExternalContextWrapper;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJExternalContextFactoryImpl extends ExternalContextFactoryImpl {

   public BJExternalContextFactoryImpl() {
   }

   @Override
   public ExternalContext getExternalContext(Object servletContext, Object request, Object response)

   throws FacesException {
      ExternalContext ctx = super.getExternalContext(servletContext, request, response);
      return new BJExternalContextWrapper(ctx);

   }

}
