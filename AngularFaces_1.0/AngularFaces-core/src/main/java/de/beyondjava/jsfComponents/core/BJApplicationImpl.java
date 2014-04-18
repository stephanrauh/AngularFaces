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
package de.beyondjava.jsfComponents.core;

import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;

import com.sun.faces.application.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJApplicationImpl extends ApplicationImpl {
   private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.ajax.context.BJExternalContextWrapper");

   public void injectClassIntoMap(String key, ApplicationInstanceFactoryMetadataMap<String, Object> map) {
      Object value;

      value = map.get(key);
      if (value != null) {

         Class<?> clazz;
         if (value instanceof String) {
            String cValue = (String) value;
            try {
               clazz = BJUIComponentFactory.getComponent(cValue);
               LOGGER.info("Injecting " + clazz.getSimpleName() + " as " + cValue + " into component map");
               if (!isDevModeEnabled()) {
                  map.put(key, clazz);
               }
               else {
                  map.scanForAnnotations(key, clazz);
               }
               assert (clazz != null);
            }
            catch (Exception e) {
               throw new FacesException(e.getMessage(), e);
            }
         }
      }

   }

   private boolean isDevModeEnabled() {
      return (getProjectStage() == ProjectStage.Development);

   }

   @Override
   protected Object newThing(String key, ApplicationInstanceFactoryMetadataMap<String, Object> map) {
      LOGGER.info("newThing(" + key + ") = " + map.get(key));
      System.out.println("newThing(" + key + ") = " + map.get(key));
      if (key.startsWith("de")) {
         injectClassIntoMap(key, map);
      }
      return super.newThing(key, map);
   }
}
