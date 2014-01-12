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

import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.*;

import org.primefaces.expression.SearchExpressionResolver;

/**
 * Implementation of the SearchExpression @ngHead delivering the header of the HTML page.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class HeadExpressionResolver implements SearchExpressionResolver {
    @Override
    public UIComponent resolve(UIComponent source, UIComponent last, String expression) {
        UIComponent surrounding = source;
        while (null != surrounding) {
            if (surrounding.getClass() == HtmlBody.class) {
                List<UIComponent> siblings = surrounding.getChildren();
                for (UIComponent s : siblings) {
                    if (s.getClass() == HtmlHead.class) {
                        return s;
                    }
                }
            }
            surrounding = source.getParent();
        }
        throw new FacesException("Couldn't find head element.");

    }
}