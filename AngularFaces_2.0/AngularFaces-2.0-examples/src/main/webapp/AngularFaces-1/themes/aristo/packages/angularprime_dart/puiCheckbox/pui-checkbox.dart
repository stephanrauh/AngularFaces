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
library puiCheckbox;

import 'dart:html';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';

/**
 * <pui-checkbox> adds AngularDart to an checkbox styled by PrimeFaces.
 */
@NgComponent(
    selector: 'pui-checkbox',
    templateUrl: 'packages/angularprime_dart/puiCheckbox/pui-checkbox.html',
    cssUrl: 'packages/angularprime_dart/puiCheckbox/pui-checkbox.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiCheckboxComponent extends PuiBaseComponent implements NgShadowRootAware  {
  /** The <pui-checkbox> field as defined in the HTML source code. */
  Element puiCheckboxElement;

  /** The <input> field in the shadow DOM displaying the component. */
  Element shadowyInputField;

  Element iconElement;

  /** <pui-input> fields require an ng-model attribute. */
  @NgTwoWay("ng-model")
  bool ngmodel;

  /** <pui-input> fields require an ng-model attribute. */
  @NgOneWay("disabled")
  bool disabled;


  /** The scope is needed to add watches. */
  Scope scope;

  /**
   * Initializes the component by setting the <pui-input> field and setting the scope.
   */
  PuiCheckboxComponent(this.scope, this.puiCheckboxElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }

  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   *
   * @Todo Find out, which attributes are modified by Angular, and set a watch updating only the attributes that have changed.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowyInputField = shadowRoot.getElementById("checkboxVisibleItemId");
    iconElement = shadowRoot.getElementById("checkboxIconId");
    copyAttributesToShadowDOM(puiCheckboxElement, shadowyInputField, scope);
    update();
    shadowyInputField.onChange.listen((Event e)=>update());
    shadowyInputField.onClick.listen((Event e)=>toggle());

    addWatches(puiCheckboxElement, shadowyInputField, scope);

    scope.watch("ngmodel", (newVar, oldVar) => update());

  }

  /**
   * Called by onClick to toggle the value of the check box.
   */
  toggle() {
    if (true != disabled){
      if (disabled != false) {
        disabled=false;
      }
      if (null==ngmodel)
        ngmodel=true;
      else
        ngmodel=!ngmodel;
      update();
    }
  }

  /**
   * Updates the CSS classes to update model changes and the checkbox state.
   */
  update() {
    updateAttributesInShadowDOM(puiCheckboxElement, shadowyInputField, scope);
    if (true==ngmodel)
    {
      iconElement.classes.add("ui-icon");
      iconElement.classes.add("ui-icon-check");
    }
    else
    {
      iconElement.classes.remove("ui-icon");
      iconElement.classes.remove("ui-icon-check");
    }
    if (true==disabled)
    {
      shadowyInputField.classes.add("ui-state-disabled");
      iconElement.classes.add("ui-state-disabled");
    }
    else
    {
      shadowyInputField.classes.remove("ui-state-disabled");
      iconElement.classes.remove("ui-state-disabled");
    }
  }
}
