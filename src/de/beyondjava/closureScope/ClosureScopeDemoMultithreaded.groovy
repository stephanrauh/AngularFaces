/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.closureScope

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
public class ClosureScopeDemoMultithreaded {
   public static void main(String[] args) {
      100.times{
         //         long start = System.nanoTime()
         List<Closure> closures = new ClosureScopeDemoMultithreaded().demo()
         closures[1..-1].each({new Thread(new ClosureScopeTester(it)).start() })
         //         System.out.println("Time elapsed: " + ((System.nanoTime()-start)/1000)/1000.0d + " ms")
         Thread.currentThread().sleep(100)
         closures[0]()
      }
   }

   public def demo() {
      int counter=0
      List counters = []
      11.times { int thread ->
         def closure =
         { counter++; if (thread==0) println "$thread: $counter"  }
         counters = counters + closure
      }
      return counters
   }

   private static final class ClosureScopeTester implements Runnable {
      Closure closure

      public ClosureScopeTester(Closure closure) {
         this.closure = closure
      }

      public void run() {
         for (int i = 0; i < 1000; i++) {
            closure()
         }
      }
   }
}
