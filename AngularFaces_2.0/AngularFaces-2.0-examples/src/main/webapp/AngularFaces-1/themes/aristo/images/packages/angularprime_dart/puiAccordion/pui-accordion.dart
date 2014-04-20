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
library puiAccordion;

import 'package:angular/angular.dart';
import 'dart:html';
import 'dart:async';
import '../core/pui-base-component.dart';
//import 'package:animation/animation.dart';
//import 'package:animation/effect.dart';

/**
 * The pui-accordion component is a panel group that can be shrinked to a single line. Typically, there are multiple accordions on a dialog, and expanding one of them hides the other ones.
 */
@NgComponent(selector: 'pui-accordion',
    templateUrl:       'packages/angularprime_dart/puiAccordion/pui-accordion.html',
    cssUrl:            'packages/angularprime_dart/puiAccordion/pui-accordion.css',
    applyAuthorStyles: true,
    publishAs:         'cmp')
class PuiAccordionComponent  extends PuiBaseComponent implements NgShadowRootAware  {

  /** This mouse listener is exposed so it can be removed again. */
  StreamSubscription<MouseEvent> listenOnMouseEnter = null;

  /** This mouse listener is exposed so it can be removed again. */
  StreamSubscription<MouseEvent> listenOnMouseLeave = null;

  /** This mouse listener is exposed so it can be removed again. */
  StreamSubscription<MouseEvent> listenOnClick = null;

  /** The accordion panel's caption. */
  @NgAttr("header")
  String header;

  /** Is the accordion panel collapsible? */
  String _toggleable;

  @NgAttr("toggleable")
  set toggleable(String s) {
    _toggleable = s;
    addOrRemoveMouseListeners();
  }

  /** Is the accordion panel currently collapsed? */
  bool collapsedState = false;

  /** Is the accordion panel currently collapsed? */
  @NgTwoWay("collapsed")
  set collapsed(bool state) {
    collapsedState = (null != state && state);
    if (collapsedState) collapse(); else expand();
  }

  /** Is the accordion panel currently collapsed? */
  bool get collapsed => collapsedState;

  /** Allows accordions to collapse vertically (similar to the way Eclipse collapses views) */
  @NgAttr("toggleOrientation")
  String toggleOrientation = 'vertical';

  /** The HTML element displaying the caption. */
  HtmlElement titlebar;

  /** The HTML element displaying the content of the accordion. */
  HtmlElement content;

  /** The <pui-accordion> field as defined in the HTML source code. */
  Element puiAccordionElement;

  /** The scope is needed to add watches. */
  Scope scope;

  /**
   * Initializes the component by setting the <pui-accordion> field and setting the scope.
   */
  PuiAccordionComponent(this.scope, this.puiAccordionElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }

  /** Yields the caption unless the accordion has been collapsed horizontally. */
  String headerText() {
    if (collapsedState && "horizontal" == toggleOrientation) {
      return "";
    } else return header;
  }

  /** Depending on the collapse state a different icon is displayed. */
  String headerIconClass() => collapsedState ? "ui-icon-triangle-1-e" :
      "ui-icon-triangle-1-s";

  /** Toggles the accordion. */
  void toggle(MouseEvent event) {
    event.preventDefault();
    if (collapsedState) expand(); else collapse();
  }

  /** Expands the accordion. */
  void expand() {
    if (null == toggleOrientation || toggleOrientation == 'vertical') {
      _slideDown();
    } else if (toggleOrientation == 'horizontal') {
      _slideRight();
    }
    collapsedState = false;
  }


  /** Collapses the accordion. */
  void collapse() {
    if (null == toggleOrientation || toggleOrientation == 'vertical') {
      _slideUp();
    } else if (toggleOrientation == 'horizontal') {
      _slideLeft();
    }
    collapsedState = true;
  }

  /** @Todo: Close the accordion with a fancy effect. */
  void _slideUp() {
    if (null != content) {
      //slideOut(content, direction: "up")
      content.hidden = true;
    }
  }

  /** @Todo: Open the accordion with a fancy effect. */
  void _slideDown() {
    if (null != content) {
      //slideIn(content, direction: "down")
      content.hidden = false;
    }
  }

  /** @Todo: Close the accordion with a fancy effect. */
  void _slideLeft() {
    if (null != content) {
      int originalHeight = titlebar.clientHeight;
      content.hidden = true;
      titlebar.parent.style.width = "60px";
      titlebar.style.width = "36px";
      titlebar.style.height = originalHeight.toString() + "px";
    }
  }

  /** @Todo: Open the accordion with a fancy effect. */
  void _slideRight() {
    if (null != content) {
      content.hidden = false;
      titlebar.parent.style.width = "";
      titlebar.style.width = "";
    }
  }

  /** Initializes the accordion. */
  void onShadowRoot(ShadowRoot shadowRoot) {
    titlebar = shadowRoot.getElementsByClassName('pui-accordion-header')[0];
    content = shadowRoot.getElementsByClassName('pui-accordion-content')[0];
    if (collapsedState) {
      collapse();
    }
    addOrRemoveMouseListeners();
    addWatches(puiAccordionElement, null, scope);
  }

  /**
    * Copies a single attribute from the updated <pui-accordion> declaration - which contains
    * the current model values - to the shadow tree, and adds to the map of original attribute values.
    * In this case the collapsed state is updated.
    */
  void updateAttributeInShadowDOM(Element inputfield, String key, String value) {
    if ("collapsed"==key)
    {
      String newValueAsString = puiAccordionElement.attributes[key];
      bool newValue = "true"==newValueAsString;
      if (newValue!=collapsedState)
      {
        collapsed=newValue;
      }
    }
  }

  /** Adds or removes mouse lister each time the toggleability changes. */
  addOrRemoveMouseListeners() {
    if (titlebar != null) {
      if (_toggleable == null || _toggleable == "true") {

        listenOnMouseEnter = titlebar.onMouseEnter.listen((event) =>
            titlebar.classes.add('ui-state-hover'));
        listenOnMouseLeave = titlebar.onMouseLeave.listen((event) =>
            titlebar.classes.remove('ui-state-hover'));
        listenOnClick = titlebar.onClick.listen((event) => toggle(event));
      } else {
        if (null != listenOnMouseEnter) listenOnClick.cancel();
        if (null != listenOnMouseLeave) listenOnClick.cancel();
        if (null != listenOnClick) listenOnClick.cancel();
      }
    }

  }
}
