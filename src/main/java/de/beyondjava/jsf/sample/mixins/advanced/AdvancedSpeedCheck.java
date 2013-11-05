package de.beyondjava.jsf.sample.mixins.advanced;

import javassist.*;
import de.beyondjava.jsf.sample.mixins.*;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class AdvancedSpeedCheck {
   public static void main(String[] args) throws ReflectiveOperationException, CannotCompileException,
         NotFoundException {

      long t = System.nanoTime();
      FastMixinFactory<Dog> breeder = new FastMixinFactory<Dog>();
      Dog dog = breeder.create(Dog.class, SilentBarker.class, SilentFetcher.class);
      System.out.println("first Dog           " + (((System.nanoTime() - t) / 1000) / 1000.0d) + " ms");

      for (int i = 0; i <= 10000; i++) {
         boolean silent = !((i < 2) || (i == 10) || ((i % 1000) == 0));
         if (!silent) {
            System.out.println("Iteration " + i);
         }
         testCreate(silent);
         testCreateMixin(silent);
         testFocusOnCall(silent);
         testFocusOnCallMixin(silent);
      }
   }

   private static void testCreate(boolean silent) throws ReflectiveOperationException {
      long t = System.nanoTime();
      for (int i = 0; i < 1000; i++) {

         SilentFetcher f = new SilentFetcher();
         SilentBarker b = new SilentBarker();
         b.bark();
         f.fetch();
         b.bark();
         f.fetch();
      }
      if (!silent) {
         System.out.println("testCreate           " + (((System.nanoTime() - t) / 1000) / 1000.0d) + " ms");
      }
   }

   private static void testCreateMixin(boolean silent) throws ReflectiveOperationException, CannotCompileException,
         NotFoundException {
      long t = System.nanoTime();
      for (int i = 0; i < 1000; i++) {

         FastMixinFactory<Dog> breeder = new FastMixinFactory<Dog>();
         Dog dog = breeder.create(Dog.class, SilentBarker.class, SilentFetcher.class);
         dog.bark();
         dog.fetch();
         dog.bark();
         dog.fetch();
      }
      if (!silent) {
         System.out.println("testCreateMixin      " + (((System.nanoTime() - t) / 1000) / 1000.0d) + " ms");
      }
   }

   private static void testFocusOnCall(boolean silent) throws ReflectiveOperationException {
      SilentFetcher f = new SilentFetcher();
      SilentBarker b = new SilentBarker();
      long t = System.nanoTime();
      for (int i = 0; i < 1000; i++) {
         b.bark();
         f.fetch();
         b.bark();
         f.fetch();
      }
      if (!silent) {
         System.out.println("testFocusOnCall      " + (((System.nanoTime() - t) / 1000) / 1000.0d) + " ms");
      }
   }

   private static void testFocusOnCallMixin(boolean silent) throws ReflectiveOperationException,
         CannotCompileException, NotFoundException {
      FastMixinFactory<Dog> breeder = new FastMixinFactory<Dog>();
      Dog dog = breeder.create(Dog.class, SilentBarker.class, SilentFetcher.class);
      long t = System.nanoTime();
      for (int i = 0; i < 1000; i++) {
         dog.bark();
         dog.fetch();
         dog.bark();
         dog.fetch();
      }
      if (!silent) {
         System.out.println("testFocusOnCallMixin " + (((System.nanoTime() - t) / 1000) / 1000.0d) + " ms");
      }
   }

}
