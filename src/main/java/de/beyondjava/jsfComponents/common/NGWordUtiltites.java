/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.common;

/**
 * Collection of minor utilities dealing with words and strings.
 * @author Stephan Rauh http://www.beyondjava.net
 */
public class NGWordUtiltites
{
   /**
    * Converts a camelcase variable name to a human readable text.
    * @param camel
    * @return 
    */
   
   public static String labelFromCamelCase(String camel)
   {
      StringBuffer label = new StringBuffer();
      for (int i = 0; i < camel.length(); i++)
      {
         char c = camel.charAt(i);
         if (c=='_'){
            label.append(" ");
         }
         else if (Character.isUpperCase(c))
         {
            label.append(' ');
            label.append(Character.toLowerCase(c));
         }
         else
            label.append(c);
      }
      return label.toString();
   }
}
