/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.sample.mixins.advanced;

import java.lang.reflect.Method;

import javassist.*;
import de.beyondjava.jsf.sample.mixins.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class FastMixinFactory<T> {
   private static Class mixedInClass = null;

   public static void main(String[] args) throws NotFoundException, CannotCompileException,
         ReflectiveOperationException {
      FastMixinFactory<Dog> breeder = new FastMixinFactory<Dog>();
      Dog d = breeder.create(Dog.class, Barker.class, Fetcher.class);
      d.bark();
      d.fetch();
   }

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
