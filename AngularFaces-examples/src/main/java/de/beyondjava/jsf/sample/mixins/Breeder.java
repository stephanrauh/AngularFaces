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
package de.beyondjava.jsf.sample.mixins;

import java.lang.reflect.Method;

import javassist.Modifier;
import javassist.util.proxy.*;

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
