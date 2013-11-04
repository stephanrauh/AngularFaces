package de.beyondjava.jsf.sample.mixins;

import java.lang.reflect.Method;
import java.util.*;

import javassist.util.proxy.*;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class MixInFactory<T> {

   @SuppressWarnings("unchecked")
   public T create(Class<T> iface, Class<?>... mixinClasses) throws ReflectiveOperationException {
      MethodHandler handler = new MixinHandler<T>(iface, mixinClasses);
      T objectWithMixins;
      ProxyFactory factory = new ProxyFactory();
      factory.setInterfaces(new Class[] { iface });
      try {
         objectWithMixins = (T) factory.create(new Class<?>[0], new Object[0], handler);
      }
      catch (ReflectiveOperationException e) {
         // ToDo: need better logging and exception management
         System.err.println("Couldn't create object due to " + e.toString());
         throw e;
      }
      return objectWithMixins;
   }
}

class MixinHandler<T> implements MethodHandler {
   Map<String, Object> delegateTo = new HashMap<String, Object>();
   Map<String, Method> methods = new HashMap<String, Method>();

   MixinHandler(Class<T> iface, Class<?>... mixinClasses) throws InstantiationException, IllegalAccessException {

      for (Class<?> c : mixinClasses) {
         Object delegate = c.newInstance();
         Method[] exposedMethods = c.getDeclaredMethods();
         for (Method m : exposedMethods) {
            final String signature = getSignature(m);
            if (!delegateTo.containsKey(signature)) {
               delegateTo.put(signature, delegate);
               methods.put(signature, m);
            }
         }
      }
   }

   private String getSignature(Method m) {
      String params = "";
      Class<?>[] p = m.getParameterTypes();
      for (Class<?> param : p) {
         if (params.length() > 0) {
            params += "," + param.getSimpleName();
         }
         else {
            params += "," + param.getSimpleName();
         }
      }

      return m.getName() + "(" + params + ")";
   }

   @Override
   public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
      final String signature = getSignature(thisMethod);
      Object delegate = delegateTo.get(signature);
      Method m = methods.get(signature);
      return m.invoke(delegate, args);
   }

};
