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
import javax.faces.context.ExternalContext;

import com.sun.faces.context.ExternalContextFactoryImpl;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJExternalContextFactoryImpl extends ExternalContextFactoryImpl {
   private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.ajax.context.BJExternalContextFactoryImpl");

   public BJExternalContextFactoryImpl() {
   }

   @Override
   public ExternalContext getExternalContext(Object servletContext, Object request, Object response)

   throws FacesException {
      ExternalContext ctx = super.getExternalContext(servletContext, request, response);
      return new BJExternalContextWrapper(ctx);

   }

}
