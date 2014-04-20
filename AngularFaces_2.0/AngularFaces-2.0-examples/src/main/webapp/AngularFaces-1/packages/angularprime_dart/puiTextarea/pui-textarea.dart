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
library puiTextarea;

import 'dart:html';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';

/**
 * <pui-input> adds AngularDart to an input field styled by PrimeFaces.
 */
@NgComponent(
    selector: 'pui-textarea',
    templateUrl: 'packages/angularprime_dart/puiTextarea/pui-textarea.html',
    cssUrl: 'packages/angularprime_dart/puiTextarea/pui-textarea.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiTextareaComponent extends PuiBaseComponent implements NgShadowRootAware  {
  /** <pui-input> fields require an ng-model attribute. */
  @NgTwoWay("ng-model")
  String ngmodel;

  /** The <textarea> field in the shadow DOM displaying the component. */
  TextAreaElement shadowyTextareaField;

  /** The <pui-textarea> field as defined in the HTML source code. */
  Element puiTextareaElement;

  /** The scope is needed to add watches. */
  Scope scope;

  /** Does the text area grow automatically if more space is needed? */
  @NgAttr("autoresize")
  String autoresize;

  /**
   * Initializes the component by setting the <pui-textarea> field and setting the scope.
   */
  PuiTextareaComponent(this.scope, this.puiTextareaElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }

  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   *
   * @Todo Find out, which attributes are modified by Angular, and set a watch updating only the attributes that have changed.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowyTextareaField = shadowRoot.getElementsByTagName("textarea")[0];
    // @ToDo: the class shouldn't be added to the pui-class, but to the inner class
    // (but the class definition is currently destroyed by copyAttributesToShadowDOM and
    // updateAttributesInShadowDOM)
    if (autoresize=="true")
      puiTextareaElement.classes.add("pui-inputtextarea-resizable");
    copyAttributesToShadowDOM(puiTextareaElement, shadowyTextareaField, scope);
    addWatches(puiTextareaElement, shadowyTextareaField, scope);
    scope.watch("ngmodel", (newVar, oldVar) => updateAttributesInShadowDOM(puiTextareaElement, shadowyTextareaField, scope));
  }
}

