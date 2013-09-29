package de.beyondjava.jsfComponents.searchExpressions;

import java.util.List;

import javax.faces.component.UIComponent;

import org.primefaces.expression.SearchExpressionResolver;

/**
 * Implementation of the SearchExpression @ngPrevious which enables you to find
 * the predecessor of the current JSF component. Useful for adding an h:message
 * behind an h:inputText field in case you can't or don't want to use an
 * ng:inputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

public class LastExpressionResolver implements SearchExpressionResolver {
   @Override
   public UIComponent resolve(UIComponent source, UIComponent last, String expression) {
      final UIComponent parent = last.getParent();
      List<UIComponent> children = parent.getChildren();
      return children.get(children.size() - 1);
   }
}