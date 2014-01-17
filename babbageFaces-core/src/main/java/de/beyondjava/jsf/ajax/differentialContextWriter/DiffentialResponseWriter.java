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

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

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

    private boolean containsHTMLTag = false;

    private long DEBUG_EndOfPageCalculation = 0l;
    private long DEBUG_timer = 0l;

    private long DEBUG_totalTimeStart = 0l;

    /** Is it an AJAX request or an HTML request? */
    boolean isAJAX = false;

    final boolean isDeveloperMode = FacesContext.getCurrentInstance().getApplication().getProjectStage() == ProjectStage.Development;

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
        DEBUG_timer = 0l;
        DEBUG_totalTimeStart = System.nanoTime();
        DEBUG_EndOfPageCalculation = 0l;
        containsHTMLTag = false;
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
        if (!containsHTMLTag) {
            containsHTMLTag |= s.equals("html");
        }
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

        if (containsHTMLTag) {
            if (s.contains("</html>")) {
                finished = true;
            }
        }
        else if (s.contains("</body>")) {
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
        long DEBUG_StartTime = System.nanoTime();
        long DEBUG_OptimizationTime = 0;
        boolean DEBUG_Finished = false;
        rawBuffer.append(cbuf, off, len);
        if (cbuf[off] == '\n') {
            off++;
            len--;
        }
        String s = new String(cbuf, off, len);
        final boolean endOfPageReached = endOfPage(s);
        DEBUG_EndOfPageCalculation += System.nanoTime() - DEBUG_StartTime;
        if (endOfPageReached) {
            DEBUG_Finished = true;
            if (rawbufferValid) {
                try {
                    DEBUG_OptimizationTime = System.nanoTime();
                    String optimizedResponse = new DiffenceEngine().yieldDifferences(rawBuffer.toString(), sessionMap,
                            isAJAX);
                    DEBUG_OptimizationTime = System.nanoTime() - DEBUG_OptimizationTime;
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
        if (isDeveloperMode) {
            long DEBUG_endTime = System.nanoTime();
            DEBUG_timer += (DEBUG_endTime - DEBUG_StartTime);
            if (DEBUG_Finished) {
                long total = (System.nanoTime() - DEBUG_totalTimeStart);
                LOGGER.info("Total rendering time:       " + ((total / 1000) / 1000.0) + " ms");
                LOGGER.info("  BabbageFaces Overhead:    " + ((DEBUG_timer / 1000) / 1000.0) + " ms");
                LOGGER.info("  BabbageFaces optimization: " + ((DEBUG_OptimizationTime / 1000) / 1000.0) + " ms");
                LOGGER.info("  BabbageFaces End-of-Page: " + ((DEBUG_EndOfPageCalculation / 1000) / 1000.0) + " ms");
            }
        }
    }
}
