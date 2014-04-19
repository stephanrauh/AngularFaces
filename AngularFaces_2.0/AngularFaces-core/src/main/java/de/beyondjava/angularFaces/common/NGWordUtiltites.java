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
package de.beyondjava.angularFaces.common;

/**
 * Collection of minor utilities dealing with words and strings.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 */
public class NGWordUtiltites {
   /**
    * Converts a camelcase variable name to a human readable text.
    * 
    * @param camel
    * @return
    */

   public static String labelFromCamelCase(String camel) {
      StringBuffer label = new StringBuffer();
      for (int i = 0; i < camel.length(); i++) {
         char c = camel.charAt(i);
         if (c == '_') {
            label.append(" ");
         }
         else if (Character.isUpperCase(c)) {
            label.append(' ');
            label.append(Character.toLowerCase(c));
         }
         else {
            label.append(c);
         }
      }
      return label.toString();
   }
}
