/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

import de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class XMLDiffentialResponseWriter extends Writer {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.DiffentialResponseWriter");

   /**
    * true if partial-response has been written, but the trailing ">" hasn't
    * been written yet
    */
   boolean almostFinished = false;

   /** Is it an AJAX request or an HTML request? */
   boolean isAJAX = false;

   private StringBuffer rawBuffer = new StringBuffer();

   private boolean rawbufferValid = true;

   private Map<String, Object> sessionMap;

   private Writer sunWriter;

   /**
    * @param writer
    * @param sessionMap
    */
   public XMLDiffentialResponseWriter(Writer writer, Map<String, Object> sessionMap) {
      sunWriter = writer;
      this.sessionMap = sessionMap;
   }

   @Override
   public void close() throws IOException {
      sunWriter.write(rawBuffer.toString());
      sunWriter.close();
      rawBuffer.setLength(0);
   }

   /**
    * @param s
    * @throws IOException
    */
   private boolean endOfPage(String s) {
      if (rawBuffer.lastIndexOf("<![CDATA[") > rawBuffer.lastIndexOf("]]>")) {
         return false;
      }
      boolean finished = false;
      int fin = rawBuffer.length() - 1;
      if (almostFinished) {
         finished = true;
      }
      else if ((fin > 20) && (rawBuffer.charAt(fin - "partial-response".length()) == '/')
            && (rawBuffer.charAt(fin - "partial-response".length() - 1) == '<')) {
         if (s.contains("partial-response")) {
            almostFinished = true;
            isAJAX = true;
         }
      }
      if (s.contains("</body>")) {
         finished = true;
      }

      return finished;
   }

   @Override
   public void flush() throws IOException {
      rawbufferValid = false;
      LOGGER.warning("DifferentialResponseWriter hasn't been designed to work with flush(). Returning to non-differential mode.");
      sunWriter.write(rawBuffer.toString());
      sunWriter.flush();
      rawBuffer.setLength(0);
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      if (cbuf[off] == '\n') {
         off++;
         len--;
         rawBuffer.append('\n');
      }
      String s = new String(cbuf, off, len);
      rawBuffer.append(s);
      if (endOfPage(s)) {
         if (rawbufferValid) {
            String difference = new DiffenceEngine().yieldDifferences(rawBuffer.toString(), sessionMap, isAJAX);
            sunWriter.write(difference);
         }
         else {
            sunWriter.write(rawBuffer.toString());
         }
         rawBuffer.setLength(0);
      }
   }
}
