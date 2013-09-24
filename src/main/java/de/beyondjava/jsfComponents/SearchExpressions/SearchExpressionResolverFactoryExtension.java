package de.beyondjava.jsfComponents.SearchExpressions;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.primefaces.expression.SearchExpressionResolverFactory;

public class SearchExpressionResolverFactoryExtension {
   private static boolean initialized = false;

   public static void init() {
      if (!initialized) {
         initialized = true;
         Field mappingField;
         try {
            mappingField = SearchExpressionResolverFactory.class.getDeclaredField("RESOLVER_MAPPING");

            mappingField.setAccessible(true);
            HashMap mapping = (HashMap) mappingField.get(null);
            mapping.put("@aNext", new NextExpressionResolver());
            mapping.put("@aPrevious", new PreviousExpressionResolver());
         }
         catch (NoSuchFieldException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
}
