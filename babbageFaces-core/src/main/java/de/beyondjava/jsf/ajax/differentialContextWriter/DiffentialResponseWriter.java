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

    private static long DEBUG_timerCumulated = 0l;

    private static long DEBUG_totalTimeCumulated = 0l;

    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.DiffentialResponseWriter");
    /**
     * true if partial-response has been written, but the trailing ">" hasn't been written yet
     */
    boolean almostFinished = false;
    private boolean containsHTMLTag = false;
    boolean DEBUG_Finished = false;
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
        DEBUG_timer = 0l;
        DEBUG_totalTimeStart = System.nanoTime();
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
        if ((!isAJAX) && (s.contains("partial-response"))) {
            isAJAX = true;
        }
        if (!isAJAX) {
            if (!containsHTMLTag) {
                int start = rawBuffer.length() - s.length() - "<html".length();
                if (rawBuffer.indexOf("<html", start) > 0) {
                    containsHTMLTag = true;
                }
            }
        }
        boolean finished = false;
        int fin = rawBuffer.length() - 1;
        if (almostFinished) {
            finished = true;
        }
        else if ((fin > 20) && (rawBuffer.charAt(fin - "partial-response".length()) == '/')
                && (rawBuffer.charAt(fin - "partial-response".length() - 1) == '<')) {
            if (s.contains("partial-response")) {
                if (rawBuffer.lastIndexOf("<![CDATA[") < rawBuffer.lastIndexOf("]]>")) {
                    almostFinished = true;
                    isAJAX = true;
                }
            }
        }

        if (!isAJAX) {
            if (containsHTMLTag) {
                int start = rawBuffer.length() - s.length() - "</html>".length();
                if (rawBuffer.indexOf("</html>", start) > 0) {
                    if (rawBuffer.lastIndexOf("<![CDATA[") > rawBuffer.lastIndexOf("]]>")) {
                        return false;
                    }
                    finished = true;
                }
            }
            else if (s.contains("</body>")) {
                if (rawBuffer.lastIndexOf("<![CDATA[") > rawBuffer.lastIndexOf("]]>")) {
                    return false;
                }
                finished = true;
            }
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
        if (DEBUG_Finished) {
            LOGGER.severe("Unexpected use of DifferentialResponseWriter!");
        }
        long DEBUG_StartTime = System.nanoTime();
        long DEBUG_OptimizationTime = 0;
        rawBuffer.append(cbuf, off, len);
        if (cbuf[off] == '\n') {
            off++;
            len--;
        }
        String s = new String(cbuf, off, len);
        final boolean endOfPageReached = endOfPage(s);
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

                if (total < (50 * 1000 * 1000)) {
                    // we don't want to measure database access times
                    DEBUG_timerCumulated += DEBUG_timer;
                    DEBUG_totalTimeCumulated += total;
                }

                LOGGER.info("Total rendering time:       " + ((total / 1000) / 1000.0) + " ms   Cumulated: "
                        + ((DEBUG_totalTimeCumulated / 1000) / 1000.0) + " ms");
                LOGGER.info("BabbageFaces Overhead:    " + ((DEBUG_timer / 1000) / 1000.0) + " ms   Cumulated: "
                        + ((DEBUG_timerCumulated / 1000) / 1000.0) + " ms");
                LOGGER.info("##################################################################################");

            }
        }
    }
}
