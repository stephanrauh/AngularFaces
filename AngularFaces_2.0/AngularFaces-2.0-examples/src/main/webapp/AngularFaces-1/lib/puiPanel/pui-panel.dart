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
library puiPanel;

import 'package:angular/angular.dart';
import 'dart:html';
import '../core/pui-base-component.dart';
//import 'package:animation/animation.dart';
//import 'package:animation/effect.dart';


@NgComponent(
    selector: 'pui-panel',
    templateUrl: 'packages/angularprime_dart/puiPanel/pui-panel.html',
    cssUrl: 'packages/angularprime_dart/puiPanel/pui-panel.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiPanelComponent  extends PuiBaseComponent implements NgShadowRootAware  {
  @NgAttr("header")
  String header;

  @NgAttr("toggleable")
  String toggleable;

  @NgAttr("collapsed")
  String collapsed = 'false';

  @NgAttr("toggleOrientation")
  String toggleOrientation = 'vertical';

  HtmlElement titlebar;
  HtmlElement content;
  bool collapsedState;
  HtmlElement toggler;


  /** The <pui-panel> field as defined in the HTML source code. */
  Element puiPanelElement;

  /** The scope is needed to add watches. */
  Scope scope;

  /**
   * Initializes the component by setting the <pui-panel> field and setting the scope.
   */
  PuiPanelComponent(this.scope, this.puiPanelElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }


  void convertAttributes() {
    collapsedState = false;
    if (null!= collapsed && "true" == collapsed.trim().toLowerCase()) {
      collapsedState = true;
    }
  }

  String determineCollapsedIcon() {
    return collapsedState ? 'ui-icon-plusthick' : 'ui-icon-minusthick';
  }

  void toggle(MouseEvent event) {
    event.preventDefault();
    if(collapsedState) {
      _expand();
    }
    else {
      _collapse();
    }

  }

  void _expand() {
    SpanElement toggleSpan = toggler.querySelector('span.ui-icon');
    toggleSpan.classes.add('ui-icon-minusthick');
    toggleSpan.classes.remove('ui-icon-plusthick');

    if(null == toggleOrientation || toggleOrientation == 'vertical') {
      _slideDown();
    }
    else if(toggleOrientation == 'horizontal') {
      _slideRight();
    }
    collapsedState = false;
  }


  void _collapse() {
    SpanElement toggleSpan = toggler.querySelector('span.ui-icon');
    toggleSpan.classes.add('ui-icon-plusthick');
    toggleSpan.classes.remove('ui-icon-minusthick');


    if(null == toggleOrientation || toggleOrientation == 'vertical') {
      _slideUp();
    }
    else if(toggleOrientation == 'horizontal') {
     _slideLeft();
    }
    collapsedState = true;
  }

  void _slideUp() {
    //slideOut(content, direction: "up")
    content.hidden = true;
   }

  void _slideDown() {
    //slideIn(content, direction: "down")
    content.hidden = false;
  }

  void _slideLeft() {
    content.hidden = true;
    titlebar.parent.style.width="60px";
    titlebar.style.width="20px";
    HtmlElement text=titlebar.firstChild;
    text.hidden=true;

  }

  void _slideRight() {
    content.hidden = false;
    titlebar.parent.style.width="";
    titlebar.style.width="";
    HtmlElement text=titlebar.firstChild;
    text.hidden=false;
  }

  /** Adds toggle functionality to the HTML code */
  void onShadowRoot(ShadowRoot shadowRoot) {
    titlebar = shadowRoot.querySelector('.pui-panel-titlebar');
    content = shadowRoot.querySelector('.pui-panel-content');
    convertAttributes();

    if (toggleable == "true") {

      toggler = new AnchorElement(href : '#');
      toggler.classes.add('pui-panel-titlebar-icon');
      toggler.classes.add('ui-corner-all');
      toggler.classes.add('ui-state-default');

      SpanElement toggleSpan = new SpanElement();
      toggleSpan.classes.add('ui-icon');
      toggleSpan.classes.add(determineCollapsedIcon());

      toggler.children.add(toggleSpan);

      DivElement titleBar = shadowRoot.querySelector('.pui-panel-titlebar');
      titleBar.children.add(toggler);

      toggler.onMouseEnter.listen(
          (event) => toggler.classes.add('ui-state-hover'));
      toggler.onMouseLeave.listen(
          (event) => toggler.classes.remove('ui-state-hover'));
      toggler.onClick.listen(
          (event) => toggle(event));

    }
  }
}