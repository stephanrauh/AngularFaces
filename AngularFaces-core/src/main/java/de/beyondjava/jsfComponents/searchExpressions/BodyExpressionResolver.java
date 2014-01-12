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
package de.beyondjava.jsfComponents.searchExpressions;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.component.html.HtmlBody;

import org.primefaces.expression.SearchExpressionResolver;

/**
 * Implementation of the SearchExpression @ngBody delivering the body tag. Useful for updating the entire view without
 * reloading stylesheet and Javascript, but with calling onLoad() again.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BodyExpressionResolver implements SearchExpressionResolver {
    @Override
    public UIComponent resolve(UIComponent source, UIComponent last, String expression) {
        UIComponent surrounding = source;
        while (null != surrounding) {
            if (surrounding.getClass() == HtmlBody.class) {
                return surrounding;
            }
            String rendererType = surrounding.getRendererType();
            System.out.println(surrounding.getClass().getName());
            System.out.println(rendererType);
            System.out.println(surrounding.getId());
            if ((rendererType != null) && rendererType.contains("Body")) {
                if ((surrounding.getId() == null) || surrounding.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
                    throw new FacesException("Please add an id to the body tag.");
                }
                return surrounding;
            }
            surrounding = surrounding.getParent();
        }
        throw new FacesException("Couldn't find body element.");

    }
}