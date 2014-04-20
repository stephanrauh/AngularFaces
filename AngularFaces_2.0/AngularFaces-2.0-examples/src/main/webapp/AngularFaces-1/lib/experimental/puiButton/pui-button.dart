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
library puiButton;

import 'dart:html';
import 'package:angular/angular.dart';

@NgComponent(
    selector:    'pui-button',
    templateUrl: 'packages/angularprime_dart/puiButton/pui-button.html',
    cssUrl:      'packages/angularprime_dart/puiButton/pui-button.css',
    publishAs:   'cmp')
class PuiButtonComponent extends NgShadowRootAware {

  @NgAttr("onClick")
  String onClick;

  @NgAttr("value")
  String value;

  @NgAttr("icon")
  String icon;

  @NgAttr("iconPos")
  String iconPos;

  /** The scope is needed to add watches. */
  Scope scope;

  Element puiButton;

  ButtonElement button;

  PuiButtonComponent(this.scope, this.puiButton) {}

  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowRoot.applyAuthorStyles = true;
    button = shadowRoot.querySelector('button');

    //    button.onMouseEnter.listen((event) { button.classes.add('ui-state-hover');});
    //    button.onMouseLeave.listen((event) { button.classes.remove('ui-state-hover');});
  }

  String iconPosClass() {
    if (null==icon) return "pui-button-text-only";
    String c = "pui-button-icon-left";
    if ("right" == iconPos) {
      c = "pui-button-icon-right";
    }
    return c;
  }

  String iconSpanClass()
  {
    String c = "pui-button-icon-left ui-icon ";
    if (icon.startsWith("ui-icon-"))
      c+=icon;
    else
      c += "ui-icon-$icon";
    print(c);
    return c;
  }

  bool showIcon()
  {
    return null != icon;
  }

}
