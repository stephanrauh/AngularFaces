/**
 *  (C) Stephan Rauh http://www.beyondjava.net
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
