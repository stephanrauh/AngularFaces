/**
 * (C) 2014 Stephan Rauh http://www.beyondjava.net
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
library puiInput;

import 'dart:html';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';
part 'pui-color.dart';
part 'pui-week.dart';

/**
 * <pui-input> adds AngularDart to an input field styled by PrimeFaces.
 */
@NgComponent(
    selector: 'pui-input',
    templateUrl: 'packages/angularprime_dart/puiInput/pui-input.html',
    cssUrl: 'packages/angularprime_dart/puiInput/pui-input.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiInputTextComponent extends PuiBaseComponent implements NgShadowRootAware  {
  /** <pui-input> fields require an ng-model attribute. */
  @NgTwoWay("ng-model")
  var ngmodel;

  /** The <input> field in the shadow DOM displaying the component. */
  InputElement shadowyInputField;

  /** The <pui-input> field as defined in the HTML source code. */
  Element puiInputElement;

  /** The scope is needed to add watches. */
  Scope scope;

  /** Reference to the Angular model corresponding to the input field */
  NgModel _model;

  /** optional: if set to "true", the button is disabled and doesn't react to being clicked. */
  @NgAttr("disabled")
  String disabled;


  static final Map errorMessages = {
         "ng-required":"Please fill this field",
         "pui-min-error": "Number to small",
         "pui-max-error": "Number to big",
         "pui-min-length-error": "too few characters",
         "pui-max-length-error": "too many characters",
         "pui-pattern-error": "Your input doesn't match the allowed format",
         "ng-pattern": "Your input doesn't match the allow format"
  };

  static final Map userDefinedErrorMessages = {
         "ng-required":"required-message",
         "pui-min-error": "min-message",
         "pui-max-error": "max-message",
         "pui-min-length-error": "min-length-message",
         "pui-max-length-error": "min-length-message",
         "pui-pattern-error": "pattern-message",
         "ng-pattern": "pattern-message"
  };

  /**
   * Initializes the component by setting the <pui-input> field and setting the scope.
   */
  PuiInputTextComponent(this.scope, this.puiInputElement, this._model, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }

  /** returns the CSS style needed to display or hide the error message */
  String isInvalid()
  {
    if (_model.invalid)
    {
      var es = _model.errorStates;
      return "display:block";
    }
    else
    {
      return "display:none";
    }
  }

  /** return the validation error message (if any) */
  String get errorMessage{

    if (_model.invalid)
    {
      if (null != shadowyInputField) {
        shadowyInputField.classes.add("ui-state-error");
      }
      String errorMessage=null;
      Map es = _model.errorStates;
      es.forEach((key, value){ if (userDefinedErrorMessages.containsKey(key)) {
                                 if (puiInputElement.attributes[userDefinedErrorMessages[key]]!=null)
                                   errorMessage=puiInputElement.attributes[userDefinedErrorMessages[key]];
                                 }
                             });
      if (errorMessage==null) {
        es.forEach((key, value){ if (errorMessages.containsKey(key)) {errorMessage=errorMessages[key];}});
      }
      if (errorMessage==null) {
        errorMessage="Please check your input. Something's wrong.";
      }
      return errorMessage;
    }
    else
    {
      if (null != shadowyInputField) {
        shadowyInputField.classes.remove("ui-state-error");
      }
      return "";
    }
  }


  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   *
   * @Todo Find out, which attributes are modified by Angular, and set a watch updating only the attributes that have changed.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowyInputField = shadowRoot.getElementsByTagName("input")[0];

    _autoDetectType();
    copyAttributesToShadowDOM(puiInputElement, shadowyInputField, scope);
    _addConstraintsForNumerics();
    _addLengthConstraints();
    addWatches(puiInputElement, shadowyInputField, scope);
    scope.watch("ngmodel", (newVar, oldVar) { updateAttributesInShadowDOM(puiInputElement, shadowyInputField, scope);});
    scope.watch("disabled", (newVar, oldVar) { _updateDisabledState();});
  }

  /** depending on the disabled flag, the input field is switched either to read-only or editable mode */
  void _updateDisabledState()
  {
    if (disabled=="true")
    {
      shadowyInputField.classes.add("ui-state-disabled");
      shadowyInputField.attributes["disabled"]="true";
    }
    else
    {
      shadowyInputField.classes.remove("ui-state-disabled");
      shadowyInputField.attributes.remove("disabled");
    }
  }

  /** adds validators to check the minimum and maximum size of the input */
  void _addLengthConstraints() {
    if (null != puiInputElement.attributes["minlength"]){
      int minlength = int.parse(puiInputElement.attributes["minlength"].toString());
      new PuiModelMinLengthValidator(_model, minlength);
    }
    if (null != puiInputElement.attributes["maxlength"]){
      int maxlength = int.parse(puiInputElement.attributes["maxlength"].toString());
      new PuiModelMinLengthValidator(_model, maxlength);
    }

  }

  /**
   * Traditionally the type of an HTML input field is specified explicitely by the web designer.
   * However, in many cases the type of an input field can be derived from the underlying model.
   * This method examines the type of the ng-model, setting the input field type automatically if possible.
   */
  void _autoDetectType() {
    if (ngmodel.runtimeType==int || ngmodel.runtimeType==double)
    {
      if (puiInputElement.attributes["type"]==null)
      {
        puiInputElement.attributes["type"]="number";
      }
    }
    else if (ngmodel.runtimeType==DateTime)
    {
      if (puiInputElement.attributes["type"]==null)
      {
        puiInputElement.attributes["type"]="date";
      }
    }
  }

  /**
   * Numeric values - int and double - can be validated against a minimum and a maximum value. Plus,
   * there's only a limited range of legal characters we accept.
   */
  void _addConstraintsForNumerics() {
    if (puiInputElement.attributes["type"]=="number")
    {
      if (null != puiInputElement.attributes["min"]){
        double min = double.parse(puiInputElement.attributes["min"].toString());
        new PuiModelMinNumberValidator(_model, min);
      }
      if (null != puiInputElement.attributes["max"]){
        double max = double.parse(puiInputElement.attributes["max"].toString());
        new PuiModelMaxNumberValidator(_model, max);
      }
    //      _model.addValidator(new NgModelNumberValidator(_model));
      // ToDo: check for cross browser compatibility
      // (see http://japhr.blogspot.de/2013/08/keyboard-event-support-remains-rough-in.html)
      puiInputElement.onKeyDown.listen((KeyboardEvent e) {
        if (e.keyCode>0)
        {
          bool good= ((e.keyCode>=48 && e.keyCode<=57)  // digits
                  || e.keyCode==187 || e.keyCode==189 || e.keyCode==190) // decimal point, minus and plus
                  || (e.keyCode==8) || (e.keyCode==46) // backspace and delete
                  || ((e.keyCode>=37)&& (e.keyCode<=40)); // cursor keys
    //          print("${e.charCode} ${e.keyCode} $good");
          if (!good)
          {
            e.preventDefault();
          }
        }
      });
    }
  }
}

