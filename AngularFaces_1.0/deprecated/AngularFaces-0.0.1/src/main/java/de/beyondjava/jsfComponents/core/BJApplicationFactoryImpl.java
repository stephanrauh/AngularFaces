/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.core;

import javax.faces.application.Application;

import com.sun.faces.application.ApplicationFactoryImpl;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BJApplicationFactoryImpl extends ApplicationFactoryImpl {
   @Override
   public Application getApplication() {
      return new BJApplicationImpl();
   }

}
