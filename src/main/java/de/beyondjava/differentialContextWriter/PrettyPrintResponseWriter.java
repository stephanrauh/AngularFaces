/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.differentialContextWriter;

import java.io.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class PrettyPrintResponseWriter extends Writer {

   private StringBuffer buffer = new StringBuffer();

   boolean finished = false;
   private int indent = 0;

   private Writer sunWriter;

   /**
    * @param writer
    */
   public PrettyPrintResponseWriter(Writer writer) {
      sunWriter = writer;
      System.out.println("Aquiring PrettyPrintResponseWriter" + writer.getClass().getSimpleName());
   }

   @Override
   public void close() throws IOException {
      sunWriter.write(buffer.toString());
      sunWriter.close();
      buffer.setLength(0);
   }

   @Override
   public void flush() throws IOException {
      sunWriter.write(buffer.toString());
      sunWriter.flush();
      buffer.setLength(0);
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      String s = new String(cbuf, off, len);
      if (s.startsWith("<")) {
         boolean reduceIndent = false;
         if (s.startsWith("</")) {
            reduceIndent = true;
         }
         else if (!s.contains("/>")) {
            indent++;
         }
         int fin = buffer.length() - 1;
         if (fin >= 0) {
            if (buffer.charAt(fin) != '\n') {
               buffer.append("\r\n");
            }
            for (int i = 0; i < indent; i++) {
               buffer.append("    ");
            }
         }
         if (reduceIndent) {
            indent--;
         }
      }
      else if (s.contains("/>")) {
         indent--;
      }

      int fin = buffer.length() - 1;
      if (fin >= 50) {
         if (s.contains("partial-response")) {
            finished = true;
         }
         if (s.contains("</body>")) {
            finished = true;
         }
      }

      buffer.append(s);
      if ((indent == 0) || finished) {
         sunWriter.write(buffer.toString());
         buffer.setLength(0);
      }
   }
}
