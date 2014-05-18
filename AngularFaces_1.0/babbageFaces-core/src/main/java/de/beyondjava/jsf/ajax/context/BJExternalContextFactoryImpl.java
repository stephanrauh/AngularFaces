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

import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net with some help from
 *         http://blogs.steeplesoft.com/posts/2010/05/04/putting-facelets-in-a-jar/
 *
 */
public class BJExternalContextFactoryImpl extends ExternalContextFactory {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.context.BJExternalContextFactoryImpl");

    static {
        LOGGER.info("Running on BabbageFaces 0.9");
    }

    private ExternalContextFactory parent;

    public BJExternalContextFactoryImpl(ExternalContextFactory parent) {
        super();
        this.parent = parent;
    }

    @Override
    public ExternalContext getExternalContext(Object servletContext, Object request, Object response)
            throws FacesException {
        ExternalContext ctx = getWrapped().getExternalContext(servletContext, request, response);
        return new BJExternalContextWrapper(ctx);

    }

    @Override
    public ExternalContextFactory getWrapped() {
        return parent;
    }
}
