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
library puiBaseComponent;

import 'dart:html';
import 'package:angular/angular.dart';
import 'dart:async';


/**
 * This class encapsulates common behaviour of the components of AngularPrime-Dart.
 * Most notably, the values of the attributes of the PUI template and the HTML code written by the
 * user are merged into the shadow tree.
 *
 * @Todo The merger should happen when the model changes. Current, it's triggered by the users key strokes.
 */
class PuiBaseComponent {

  /** The AngularDart compiler. */
  Compiler compiler;

  /** TODO */
  Injector injector;

  /** TODO */
  DirectiveMap directives;

  /** The parser allows to read values of a variable that's known by name (i.e. know as a String). */
  Parser parser;


  /** Stores the attributes originally defined by the user. */
  Map<String, String> originalValues;

  PuiBaseComponent(this.compiler, this.injector, this.directives, this.parser) {}

  /**
   * Copies every attribute from the updated <pui-input> - which contains the current model values -
   * to the shadow tree.
   */
  void updateAttributesInShadowDOM(Element puiInputElement, Element shadowyInputField, Scope scope) {
    scheduleMicrotask(() {
      puiInputElement.attributes.forEach((String key, String value) =>
          updateAttributeInShadowDOM(shadowyInputField, key, value));
    });
  }

  /**
   * Copies a single attribute from the updated <pui-input> - which contains the current model values -
   * to the same component of the shadow tree.
   * @param inputfield The component that play the same role of the pui-component in the shadow tree. For instance, pui-input
   * is displayed by an input field in the shadow tree. If there no such component, the parameter may be null, and the method does
   * nothing at all.
   * In this case the method may still be useful if it's overridden (as is the case with pui-accordion).
   */
  void updateAttributeInShadowDOM(Element inputfield, String key, String value) {
    if (null != originalValues)
    {
      if (originalValues.containsKey(key)) {
        String s = originalValues[key];
        if (s == null) {
          inputfield.attributes[key] = value;
        } else {
          inputfield.attributes[key] = "$s $value";
        }
      }
    }
  }

  /**
    * Copies every attribute from the updated <pui-input> - which contains the current model values -
    * to the shadow tree, and creates the map of original attribute values.
    */

  void addWatches(Element puiInputElement, Element shadowyInputField, Scope scope) {
    String w = puiInputElement.attributes["pui-toBeWatched"];
    if (null != w && w.length>0)
    {
      List<String> list = w.split(" ");
      list.forEach((String exp) => addWatch(scope, exp, puiInputElement, shadowyInputField));
    }
   }

  /** This method is a little sophisticated.
    * The current scope refers to the pui-component implementation itself.
    * The parent scope refers to the outer world. That's where the pui-component is declared.
    * We want to react on changes of the values of the pui-component declaration,
    * so we add a watch to the parent scope.
    */
  addWatch(Scope scope, String exp, Element puiInputElement, Element shadowyInputField) =>
      scope.parentScope.watch(exp,  (newVar, oldVar){
        updateAttributesInShadowDOM(puiInputElement, shadowyInputField, scope);
      });

  /**
    * Copies every attribute from the updated <pui-input> - which contains the current model values -
    * to the shadow tree, and creates the map of original attribute values.
    */
  void copyAttributesToShadowDOM(Element puiInputElement, Element shadowyInputField, Scope scope) {
    if (null!=shadowyInputField)
    {
      originalValues = new Map();

      puiInputElement.attributes.forEach((String key, String value) =>
         addAttributeToShadowDOM(shadowyInputField, key, value));
    }
  }

  /**
    * Copies a single attribute from the updated <pui-input> - which contains the current model values -
    * to the shadow tree, and adds to the map of original attribute values.
    */
 void addAttributeToShadowDOM(Element inputfield, String key, String value) {
    var s = inputfield.attributes[key];
    if ((s != null) && (s.length>4) && (s.substring(0, 4) == "cmp.")) {
      return;
    }
    originalValues[key] = s;
    if (s == null) {
      inputfield.attributes[key] = value;
    } else {
      inputfield.attributes[key] = "$s $value";
    }
  }
}

