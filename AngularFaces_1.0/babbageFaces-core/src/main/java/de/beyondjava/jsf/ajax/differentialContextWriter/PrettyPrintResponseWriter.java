/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsf.ajax.differentialContextWriter;

import java.io.*;
import java.util.logging.Logger;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class PrettyPrintResponseWriter extends Writer {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.PrettyPrintResponseWriter");

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
