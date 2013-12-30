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
package de.beyondjava.jsfComponents.common;

import java.lang.annotation.Annotation;

import javax.faces.component.UIComponent;
import javax.validation.constraints.*;

/**
 * Stores server side validation and layout informations.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 */
public class NGBeanAttributeInfo {
   private Class<?> clazz;
   private boolean hasMax = false;

   private boolean hasMaxSize = false;
   private boolean hasMin = false;
   private boolean hasMinSize = false;
   private boolean isInteger = false;
   private boolean isRequired = false;
   private long max = 0;
   private long maxSize = Integer.MIN_VALUE;
   private long min = 0;
   private long minSize = Integer.MIN_VALUE;

   /**
    * Extract the server side validation and layout informations.
    */
   public NGBeanAttributeInfo(UIComponent component) {
      readJSR303Annotations(component);
   }

   public Class<?> getClazz() {
      return clazz;
   }

   public long getMax() {
      return max;
   }

   public long getMaxSize() {
      return maxSize;
   }

   public long getMin() {
      return min;
   }

   public long getMinSize() {
      return minSize;
   }

   public boolean isHasMax() {
      return hasMax;
   }

   public boolean isHasMaxSize() {
      return hasMaxSize;
   }

   public boolean isHasMin() {
      return hasMin;
   }

   public boolean isHasMinSize() {
      return hasMinSize;
   }

   public boolean isInteger() {
      return isInteger;
   }

   public boolean isRequired() {
      return isRequired;
   }

   /**
    * Read the JSR 303 annotations from a bean\"s attribute.
    * 
    * @param component
    */
   private void readJSR303Annotations(UIComponent component) {
      Annotation[] annotations = ELTools.readAnnotations(component);
      if (null != annotations) {
         for (Annotation a : annotations) {
            if (a instanceof Max) {
               long maximum = ((Max) a).value();
               max = maximum;
               hasMax = true;
            }
            else if (a instanceof Min) {
               long minimum = ((Min) a).value();
               hasMin = true;
               min = minimum;
            }
            else if (a instanceof Size) {
               maxSize = ((Size) a).max();
               hasMaxSize = maxSize > 0;
               minSize = ((Size) a).min();
               hasMinSize = minSize > 0;
            }
            else if (a instanceof NotNull) {
               isRequired = true;
            }
         }
      }

      clazz = ELTools.getType(component);
      if ((clazz == Integer.class) || (clazz == int.class)) {
         isInteger = true;
      }
   }
}
