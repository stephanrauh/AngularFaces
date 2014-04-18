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
package de.beyondjava.jsf.ajax.context;

import java.io.*;
import java.util.logging.Logger;

import javax.faces.context.*;

import de.beyondjava.jsf.ajax.differentialContextWriter.DiffentialResponseWriter;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJExternalContextWrapper extends ExternalContextWrapper {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.ajax.context.BJExternalContextWrapper");

    private ExternalContext original;

    private Writer originalResponseWriter;
    private Writer responseWriter;

    /**
    * 
    */
    public BJExternalContextWrapper(ExternalContext original) {
        this.original = original;
    }

    @Override
    public Writer getResponseOutputWriter() throws IOException {
        if ((null == originalResponseWriter) || (originalResponseWriter != super.getResponseOutputWriter())) {
            originalResponseWriter = super.getResponseOutputWriter();
            responseWriter = new DiffentialResponseWriter(originalResponseWriter, getSessionMap());
        }
        return responseWriter;
    }

    @Override
    public ExternalContext getWrapped() {
        return original;
    }

    @Override
    public void invalidateSession() {
        LOGGER.info("Invalidate Session");
        originalResponseWriter = null;
        responseWriter = null;
        super.invalidateSession();
    }
}
