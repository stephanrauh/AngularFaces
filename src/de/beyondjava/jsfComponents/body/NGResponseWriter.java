/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.body;

import java.io.Writer;

import javax.faces.FacesException;

import de.beyondjava.jsfComponents.dataTable.NGDataTableResponseWriter;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGResponseWriter extends NGDataTableResponseWriter {

   /**
    * @param writer
    * @param contentType
    * @param encoding
    * @throws FacesException
    */
   public NGResponseWriter(Writer writer, String contentType, String encoding) throws FacesException {
      super(writer, contentType, encoding, null);
   }

}
