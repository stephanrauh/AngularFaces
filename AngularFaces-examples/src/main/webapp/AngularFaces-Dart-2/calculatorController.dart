import 'package:angular/angular.dart';

@MirrorsUsed(override: '*')
import 'dart:mirrors';



import 'package:angular/angular.dart';

/* Use the NgController annotation to indicate that this class is an
 * Angular Directive. The compiler will instantiate the directive if
 * it finds it in the DOM.
 *
 * The selector field defines the CSS selector that will trigger the
 * directive. It can be any valid CSS selector which does not cross
 * element boundaries.
 *
 * The publishAs field specifies that the directive instance should be
 * assigned to the current scope under the name specified.
 *
 * The directive's public fields are available for data binding from the view.
 * Similarly, the directive's public methods can be invoked from the view.
 */
@NgController(
    publishAs: 'CalculatorController')
class CalculatorController {
 int number1;
 int number2;
 int result;

 CalculatorController()
 {
   number1=0;
   number2=0;
   result=0;
 }
  int add() {
    return number1 + number2;
  }
  String convertToString(int number) {
      return number.toString();
  }
}


class CalculatorControllerModule extends Module {
  CalculatorControllerModule() {
    type(CalculatorController);
  }
}

main() {
  ngBootstrap(module: new CalculatorControllerModule());
}


