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

library puiTabview;

import '../core/pui-base-component.dart';
import 'package:angular/angular.dart';
import 'dart:html';

/**
 * A <pui-tabview> consists of a number of <pui-tabs>, each containing content that's hidden of shown
 * depending on whether the tab is active or not. Only one tab can be active at a time.
 *
 * The programming model is much like the API of the PrimeFaces <tabView> component.
 *
 * Usage:
 * <pui-tabview>
 *   <pui-tab title="first tab">
 *      content of first tab
 *   </pui-tab>
 *   <pui-tab title="default tab" selected="true">
 *      content of second tab
 *   </pui-tab>
 *   <pui-tab title="closable tab" closeable="true">
 *      content of closeable tab
 *   </pui-tab>
 * </pui-tabview>
 *
 * Kudos: This component's development was helped a lot by a stackoverflow answer:
 * http://stackoverflow.com/questions/20531349/struggling-to-implement-tabs-in-angulardart.
 */
@NgComponent(
  visibility: NgDirective.CHILDREN_VISIBILITY,
  selector: 'pui-tabview',
  templateUrl: 'packages/angularprime_dart/puiTabview/pui-tabview.html',
  cssUrl: 'packages/angularprime_dart/puiTabview/pui-tabview.css',
  applyAuthorStyles: true,
  publishAs: 'cmp'
)
class PuiTabviewComponent extends PuiBaseComponent implements NgShadowRootAware {

  /** Which tabs does this <pui-tabview> consist of? */
  List<PuiTabComponent> panes = new List();

  /** The <pui-input> field as defined in the HTML source code. */
  Element puiTabviewElement;

  /** The scope is needed to add watches. */
  Scope scope;

  /**
   * Initializes the component by setting the <pui-tabview> field and setting the scope.
   */
  PuiTabviewComponent(this.scope, this.puiTabviewElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {}

  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    copyAttributesToShadowDOM(puiTabviewElement, null, scope);
    addWatches(puiTabviewElement, null, scope);
  }

  /** This is the mouse click listener activating a certain tab. */
  select(MouseEvent evt, PuiTabComponent selectedPane ) {
    for (PuiTabComponent pane in panes) {
      pane.setSelected(pane == selectedPane);
    }
    evt.preventDefault();
  }

  /** This is the mouse click listener closing a certain tab. */
  close(MouseEvent evt, PuiTabComponent toBeClosed) {
    bool hasBeenSelected = toBeClosed.isSelected;
    int index = panes.indexOf(toBeClosed);
    if (hasBeenSelected)
    {
      toBeClosed.setSelected(false); // hide the contents
      if (panes.length>0)
      {
        if (index==0)
          panes[0].setSelected(true);
        else
          panes[index-1].setSelected(true);
      }
    }
    panes.removeAt(index);
    evt.preventDefault();
  }


  /** This method is called automatically by the <pui-tabs> when they register themselves to the <pui-tabview>. */
  add(PuiTabComponent pane) {
    panes.add(pane);
  }
}

/** <pui-tab> is a single tab. The <pui-tabview> consists of several <pui-tabs>. */
@NgComponent(
    selector: 'pui-tab',
    templateUrl: 'packages/angularprime_dart/puiTabview/pui-tab.html',
    cssUrl: 'packages/angularprime_dart/puiTabview/pui-tabview.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiTabComponent {

  /** The caption of the tab. */
  @NgAttr("title")
  String name;

  /** Can the tab be closed? */
  bool _closeable;

  /** Can the tab be closed? */
  @NgAttr("closeable")
  set closeable(String s){_closeable="true"==s; }

  /** The parent <pui-tabview>. Needed to register the tab. */
  PuiTabviewComponent _tab;

  /** Is the current tab active? */
  bool _selected = false;

  /** The constructor of the tab initializes the reference to the parent <pui-tabview>, the scope
   * and the HTML code of the <pui-tab> itself.
   */
  PuiTabComponent(this._tab) {
    _tab.add(this);
  }

  /** Is the current tab active? This method is exposed to the declaration in the HTML code. */
  @NgAttr("selected")
  set isSelectedByDefault(String selected) {
    _selected = selected == "true";
  }

  /** Is the current tab active? */
  bool get isSelected => _selected;

  /** Is the current tab active? */
  void setSelected(bool b){_selected=b;}

  /** returns the CSS class PrimeUI uses to display an activated or deactivated tab. */
  String selectedClass() {
    return _selected ? "pui-tabview-selected ui-state-active" : "";
  }

  /** returns the CSS class to hide a tab's content. */
  String hiddenClass(){
     return _selected ? "" : "ui-helper-hidden";
  }

  /** returns the CSS class to hide a tab's close button. */
  String closeableClass(){
     return _closeable ? "" : "ui-helper-hidden";
  }

}
