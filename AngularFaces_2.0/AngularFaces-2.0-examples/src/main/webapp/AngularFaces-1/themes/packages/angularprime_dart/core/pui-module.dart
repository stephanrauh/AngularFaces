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
library angularprime_dart;

import 'dart:html';
import 'package:angular/angular.dart';
import 'dart:async';
//import 'package:dart.dom.html';

import 'package:di/di.dart';
import '../puiPanel/pui-panel.dart';
import '../puiAccordion/pui-accordion.dart';
import '../puiTabview/pui-tabview.dart';

import '../puiDatatable/pui-datatable.dart';
import '../puiDatatable/pui-repeat.dart';

import '../puiInclude/pui-include.dart';
import '../puiDynamicHTML/pui-bind-html.dart';

import '../puiGrid/pui-grid.dart';

import '../puiInput/pui-input.dart';
import '../puiButton/pui-button.dart';
import '../puiCheckbox/pui-checkbox.dart';
import '../puiDropdown/pui-dropdown.dart';
import '../puiTextarea/pui-textarea.dart';
//import '../puiRadiobuttons/pui-radiobutton.dart';

import 'package:logging/logging.dart';

part 'pui-html-utils.dart';
part "pui-datatable-preparator.dart";

/**
 * AngularPrime-Dart applications can get access to every PUI component by deriving from this class.
 * Alternatively, you can add the components you need manually.
 */
class PuiModule extends Module {
  static NodeList nodesToBeWatched = null;

  /** Analyses each pui component of the DOM tree and add register attributes to be watched. */
  static void _findNodesToBeWatched()
  {
    List<String> puiElements = ['pui-button', 'pui-accordion',
                                'pui-datatable', 'pui-row', 'pui-column',
                                 'pui-checkbox', 'pui-dropdown','pui-input', 'pui-panel',
                                 'pui-radiobutton',
                                 'pui-tabview', 'pui-tab', 'pui-textarea'];
    List<HtmlElement> myComponents = puiElements.fold(null, (List<HtmlElement> list, String puiType) => _addTags(list, puiType));
    myComponents.forEach((HtmlElement n) => _registerAttributesToBeWatched(n));
  }

  /** Scans the entire document for a particular component type. */
  static List<HtmlElement> _addTags(List<HtmlElement> initial, String puiType) {
    HtmlCollection list = window.document.getElementsByTagName(puiType);
    List<Element> result = initial;
    if (null==result) result = new List<HtmlElement>();
    list.forEach((HtmlElement n) => result.add(n));
    return result;
  }

  /** Once Angular is started, we can only see the values of interpolated expressions.
   * The original expression is lost - at least to my current (Apr 03 2014) knowledge.
   * So we store the original expressions in special, non-interpolated attributes.
   */
  static void _registerAttributesToBeWatched(HtmlElement puiComponent)
  {
    String watches = "";
    puiComponent.attributes.forEach((String key, String value) {
      if (value.contains("{{") && value.contains("}}"))
      {
        watches+=" "+_innerExpression(value);
        // puiComponent.attributes.remove(key);
      }
     });

    if (watches.length>0)
    {
      puiComponent.attributes["pui-toBeWatched"]=watches.trim();
    }
  }

  /** Removes the braces from an interpolated expression. */
  static String _innerExpression(String value) {
    int start = value.indexOf("{{");
    int end = value.indexOf("}}", start);
    if (start>=0 && end>start)
    {
      return value.substring(start+2, end);
    }
    else return "";
  }

  /** Loads the file included via <pui-include> (if any) and initializes AngularDart after incorporating the include files' content */
  void _loadIncludeFiles()
  {
    HtmlCollection list = window.document.getElementsByTagName('pui-include');
    if (list.length==0)
    {
        startAngular();
    }
    else
    {
      List<Future> futures = new List<Future>();
      list.forEach((Element puiIncludeTag){
        String file=puiIncludeTag.attributes["file"];
        Future future = HttpRequest.getString(file).then((resp) {
          var includedElement = PuiHtmlUtils.parseResponse(resp);
          puiIncludeTag.children.add(includedElement);
        });
        futures.add(future);
      });
      Future.wait(futures).then((List l) {
         startAngular();});
    }
  }

  /**
   * Prepares the DOM tree prior to running up AngularDart.
   * For instance, pui-datatables are converted to a more angular style,
   * pui-includes are loaded and the formulas of interpolated expressions
   * are store for future use.
   */
  void startAngular() {
    prepareDatatables();
     _findNodesToBeWatched();
    bootStrap();
  }

  /**
   * This method is can be overwritten to accomodate more complex situations. By default,
   * ngBootstrag is called with the single parameter <code>this</code>.
   */
  void bootStrap() {
    ngBootstrap(module: this);
  }


  /** Registers the PUI modules, loads the pui-include files, takes care of any attribute that has to be watched and initializes AngularDart */
  PuiModule() {
    Logger.root.level = Level.FINEST;
    Logger.root.onRecord.listen((LogRecord r) { print(r.message); });

    type(PuiPanelComponent);
    type(PuiAccordionComponent);
    type(PuiTabviewComponent);
    type(PuiTabComponent);
    type(PuiGridComponent);

    type(PuiDatatableComponent);
    type(PuiRepeatDirective);
    type(PuiColumnHeaderComponent);
    type(PuiEmptyRowsFilter);
    type(PuiDatatableSortFilter);

    type(PuiIncludeComponent);
    type(PuiBindHtmlDirective);

    type(PuiInputTextComponent);
    type(PuiWeekComponent);
    type(PuiColorComponent);
    type(PuiButtonComponent);
    type(PuiCheckboxComponent);
    type(PuiDropdownComponent);
//    type(PuiRadiobuttonComponent);
    type(PuiTextareaComponent);

    scheduleMicrotask(() {
      _loadIncludeFiles();
    });
  }
}

