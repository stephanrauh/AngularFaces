/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter;

import java.io.*;

import javax.faces.context.*;

/**
 * This class isn't used currently, but it might be reactived soon.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class UnusedPrettyPrintResponseWriterWrapper extends ResponseWriterWrapper {

   ResponseWriter sunWriter;

   /**
    * @param writer
    */
   public UnusedPrettyPrintResponseWriterWrapper(ResponseWriter writer) {
      sunWriter = writer;
   }

   @Override
   public ResponseWriter cloneWithWriter(Writer writer) {
      System.out.println("cloneWithWriter PrettyPrintResponseWriter" + writer.getClass().getSimpleName());
      PrettyPrintResponseWriter prettyWriter = new PrettyPrintResponseWriter(writer);
      ResponseWriter sunResponseWriter = super.cloneWithWriter(prettyWriter);
      return new UnusedPrettyPrintResponseWriterWrapper(sunResponseWriter);

   }

   @Override
   public void endDocument() throws IOException {
      // TODO notify PrettyPrintResponseWriter here
      super.endDocument();
   }

   @Override
   public void endElement(String name) throws IOException {
      getWrapped().endElement(name);
   }

   @Override
   public ResponseWriter getWrapped() {
      return sunWriter;
   }
}
