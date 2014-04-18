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
