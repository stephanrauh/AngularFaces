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
import 'dart:async';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';

/**
 * pui-buttons are
 */
@NgComponent(
    selector: 'pui-button',
    templateUrl: 'packages/angularprime_dart/puiButton/pui-button.html',
    cssUrl: 'packages/angularprime_dart/puiButton/pui-button.css',
    applyAuthorStyles: true,
    publishAs: 'cmp')
class PuiButtonComponent extends PuiBaseComponent implements NgShadowRootAware {

  /** The button's caption */
  @NgAttr("value")
  String value;

  /** optional: the button's icon */
  @NgAttr("icon")
  String icon;

  /** optional: the button's icon's position. Legal values: "right" and "left" (=default). */
  @NgAttr("iconPos")
  String iconPos;

  /** optional: if set to "true", the button is disabled and doesn't react to being clicked. */
  @NgAttr("disabled")
  String disabled;

  /** optional: the name of a Dart function called when the button is clicked.
   * Similar to ng-click (but more natural to PrimeFaces programmers).
   * Note that onClick is also a legal attribute, only it calls a Javascript function instead of a Dart function!
   */
  @NgCallback("actionListener")
  Function actionListener;

  /** The scope is needed to add watches. */
  Scope scope;

  /** The <puiButton> as defined in the user's source code */
  Element puiButton;

  /** The button component within the shadow tree */
  ButtonElement button;

  /** Constructor. */
  PuiButtonComponent(this.scope, this.puiButton, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {}

  /** This actionListener to stored so it can be disabled if the button is disabled */
  StreamSubscription onMouseEnter=null;

  /** This actionListener to stored so it can be disabled if the button is disabled */
  StreamSubscription onMouseLeave=null;


  /**
   * This method is called after the construction of the shadow DOM to provide advanced
   * features that are too difficult to express in pure HTML.
   * <ul>
   * <li>activates the actionListener</li>
   * <li>add the optional icon</li>
   * <li>add the disabled class (if needed)</li>
   * <li>add watches to copy interpolated attributes to the shadow DOM</li>
   * </ul>
   * */
  void onShadowRoot(ShadowRoot shadowRoot) {
    button = shadowRoot.querySelector('button');

    if (actionListener != null) {
      button.onClick.listen((e) {
        // When the button is clicked, it runs this code.
        if (disabled==null || disabled=="false") {
          actionListener();
        }
      });
    }

    updateMouseOverListeners();
    scope.watch("disabled", (newVar, oldVar) { updateMouseOverListeners();});

    if (icon == null) {
      button.classes.add('pui-button-text-only');
    } else {
      drawIcon();
    }
    addWatches(puiButton, button, scope);
  }

  /**
   * Sets either the disabled class or the mouse over effects.
   * Called during the initialization of the button and when the disabled attribute has been modified.
   */
  void updateMouseOverListeners() {
    if (disabled!=null)
    {
      button.classes.add("ui-state-disabled");
      if (null != onMouseEnter) onMouseEnter.cancel();
      if (null != onMouseLeave) onMouseLeave.cancel();
    }
    else
    {
      onMouseEnter = button.onMouseEnter.listen((event) { button.classes.add('ui-state-hover');});
      onMouseLeave = button.onMouseLeave.listen((event) { button.classes.remove('ui-state-hover');});
    }
  }

  /** adds the icon as a span element */
  void drawIcon() {

    if ("right" == iconPos) {
      button.classes.add('pui-button-text-icon-right');
    } else {
      button.classes.add('pui-button-text-icon-left');
    }
    SpanElement iconSpan = new SpanElement();
    iconSpan.classes.add(iconSpanClass());
    button.children.insert(0, iconSpan);

  }

  /** return the CSS class defining the icon position */
  String iconPosClass() {
    if (null==icon) return "pui-button-text-only";
    String c = "pui-button-icon-left";
    if ("right" == iconPos) {
      c = "pui-button-icon-right";
    }
    return c;
  }

  /** returns the CSS classes defining the icon */
  String iconSpanClass()
  {
    String c = "pui-button-icon-left";
    if ("right" == iconPos) {
      c = "pui-button-icon-right";
    }

    c+= " ui-icon ";
    if (icon.startsWith("ui-icon-"))
      c+=icon;
    else
      c += "ui-icon-$icon";
    return c;
  }
}
