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
import java.util.Map;
import java.util.logging.Logger;

import de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffentialResponseWriter extends Writer {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.DiffentialResponseWriter");

    /**
     * true if partial-response has been written, but the trailing ">" hasn't been written yet
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
    public DiffentialResponseWriter(Writer writer, Map<String, Object> sessionMap) {
        sunWriter = writer;
        this.sessionMap = sessionMap;
        System.out.println("##### Initializing BabbageFaces DifferentialResponseWriter ##### ");
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
                try {
                    String optimizedResponse = new DiffenceEngine().yieldDifferences(rawBuffer.toString(), sessionMap,
                            isAJAX);
                    sunWriter.write(optimizedResponse);
                }
                catch (Exception anyError) {
                    LOGGER.severe("An error occured when optimizing the AJAX response. I'll use the original response instead.");
                    LOGGER.severe(anyError.toString());
                    anyError.printStackTrace();
                    sunWriter.write(rawBuffer.toString());
                }
            }
            else {
                sunWriter.write(rawBuffer.toString());
            }
            rawBuffer.setLength(0);
        }
    }
}
