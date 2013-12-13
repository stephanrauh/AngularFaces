package de.beyondjava.jsf.sample.mixins;

import java.lang.reflect.Method;

import javassist.Modifier;
import javassist.util.proxy.*;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class Breeder {
   public static void main(String[] args) {
      Breeder breeder = new Breeder();
      Dog dog = breeder.breedDog();
      dog.bark();
      dog.fetch();
      dog.bark();
      dog.fetch();
   }

   public Dog breedDog() {
      ProxyFactory factory = new ProxyFactory();
      factory.setInterfaces(new Class[] { Dog.class });
      factory.setSuperclass(Barker.class);

      factory.setFilter(new MethodFilter() {
         @Override
         public boolean isHandled(Method method) {
            return Modifier.isAbstract(method.getModifiers());
         }
      });

      MethodHandler handler = new MethodHandler() {
         Fetcher husky = new Fetcher();

         @Override
         public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            if (thisMethod.getName().equals("fetch")) {
               husky.fetch();
               return null;
            }
            // ToDo: need better logging and exception management
            System.err.println("Method " + thisMethod + " not implemented");
            return null;
         }
      };

      Dog dog;
      try {
         dog = (Dog) factory.create(new Class<?>[0], new Object[0], handler);
      }
      catch (ReflectiveOperationException e) {
         // ToDo: need better logging and exception management
         e.printStackTrace();
         return null;
      }
      return dog;
   }
}
