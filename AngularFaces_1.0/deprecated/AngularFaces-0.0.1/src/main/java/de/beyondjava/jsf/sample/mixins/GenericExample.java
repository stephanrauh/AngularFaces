package de.beyondjava.jsf.sample.mixins;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class GenericExample {
   public static void main(String[] args) throws ReflectiveOperationException {
      MixInFactory<Dog> breeder = new MixInFactory<Dog>();
      Dog dog = breeder.create(Dog.class, Barker.class, Fetcher.class);
      dog.bark();
      dog.fetch();
      dog.bark();
      dog.fetch();
   }

}
