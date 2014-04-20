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
library puiRadiobuttons;

import 'dart:html';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';

/**
 * <pui-input> adds AngularDart to an input field styled by PrimeFaces.
 */
@NgComponent(
    selector: 'pui-radiobutton',
    templateUrl: 'packages/angularprime_dart/puiRadiobuttons/pui-radiobutton.html',
    cssUrl: 'packages/angularprime_dart/puiRadiobuttons/pui-radiobutton.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiRadiobuttonComponent extends PuiBaseComponent implements NgShadowRootAware  {
  /** <pui-input> fields require an ng-model attribute. */
  @NgTwoWay("ng-model")
  String ngmodel;

  /** The <input> field in the shadow DOM displaying the component. */
  RadioButtonInputElement shadowyRadiobutton;

  /** The <pui-input> field as defined in the HTML source code. */
  Element puiInputElement;

  /** The scope is needed to add watches. */
  Scope scope;

  /**
   * Initializes the component by setting the <pui-input> field and setting the scope.
   */
  PuiRadiobuttonComponent(this.scope, this.puiInputElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }

  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   *
   * @Todo Find out, which attributes are modified by Angular, and set a watch updating only the attributes that have changed.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowyRadiobutton = shadowRoot.getElementsByTagName("input")[0];
    copyAttributesToShadowDOM(puiInputElement, shadowyRadiobutton, scope);
    addWatches(puiInputElement, shadowyRadiobutton, scope);
    scope.watch(()=>ngmodel, (newVar, oldVar) => updateAttributesInShadowDOM(puiInputElement, shadowyRadiobutton, scope));
  }
}

