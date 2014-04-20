/**
 * (C) 2014 Stephan Rauh http://www.beyondjava.net
 *
 * Code originally taken from AngularDart 0.9.9 ng_bind_html.dart.
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

library puiBind;

import 'dart:html';
import 'package:angular/angular.dart';
import '../core/pui-module.dart';


/**
 * Creates a binding that will innerHTML the result of evaluating the
 * `expression` bound to `pui-bind-html` into the current element in a secure
 * way.  This expression must evaluate to a string.  The difference to
 * ng-bind-html is that the innerHTML-ed content
 * is not sanitized using any default [NodeValidator]. So use it carefully!
 *
 * Example:
 *
 *     <div pui-bind-html="htmlVar"></div>
 */
@NgComponent(
  visibility: NgDirective.CHILDREN_VISIBILITY,
  selector: 'pui-bind-html',
  template: '',
  applyAuthorStyles: true,
  publishAs: 'cmp'
)
class PuiBindHtmlDirective implements NgShadowRootAware {
  final Element element;

  Scope _scope;

  PuiBindHtmlDirective(this.element, this._scope);

  /**
   * Parsed expression from the `pui-bind-html` attribute.  The result of this
   * expression is innerHTML'd according to the rules specified in this class'
   * documentation.
   */
  @NgOneWay("html")
  String innerHtml;

  @override
  void onShadowRoot(ShadowRoot shadowRoot) {
    updateInnerHTML(shadowRoot);
    _scope.watch("cmp.innerHtml", (oldVar, newVar){updateInnerHTML(shadowRoot);});
  }

  void updateInnerHTML(ShadowRoot shadowRoot) {
    shadowRoot.children.clear();
    if (innerHtml!=null && innerHtml.length>0)
    {
      try
      {
        Element element = PuiHtmlUtils.parseResponse(innerHtml);
        shadowRoot.children.add(element);
      }
      catch (exception)
      {
        print("An exception has occured." + exception.toString());
      }
    }
  }
}
