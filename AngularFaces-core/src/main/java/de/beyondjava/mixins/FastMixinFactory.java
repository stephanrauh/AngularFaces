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
package de.beyondjava.mixins;

import java.lang.reflect.Method;

import javassist.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class FastMixinFactory<T> {
   private static Class mixedInClass = null;

   @SuppressWarnings("unchecked")
   public T create(Class<T> iface, Class<?>... mixinClasses) throws ReflectiveOperationException,
         CannotCompileException, NotFoundException {
      if (null == mixedInClass) {
         Class.forName(iface.getCanonicalName());
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(new ClassClassPath(iface));
         CtClass cc = pool.makeClass(iface.getCanonicalName() + "Impl");
         CtClass ctInterface = pool.get(iface.getCanonicalName());
         cc.setInterfaces(new CtClass[] { ctInterface });

         for (Class delegateClass : mixinClasses) {
            String delegateClassName = delegateClass.getCanonicalName();
            CtClass ctDelegateClass = pool.get(delegateClassName);
            String delegateFieldName = delegateClassName.toLowerCase();
            int pos = delegateFieldName.lastIndexOf('.');
            if (pos >= 0) {
               delegateFieldName = delegateFieldName.substring(pos + 1);
            }
            CtField delegateField = new CtField(ctDelegateClass, delegateFieldName, cc);
            cc.addField(delegateField, "new " + delegateClassName + "()");
            for (Method m : delegateClass.getDeclaredMethods()) {
               String methodName = m.getName();
               String signature = m.toGenericString();
               signature = signature.replace(delegateClassName + ".", "");
               String declaration;
               Class<?>[] parameters = m.getParameterTypes();
               if (null != m.getReturnType()) {
                  declaration = signature + "{ " + "return " + delegateFieldName + "." + methodName
                        + getParameterList(m) + ";}";

               }
               else {
                  declaration = signature + "{ " + delegateFieldName + "." + methodName + getParameterList(m) + ";}";
               }

               try {
                  CtMethod delegateMethod = CtNewMethod.make(declaration, cc);
                  cc.addMethod(delegateMethod);
               }
               catch (CannotCompileException e) {
                  System.out.println(declaration);
               }
               // CtMethod fetchMethod =
               // CtNewMethod.make("public void fetch() { fetcher.fetch(); }",
               // cc);
               // cc.addMethod(fetchMethod);
            }
         }
         mixedInClass = cc.toClass();
      }

      T d = (T) mixedInClass.newInstance();
      return d;
   }

   private String getParameterList(Method m) {
      // ToDo: extract the parameter list
      return "()";
   }

}
