/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.core;

import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@ApplicationScoped
@ManagedBean
public class BJUIComponentFactory {
   private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.ajax.context.BJExternalContextWrapper");

   public static Class getComponent(String name) {
      // LOGGER.info("getComponent(" + name + ")");
      Class clazz = null;
      // try {
      // clazz = Util.loadClass(name, name);
      // FastMixinFactory<Dog> breeder = new FastMixinFactory<Dog>();
      // Dog dog = breeder.create(Dog.class, clazz, SilentBarker.class,
      // SilentFetcher.class);
      // dog.bark();
      // // System.out.println(((UIComponent) dog).getFamily());
      //
      // }
      // catch (ClassNotFoundException e) {
      // LOGGER.log(Level.SEVERE, "UIComponent class not found", e);
      // return null;
      // }
      // catch (ReflectiveOperationException e) {
      // LOGGER.log(Level.SEVERE, "UIComponent class couldn\"t be mixed in", e);
      // return null;
      // }
      // catch (CannotCompileException e) {
      // LOGGER.log(Level.SEVERE, "UIComponent class couldn\"t be mixed in", e);
      // return null;
      // }
      // catch (NotFoundException e) {
      // LOGGER.log(Level.SEVERE, "UIComponent class couldn\"t be mixed in", e);
      // return null;
      // }
      return clazz;
   }

}
