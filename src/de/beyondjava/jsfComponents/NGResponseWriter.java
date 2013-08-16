/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.faces.config.WebConfiguration.DisableUnicodeEscaping;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGResponseWriter extends HtmlResponseWriter {
   String ngPrefix = "";

   /**
    * @param writer
    * @param contentType
    * @param encoding
    * @param isScriptHidingEnabled
    * @param isScriptInAttributeValueEnabled
    * @param disableUnicodeEscaping
    * @param isPartial
    * @throws FacesException
    */
   public NGResponseWriter(Writer writer, String contentType, String encoding, Boolean isScriptHidingEnabled,
         Boolean isScriptInAttributeValueEnabled, DisableUnicodeEscaping disableUnicodeEscaping, boolean isPartial)
         throws FacesException {
      super(writer, contentType, encoding, isScriptHidingEnabled, isScriptInAttributeValueEnabled,
            disableUnicodeEscaping, isPartial);
   }

   /**
    * @param writer
    * @param contentType
    * @param encoding
    * @throws FacesException
    */
   public NGResponseWriter(Writer writer, String contentType, String encoding, String ngPrefix) throws FacesException {
      super(writer, contentType, encoding);
      if (null != ngPrefix) {
         this.ngPrefix = ngPrefix;
      }
   }

   @Override
   public Writer append(CharSequence csq) throws IOException {
      return super.append(csq);
   }

   @Override
   public void startElement(String arg0, UIComponent arg1) throws IOException {
      super.startElement(arg0, arg1);
   }

   @Override
   public String toString() {
      return super.toString();
   }

   @Override
   public void writeAttribute(String arg0, Object arg1, String arg2) throws IOException {
      // TODO Auto-generated method stub
      if ("ng-model".equals(arg0)) {
         super.writeAttribute(arg0, ngPrefix + "." + arg1, arg2);
      }
      else if (!"value".equals(arg0) && (!"id".equals(arg0)) && (!"selected".equals(arg0))) {
         super.writeAttribute(arg0, arg1, arg2);
      }
      else {
         System.out.println("Suppressed Value: " + arg1);
      }
   }
}
