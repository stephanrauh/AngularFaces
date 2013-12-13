/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.context;

import java.io.*;
import java.util.logging.Logger;

import javax.faces.context.*;

import de.beyondjava.jsf.ajax.differentialContextWriter.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJExternalContextWrapper extends ExternalContextWrapper {
   private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.ajax.context.BJExternalContextWrapper");

   private ExternalContext original;

   private Writer originalResponseWriter;
   private Writer prettyResponseWriter;
   private Writer responseWriter;

   /**
    * 
    */
   public BJExternalContextWrapper(ExternalContext original) {
      this.original = original;
   }

   @Override
   public Writer getResponseOutputWriter() throws IOException {
      if ((null == originalResponseWriter) || (originalResponseWriter != super.getResponseOutputWriter())) {
         originalResponseWriter = super.getResponseOutputWriter();
         prettyResponseWriter = new PrettyPrintResponseWriter(originalResponseWriter);
         responseWriter = new DiffentialResponseWriter(originalResponseWriter, getSessionMap());
      }
      return responseWriter;
   }

   @Override
   public ExternalContext getWrapped() {
      return original;
   }
}
