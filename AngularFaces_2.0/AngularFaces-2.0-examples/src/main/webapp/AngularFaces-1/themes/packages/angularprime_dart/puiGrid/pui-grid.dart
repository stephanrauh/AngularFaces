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
library puiGrid;

import 'dart:html';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';

/**
 * <pui-grid> makes it a little easier to create nice input dialogs.
 */
@NgComponent(
    selector: 'pui-grid',
    templateUrl: 'packages/angularprime_dart/puiGrid/pui-grid.html',
    cssUrl: 'packages/angularprime_dart/puiGrid/pui-grid.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiGridComponent extends PuiBaseComponent implements NgShadowRootAware  {

  /** The <pui-grid> field as defined in the HTML source code. */
  Element puiGridComponent;

  /**
   * Initializes the component by setting the <pui-input> field and setting the scope.
   */
  PuiGridComponent(this.puiGridComponent, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {}



  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   *
   * @Todo Find out, which attributes are modified by Angular, and set a watch updating only the attributes that have changed.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    DivElement table = shadowRoot.children[1];
    List rows = table.children;
    List fields = puiGridComponent.children;
    var numberOfFields = fields.length;
//    print("Adding rows to pui-grid");
    for (int i = 1; i < numberOfFields; i++)
    {
        rows.add(rows[0].clone(true));
    }
//    print("Adding fields to pui-grid");
    for (int i = 0; i < numberOfFields; i++)
    {
//      print("Adding field #$i");
      DivElement currentRow = rows[i];
      HtmlElement currentField = fields[0]; // no typo - each time
      // a field is added to the shadow DOM it's removed from the fields array

      String label = currentField.attributes["label"];
      if (null == label && null != currentField.attributes["ng-model"])
      {
        label = currentField.attributes["ng-model"];
      }
      if (label!=null)
      {
        currentRow.children[0].children[0].innerHtml= label;
      }
      if (currentField.id==null || currentField.id=="")
      {
        currentField.id = "puiinputid:" + currentField.hashCode.toString();
      }
      currentRow.children[0].children[0].attributes["for"]=currentField.id;

      currentRow.children[1].append(currentField);
    }
  }
}

