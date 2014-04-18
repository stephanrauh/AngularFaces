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

import java.util.regex.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import org.primefaces.expression.SearchExpressionResolver;

/**
 * Implementation of the SearchExpression @ngClass delivering every node bearing a particular CSS class.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class ClassExpressionResolver implements SearchExpressionResolver {

    private static final Pattern CLASS_PATTERN = Pattern.compile("@class\\((\\d+)\\)");

    @Override
    public UIComponent resolve(UIComponent source, UIComponent last, String expression) {

        try {
            Matcher matcher = CLASS_PATTERN.matcher(expression);

            if (matcher.matches()) {

                int childNumber = Integer.parseInt(matcher.group(1));

                if ((childNumber + 1) > last.getChildCount()) {
                    throw new FacesException("Component with clientId \"" + last.getClientId()
                            + "\" has fewer children as \"" + childNumber + "\". Expression: \"" + expression + "\"");
                }

                return last.getChildren().get(childNumber);

            }
            else {
                throw new FacesException("Expression does not match following pattern @child(n). Expression: \""
                        + expression + "\"");
            }

        }
        catch (Exception e) {
            throw new FacesException("Expression does not match following pattern @child(n). Expression: \""
                    + expression + "\"", e);
        }
    }

}