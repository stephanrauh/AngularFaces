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
library puiDropdown;

import 'dart:html';
import 'dart:async';
import 'package:angular/angular.dart';
import '../core/pui-base-component.dart';

/**
 * <pui-dropdown> is a drop-down menue (aka combo-box) resembling the selectOneMenu component of PrimeFaces.
 */
@NgComponent(
    selector: 'pui-dropdown',
    templateUrl: 'packages/angularprime_dart/puiDropdown/pui-dropdown.html',
    cssUrl: 'packages/angularprime_dart/puiDropdown/pui-dropdown.css',
    applyAuthorStyles: true,
    publishAs: 'cmp'
)
class PuiDropdownComponent extends PuiBaseComponent implements NgShadowRootAware  {
  /** The <pui-input> field as defined in the HTML source code. */
  Element puiInputElement;

  /** The <input> field in the shadow DOM displaying the component if the drop-down menu is editable. */
  InputElement shadowyInputField;

  /** The <input> field in the shadow DOM displaying the component if the drop-down menu is not editable. */
  LabelElement shadowySelection;

  /** This panel contains the list of predefined values. */
  DivElement dropDownPanel;

  /** We need this variable to hide a recently opened drop down panel before showing a new one */
  static DivElement currentlyOpenedDropDownPanel;

  /** put the predefined values into this container */
  UListElement dropDownItems;


  /** <pui-dropdown> fields require an ng-model attribute. */
  @NgTwoWay("ng-model")
  String ngmodel;

  /** The clear text value that's displayed to the user (instead of the internal ng-model) */
  String displayedValue;

  /** Is it an editable field, or is it restricted to the predefined list of options? */
  @NgOneWay("editable")
  bool editable;

  @NgOneWay("options")
  Map<String, String> options;

  /** The scope is needed to add watches. */
  Scope scope;

  /** Map containing the selectable items of the drop down menu */
  Map<String, String> predefinedOptions = new Map<String, String>();

  /** Map containing the selectable items of the drop down menu */
  List<String> predefinedValuesAsList = new List<String>();

  /** The mouse click listener is needed to close the drop down menu by clicking somewhere outside. It has to be static
   * to be able to cancel it from another drop down menu.
   */
  static StreamSubscription<MouseEvent> mouseClickListenerSubscription=null;


  /**
   * Initializes the component by setting the <pui-dropdown> field and setting the scope.
   */
  PuiDropdownComponent(this.scope, this.puiInputElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }

  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   *
   * @Todo Find out, which attributes are modified by Angular, and set a watch updating only the attributes that have changed.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowyInputField = shadowRoot.getElementsByTagName("input")[0];
    shadowySelection = shadowRoot.getElementsByTagName("label")[0];
    shadowySelection.parent.onClick.listen((Event) => toggleOptionBox());
    dropDownPanel = shadowRoot.getElementsByClassName("pui-dropdown-panel")[0];
    dropDownItems = shadowRoot.getElementsByClassName('pui-dropdown-items')[0];

    if (editable==true) {
      shadowySelection.style.display="none";
      shadowySelection.classes.add("ui-helper-hidden");
      shadowyInputField.onKeyDown.listen((KeyboardEvent e){keyListener(e);});
    }
    else {
      shadowyInputField.style.display="none";
      shadowyInputField.classes.add("ui-helper-hidden");
      // Todo: shadowySelection.onKeyDown.listen((KeyboardEvent e){keyListener(e);});
    }

    var children = puiInputElement.children;
    children.forEach((Element option) => add(option));
    scope.watch("cmp.options", (newVar, oldVar) {addVariableOptions(children.length);});
//    addVariableOptions();

