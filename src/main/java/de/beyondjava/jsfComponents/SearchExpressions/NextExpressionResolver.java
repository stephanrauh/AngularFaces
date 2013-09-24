package de.beyondjava.jsfComponents.SearchExpressions;

import java.util.List;

import javax.faces.component.UIComponent;

import org.primefaces.expression.SearchExpressionResolver;

/**
 * Implementation of the SearchExpression @ngNext which enables you to find the
 * successor of the current JSF component. Useful for adding an h:outputLabel in
 * front of an h:inputText field in case you can't or don't want to use an
 * ng:inputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NextExpressionResolver implements SearchExpressionResolver {
   @Override
   public UIComponent resolve(UIComponent source, UIComponent last, String expression) {
      final UIComponent parent = last.getParent();
      boolean selectNextOne = false;
      List<UIComponent> children = parent.getChildren();
      for (UIComponent c : children) {
         if (selectNextOne) {
            return c;
         }
         if (c == source) {
            selectNextOne = true;
         }
      }
      return null;
   }
}