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

import org.xml.sax.SAXParseException;

import de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DifferenceEngine;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
public class DiffentialResponseWriter extends Writer {

    private static long DEBUG_timerCumulated = 0l;

    private static long DEBUG_totalTimeCumulated = 0l;

    /**
     * If the application runs on Apache MyFaces, we can detect the end of the HTML stream a lot simpler and faster than
     * on Mojarra
     */
    private static boolean isMyFaces = false;
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.DiffentialResponseWriter");
    static {
        try {
            Class.forName("org.apache.myfaces.application.ApplicationImpl");
            isMyFaces = true;
        }
        catch (ClassNotFoundException e) {
            isMyFaces = false; // activates Mojarra support
        }
    }
    /**
     * true if partial-response has been written, but the trailing ">" hasn't been written yet
     */
    boolean almostFinished = false;
    private boolean containsHTMLTag = false;
    boolean DEBUG_Finished = false;

    private long DEBUG_timer = 0l;

    private long DEBUG_totalTimeStart = 0l;

    private boolean disabledAfterError = false;

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

    /**
     * @param DEBUG_StartTime
     * @throws IOException
     */
    private void analyzeAndOptimizeResponse(long DEBUG_StartTime) throws IOException {
        long DEBUG_OptimizationTime;
        DEBUG_Finished = true;
        if (rawbufferValid) {
            try {
                DEBUG_OptimizationTime = System.nanoTime();
                String optimizedResponse = new DifferenceEngine().yieldDifferences(rawBuffer.toString(), sessionMap,
                        isAJAX);
                DEBUG_OptimizationTime = System.nanoTime() - DEBUG_OptimizationTime;
                long DEBUG_endTime = System.nanoTime();
                DEBUG_timer += (DEBUG_endTime - DEBUG_StartTime);
                long total = (System.nanoTime() - DEBUG_totalTimeStart);

                if (total < (500 * 1000 * 1000)) {
                    // we don't want to measure database access times
                    DEBUG_timerCumulated += DEBUG_timer;
                    DEBUG_totalTimeCumulated += total;
                }

                int pos = optimizedResponse.indexOf("<div id=\"babbageFacesStatistics\">");
                if (pos > 0 && DEBUG_totalTimeCumulated>0 && total>0) {
                    pos += "<div id=\"babbageFacesStatistics\">".length();
                    optimizedResponse = optimizedResponse.substring(0, pos) + "<br />" + "BabbageFaces 0.9 running on "
                            + (isMyFaces ? "Apache MyFaces" : "Oracle Mojarra") + "<br />" + "<table><tr>"
                            + "<td>Total rendering time:</td><td>" + ((total / 100000) / 10.0)
                            + " ms</td><td>Cumulated:</td><td> " + ((DEBUG_totalTimeCumulated / 10000) / 10.0)
                            + " ms</td></tr><tr>" + "<td>BabbageFaces overhead:</td><td>"
                            + ((DEBUG_timer / 100000) / 10.0) + " ms (" + ((100 * DEBUG_timer) / total)
                            + "%)</td><td>Cumulated: </td><td>" + ((DEBUG_timerCumulated / 10000) / 10.0) + " ms ("

                            + ((100 * DEBUG_timerCumulated) / DEBUG_totalTimeCumulated) + "%)</td></tr></table>"
                            + optimizedResponse.substring(pos);
                }
                sunWriter.write(optimizedResponse);
            }
            catch (Exception anyError) {
                if (!(anyError.getCause() instanceof SAXParseException)) // Error has already been reported
                {
                    LOGGER.severe("An error occured when optimizing the AJAX response. I'll use the original response instead.");
                    LOGGER.severe(anyError.toString());
                    anyError.printStackTrace();
                }
                sunWriter.write(rawBuffer.toString());
                disabledAfterError = true;
            }
        }
        else {
            sunWriter.write(rawBuffer.toString());
        }
        rawBuffer.setLength(0);
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
        if (isMyFaces) {
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
        analyzeAndOptimizeResponse(System.nanoTime());
        // rawbufferValid = false;
        // LOGGER.warning("DifferentialResponseWriter hasn't been designed to work with flush(). Returning to non-differential mode.");
        // sunWriter.write(rawBuffer.toString());
        // sunWriter.flush();
        // rawBuffer.setLength(0);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (DEBUG_Finished) {
            if (disabledAfterError) {
                sunWriter.write(cbuf, off, len);
                return;
            }

            LOGGER.severe("Unexpected use of DifferentialResponseWriter!");
        }
        long DEBUG_StartTime = System.nanoTime();
        rawBuffer.append(cbuf, off, len);
        if (cbuf[off] == '\n') {
            off++;
            len--;
        }
        String s = new String(cbuf, off, len);
        final boolean endOfPageReached = endOfPage(s);
        if (endOfPageReached) {
            analyzeAndOptimizeResponse(DEBUG_StartTime);
        }
        else {
            long DEBUG_endTime = System.nanoTime();
            DEBUG_timer += (DEBUG_endTime - DEBUG_StartTime);
        }
    }
}
