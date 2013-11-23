/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter;

import java.io.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class PrettyPrintResponseWriter extends Writer {

   boolean almostFinished = false; // true if partial-response has been written,
                                   // but the trailing ">" hasn't been written
                                   // yet
   boolean finished = false;
   private int indent = 0;

   private StringBuffer prettyBuffer = new StringBuffer();
   private StringBuffer rawBuffer = new StringBuffer();

   private Writer sunWriter;

   /**
    * @param writer
    */
   public PrettyPrintResponseWriter(Writer writer) {
      sunWriter = writer;
   }

   @Override
   public void close() throws IOException {
      sunWriter.write(rawBuffer.toString());
      sunWriter.close();
      rawBuffer.setLength(0);
   }

   @Override
   public void flush() throws IOException {
      sunWriter.write(rawBuffer.toString());
      sunWriter.flush();
      rawBuffer.setLength(0);
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      if (cbuf[off] == '\n') {
         off++;
         len--;
      }
      String s = new String(cbuf, off, len);
      rawBuffer.append(s);
      while (s.contains("><")) {
         int pos = s.indexOf("><");
         String first = s.substring(0, pos + 1);
         s = s.substring(pos + 1);
         writeASingleTag(first);
      }
      writeASingleTag(s);
   }

   /**
    * @param s
    * @throws IOException
    */
   private void writeASingleTag(String s) throws IOException {
      if (s.startsWith("<")) {
         if (s.startsWith("</")) {
            indent--;
         }
         int fin = prettyBuffer.length() - 1;
         if (fin >= 0) {
            if (prettyBuffer.charAt(fin) != '\n') {
               prettyBuffer.append("\n");
            }
            for (int i = 0; i < indent; i++) {
               prettyBuffer.append("    ");
            }
         }
         if (s.startsWith("</") || s.startsWith("<?") || s.startsWith("<!")) {
            // do nothing
         }
         else if (!s.contains("/>")) {
            indent++;
         }
      }
      else if (s.contains("/>")) {
         indent--;
      }
      if (almostFinished) {
         finished = true;
      }

      int fin = prettyBuffer.length() - 1;
      if (fin >= 50) {
         if (s.contains("partial-response")) {
            almostFinished = true;
         }
         if (s.contains("</body>")) {
            finished = true;
         }
      }

      prettyBuffer.append(s);
      if (finished) {
         sunWriter.write(rawBuffer.toString());
         rawBuffer.setLength(0);
         sunWriter.append("\r\n<!--\r\n");

         sunWriter.write(prettyBuffer.toString());
         sunWriter.append("\r\n-->");
         prettyBuffer.setLength(0);
      }
   }
}
