/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.context;

import java.io.*;

import javax.faces.context.*;

import de.beyondjava.jsf.ajax.differentialContextWriter.PrettyPrintResponseWriter;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJExternalContextWrapper extends ExternalContextWrapper {

   private ExternalContext original;

   private Writer originalResponseWriter;
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
         responseWriter = new PrettyPrintResponseWriter(originalResponseWriter);
      }
      return responseWriter;
   }

   @Override
   public ExternalContext getWrapped() {
      return original;
   }
}