    copyAttributesToShadowDOM(puiInputElement, shadowyInputField, scope);
    scope.watch("cmp.ngmodel", (newVar, oldVar) => updateDisplayedValue());
    scope.watch("cmp.displayedValue", (newVar, oldVar) => updateNgModel());
    addWatches(puiInputElement, shadowyInputField, scope);

  }

  void addVariableOptions(int optionsToBeRetained) {
    while(dropDownItems.children.length>optionsToBeRetained)
    {
      dropDownItems.children.removeLast();
    }

    if (options!=null)
    {
      options.keys.forEach((String key){
        addOption(key, options[key]);
      });
    }
  }

  /**
   * Enables keyboard navigation.
   */
  keyListener(KeyboardEvent e) {
    if (e.keyCode==KeyCode.ENTER)
    {
      toggleOptionBox();
      return;
    }

    int currentSelectedIndex=-1; // undefined value as default
    if (displayedValue!=null && displayedValue.length>0)
    {
      for (int i = 0; i < predefinedValuesAsList.length;i++)
      {
        if (predefinedValuesAsList[i]==displayedValue) {
          currentSelectedIndex=i;
          break;
        }
      }
    }
    int newSelectedIndex=-1; // undefined value as default
    if (e.keyCode==KeyCode.UP)
    {
      if (currentSelectedIndex<=0)
        newSelectedIndex=predefinedValuesAsList.length-1;
      else
        newSelectedIndex= currentSelectedIndex-1;
    }
    else if (e.keyCode==KeyCode.DOWN)
    {
      if (currentSelectedIndex<0 || (currentSelectedIndex>=predefinedValuesAsList.length-1))
        newSelectedIndex=0;
      else
        newSelectedIndex = currentSelectedIndex+1;
    }
    else if (editable==true){
      // Todo: find the entry matching the entered character
    }
    if (newSelectedIndex>= 0)
    {
      displayedValue=predefinedValuesAsList[newSelectedIndex];
      updateNgModel();
      dropDownItems.children.forEach((LIElement li) => li.classes.remove("ui-state-highlight"));
      dropDownItems.children[newSelectedIndex].classes.add('ui-state-highlight');
    }

  }

  /**
   * Every change of the angular model has to result in a change of the displayed and the selected value of the drop down menu.
   */
  updateNgModel() {
     if (predefinedOptions.containsValue(displayedValue))
     {
       predefinedOptions.forEach((K, V) => (ngmodel=V==displayedValue?K:ngmodel));
     }
     else
       ngmodel=displayedValue;
  }

  /**
   * Every key stroke in the input field has to modify the angular model.
   */
  updateDisplayedValue() {
    if (predefinedOptions.containsKey(ngmodel))
      displayedValue=predefinedOptions[ngmodel];
    else
      displayedValue=ngmodel;
  }

  /**
   * Adds an item to the drop down menu.
   */
  add(Element option) {
    String v = option.value;
    String description = option.innerHtml;
    addOption(description, v);

  }

  void addOption(String description, String v) {
    LIElement li = new LIElement();
    li.attributes["data-label"] =description;
    li.classes.add('pui-dropdown-item pui-dropdown-list-item ui-corner-all pui-dropdown-label');
    li.innerHtml=description;
    li.onClick.listen((Event) => select(li, v, description));
    dropDownItems.children.add(li);
    predefinedOptions[v]=description;
    predefinedValuesAsList.add(description);
  }

  /**
   * Highlight the selected item (while removing the highlight effect from the previously selected item).
   */
  select(LIElement selectedLIElement, String v, String description) {
    dropDownItems.children.forEach((LIElement li) => li.classes.remove("ui-state-highlight"));
    ngmodel=v;
    displayedValue=description;
    selectedLIElement.classes.add('ui-state-highlight');
    toggleOptionBox();
  }

  /**
   * Show or hide the menu. Hide any menu that's previously been active.
   */
  void toggleOptionBox()
  {
    String s = dropDownPanel.style.display;
    String v = dropDownPanel.style.visibility;

    if (s.contains("none")) {
      cancelMouseClickListener();
      if (null != currentlyOpenedDropDownPanel)
      {
        currentlyOpenedDropDownPanel.style.display="none";
      }
      dropDownPanel.style.display="block";
      currentlyOpenedDropDownPanel=dropDownPanel;
      dropDownPanel.parent.onMouseOver.listen((Event e){cancelMouseClickListener();});
      dropDownPanel.parent.onMouseOut.listen((Event e){activateMouseClickListener();});
    }
    else {
      dropDownPanel.style.display="none";
      currentlyOpenedDropDownPanel=null;
      cancelMouseClickListener();

    }
  }

  void cancelMouseClickListener() {
    if (null != mouseClickListenerSubscription)
    {
      mouseClickListenerSubscription.cancel();
      mouseClickListenerSubscription=null;
    }
  }

  /** Mouse clicks outside the drop down menus close the menu. */
  activateMouseClickListener() {
    if (null == mouseClickListenerSubscription)
    {
      if (dropDownPanel.style.display=="block")
      {
        mouseClickListenerSubscription = window.onClick.listen((MouseEvent e) {
          if (dropDownPanel.style.display=="block") toggleOptionBox();
            });
        dropDownPanel.onKeyDown.listen((KeyEvent e){keyListener(e);});
      }
    }
  }
}

