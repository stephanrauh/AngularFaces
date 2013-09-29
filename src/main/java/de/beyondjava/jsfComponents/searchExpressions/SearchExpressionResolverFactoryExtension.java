package de.beyondjava.jsfComponents.searchExpressions;

import org.primefaces.expression.SearchExpressionResolverFactory;

public class SearchExpressionResolverFactoryExtension {
   private static boolean initialized = false;

   public static void init() {
      if (!initialized) {
         initialized = true;
         SearchExpressionResolverFactory.registerResolver("@first", new FirstExpressionResolver());
         SearchExpressionResolverFactory.registerResolver("@last", new LastExpressionResolver());
      }
   }
}
