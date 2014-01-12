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

import java.util.*;
import java.util.regex.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.*;

import org.primefaces.expression.SearchExpressionResolver;

import com.sun.faces.taglib.html_basic.PanelGroupTag;

/**
 * Implementation of the SearchExpression @ngSurrounding which enables you to find a surrounding tag.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class SurroundingExpressionResolver implements SearchExpressionResolver {

    private static final Pattern DIV_PATTERN = Pattern.compile("@surrounding\\((\\d+)\\)");

    private static final Map<Class<?>, String> tagNames = new HashMap<Class<?>, String>() {
        {
            put(HtmlBody.class, "body");
            put(HtmlForm.class, "form");
            put(HtmlHead.class, "head");
            put(HtmlColumn.class, "td");
            put(PanelGroupTag.class, "div");
            put(PanelGroupTag.class, "span");
        }
    };

    @Override
    public UIComponent resolve(UIComponent source, UIComponent last, String expression) {

        try {
            Matcher matcher = DIV_PATTERN.matcher(expression);

            if (matcher.matches()) {

                String tag = matcher.group(1);
                UIComponent surrounding = source;
                while (null != surrounding.getParent()) {
                    surrounding = source.getParent();
                    Class<?> classname = surrounding.getClass();
                    String tagName = tagNames.get(classname);
                    if (tagName != null) {
                        if (tagName.equals(tag)) {
                            return surrounding;
                        }
                    }
                }

                throw new FacesException("Couldn't find a surrounding " + tag
                        + ". Possibly the list of tags is incomplete. Expression: \"" + expression + "\"");

            }
            else {
                throw new FacesException(
                        "Expression does not match following pattern @surrounding(tag). Expression: \"" + expression
                                + "\"");
            }

        }
        catch (Exception e) {
            throw new FacesException("Expression does not match following pattern @surrounding(tag). Expression: \""
                    + expression + "\"", e);
        }
    }
}