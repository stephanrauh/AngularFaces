/**
 * This class isn't used currently, but it might be reactived soon.
 * 
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.renderkit;

import java.io.Writer;

import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.RenderKitImpl;

import de.beyondjava.jsf.ajax.differentialContextWriter.UnusedPrettyPrintResponseWriterWrapper;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class UnusedBJRenderKitImpl extends RenderKitImpl {

   @Override
   public ResponseWriter createResponseWriter(Writer writer, String desiredContentTypeList, String characterEncoding) {
      ResponseWriter sunResponseWriter = super.createResponseWriter(writer, desiredContentTypeList, characterEncoding);
      UnusedPrettyPrintResponseWriterWrapper prettyWriter = new UnusedPrettyPrintResponseWriterWrapper(
            sunResponseWriter);
      return prettyWriter;
   }

}
