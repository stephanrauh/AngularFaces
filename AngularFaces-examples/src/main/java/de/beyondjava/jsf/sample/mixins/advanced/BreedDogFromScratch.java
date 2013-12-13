/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.sample.mixins.advanced;

import javassist.*;
import de.beyondjava.jsf.sample.mixins.Dog;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BreedDogFromScratch {
   public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException,
         IllegalAccessException {
      ClassPool pool = ClassPool.getDefault();
      CtClass cc = pool.makeClass("DogImpl");
      CtClass barker = pool.get("de.beyondjava.jsf.sample.mixins.Barker");
      cc.setSuperclass(barker);
      CtClass dog = pool.get("de.beyondjava.jsf.sample.mixins.Dog");
      cc.setInterfaces(new CtClass[] { dog });
      CtClass fetcher = pool.get("de.beyondjava.jsf.sample.mixins.Fetcher");

      CtField fetcherField = new CtField(fetcher, "fetcher", cc);
      cc.addField(fetcherField, "new de.beyondjava.jsf.sample.mixins.Fetcher()");

      CtMethod fetchMethod = CtNewMethod.make("public void fetch() { fetcher.fetch(); }", cc);
      cc.addMethod(fetchMethod);

      Class dogClass = cc.toClass();
      Dog d = (Dog) dogClass.newInstance();
      d.bark();
      d.fetch();
   }
}