/** This class is redundant to NgModelMinNumberValidator, which should be active, but isn't for some reason.
 * So we implement it manually until the apparent AngularDart bug is fixed.
 */
class PuiModelMinNumberValidator implements NgValidator {
  double _min;
  final NgModel _ngModel;

  PuiModelMinNumberValidator(this._ngModel, double this._min) {
    _ngModel.addValidator(this);
  }

  bool isValid(modelValue) {
    try {
      num parsedValue = double.parse(modelValue.toString());
      if (!parsedValue.isNaN) {
        return parsedValue >= _min;
      }
    } catch(exception, stackTrace) {}

    //this validator doesn't care if the type conversation fails or the value
    //is not a number (NaN) because NgModelNumberValidator will handle the
    //number-based validation either way.
    return true;
  }

  @override
  String get name => "pui-min-error";
}

/** This class is redundant to NgModelMaxNumberValidator, which should be active, but isn't for some reason.
 * So we implement it manually until the apparent AngularDart bug is fixed.
 */
class PuiModelMaxNumberValidator implements NgValidator {
  double _max;
  final NgModel _ngModel;

  PuiModelMaxNumberValidator(this._ngModel, double this._max) {
    _ngModel.addValidator(this);
  }

  bool isValid(modelValue) {
    try {
      num parsedValue = double.parse(modelValue.toString());
      if (!parsedValue.isNaN) {
        return parsedValue <= _max;
      }
    } catch(exception, stackTrace) {}

    //this validator doesn't care if the type conversation fails or the value
    //is not a number (NaN) because NgModelNumberValidator will handle the
    //number-based validation either way.
    return true;
  }

  @override
  String get name => "pui-max-error";
}

/**
 * Minimum length constrait
 */
class PuiModelMinLengthValidator implements NgValidator {
  int _min;
  final NgModel _ngModel;

  PuiModelMinLengthValidator(this._ngModel, int this._min) {
    _ngModel.addValidator(this);
  }

  bool isValid(modelValue) {
    try {
      if (modelValue==null) return false;
      if (modelValue.toString().length < _min) return false;
    } catch(exception, stackTrace) {}

    return true;
  }

  @override
  String get name => "pui-min-length-error";
}

/**
 * Maximum length constrait (redundant to HTML5's maxlength attribute)
 */
class PuiModelMaxLengthValidator implements NgValidator {
  int _max;
  final NgModel _ngModel;

  PuiModelMaxLengthValidator(this._ngModel, int this._max) {
    _ngModel.addValidator(this);
  }

  bool isValid(modelValue) {
    try {
      if (modelValue==null) return false;
      if (modelValue.toString().length > _max) return false;
    } catch(exception, stackTrace) {}

    return true;
  }

  @override
  String get name => "pui-max-length-error";
}


