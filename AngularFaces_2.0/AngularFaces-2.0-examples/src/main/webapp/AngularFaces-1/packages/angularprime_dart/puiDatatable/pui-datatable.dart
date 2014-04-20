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

library puiDatatable;

import '../core/pui-base-component.dart';
import '../core/pui-module.dart';
import 'package:angular/angular.dart';
import 'dart:html';
import 'dart:async';
part "pui-column-header.dart";

/**
 * A <pui-datatable> consists of a number of <pui-tabs>, each containing content that's hidden of shown
 * depending on whether the tab is active or not. Only one tab can be active at a time.
 *
 * The programming model is much like the API of the PrimeFaces <tabView> component.
 *
 * Usage:
 * <pui-datatable>
 *   <pui-tab title="first tab">
 *      content of first tab
 *   </pui-tab>
 *   <pui-tab title="default tab" selected="true">
 *      content of second tab
 *   </pui-tab>
 *   <pui-tab title="closable tab" closeable="true">
 *      content of closeable tab
 *   </pui-tab>
 * </pui-datatable>
 *
 * Kudos: This component's development was helped a lot by a stackoverflow answer:
 * http://stackoverflow.com/questions/20531349/struggling-to-implement-tabs-in-angulardart.
 */
@NgComponent(visibility: NgDirective.CHILDREN_VISIBILITY,
    selector: 'pui-datatable',
    templateUrl: 'packages/angularprime_dart/puiDatatable/pui-datatable.html',
    cssUrl:       'packages/angularprime_dart/puiDatatable/pui-datatable.css',
    applyAuthorStyles: true,
    publishAs: 'cmp')
class PuiDatatableComponent extends PuiBaseComponent implements
    NgShadowRootAware {

  /** The <pui-input> field as defined in the HTML source code. */
  Element puiDatatableElement;

  /** This is the table content that's really displayed. */
  DivElement shadowTableContent;

  /** The scope is needed to add watches. */
  Scope scope;

  /** List of the columns registered until now */
  List<Column> columnHeaders = new List<Column>();

  /** The list to be displayed */
  @NgTwoWay("value")
  List myList;

  @NgAttr("initialsort")
  String initialsort;

  @NgAttr("initialsortorder")
  String initialsortorder;

  bool initialized = false;


  /**
   * Initializes the component by setting the <pui-datatable> field and setting the scope.
   */
  PuiDatatableComponent(this.scope, this.puiDatatableElement, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
    String id = puiDatatableElement.attributes["puiTableID"];
    String listName=puiDatatableElement.attributes["puiListVariableName"];
    PuiDatatableSortFilter.register("$listName$id", this);
  }

  /**
   * Make the global CSS styles available to the shadow DOM, copy the user-defined attributes from
   * the HTML source code into the shadow DOM and see to it that model updates result in updates of the shadow DOM.
   */
  void onShadowRoot(ShadowRoot shadowRoot) {
    shadowTableContent = shadowRoot.getElementById("pui-content");
    addHeaderRow();
    addFilterRow();
    addFooterRow();
  }

  /**
   * Popolates the header of the data table with the columns' captions.
   */
  void addHeaderRow() {
    DivElement header = shadowTableContent.querySelector("#headerRow");
    columnHeaders.forEach((Column col) {
      _addColumnToHeader(header, col);
    });

  }

  /**
   * Shows a row containing column filters, or hides it, if there's no filter.
   */
  void addFilterRow() {
    var filterRow = shadowTableContent.querySelector("#filterRow");
    if (columnHeaders.any((Column col) => col.filterby!=null && col.filterby!=""))
    {
      columnHeaders.forEach((Column col) {
        _addColumnToFilter(filterRow, col);
      });
    }
    else
    {
      filterRow.style.display="none";
    }
  }

  /** Shows a row below the data table containing optional footers, or hides the row if it's not needed. */
  void addFooterRow() {
    var footerRow = shadowTableContent.querySelector("#footerRow");
    if (columnHeaders.any((Column col) => col.footerText!=null && col.footerText!=""))
    {
      columnHeaders.forEach((Column col) {
        _addColumnToFooter(footerRow, col);
      });
    }
    else
    {
      footerRow.style.display="none";
    }

  }

  /** Adds a single column filter cell. */
  void _addColumnToFilter(DivElement filter, Column col) {
    DivElement captionCell = new DivElement();
    captionCell.classes.add("pui-datatable-th");
    captionCell.classes.add("ui-widget-header");

    captionCell.style.display = "table-cell";
    captionCell.attributes["role"] = "columnfooter";
    captionCell.style.whiteSpace = "nowrap";
    if (null != col.filterby && col.filterby!="")
    {
      DivElement overlay = new DivElement();
      overlay.innerHtml = "filter by ${col.header}";
      overlay.style.position="relative";
      overlay.style.color="#BBB";
      overlay.style.zIndex="99";
      InputElement f = new InputElement(type: "text");
      f.style.position="relative";
      f.style.top="-20px";
      f.style.marginBottom="-20px";
      f.style.zIndex="0";
      captionCell.children.add(overlay);
      captionCell.children.add(f);

      // activate input field on click
      overlay.onClick.listen((e){overlay.style.zIndex="-99"; f.focus();});

      f.style.float = "left";
      f.onKeyUp.listen((KeyboardEvent e) {
        if (overlay.style.display!="none" && f.value.length>0)
        {
          overlay.style.zIndex="-99";
          col.currentFilter=f.value;
        }
        else if (f.value.length==0)
        {
          overlay.style.zIndex="99";
          col.currentFilter="";
        }
        else
        {
          col.currentFilter=f.value;
        }
        triggerFiltering();
        e.preventDefault();
      });
    }
    filter.children.add(captionCell);
  }

  /** Adds a single table footer cell. */
  void _addColumnToFooter(DivElement footer, Column col) {
    DivElement captionCell = new DivElement();
    captionCell.classes.add("pui-datatable-th");
    captionCell.classes.add("ui-widget-header");

    captionCell.style.display = "table-cell";
    captionCell.attributes["role"] = "columnfooter";
    captionCell.style.whiteSpace = "nowrap";
    SpanElement caption = new SpanElement();
    caption.innerHtml = col.footerText;
    caption.style.float = "left";
    captionCell.children.add(caption);
    footer.children.add(captionCell);
  }

  /** Adds a single table header cell. */
  void _addColumnToHeader(DivElement header , Column col) {
    header.attributes["role"] = "row";
    DivElement captionCell = new DivElement();
    captionCell.classes.add("pui-datatable-th");
    captionCell.classes.add("ui-widget-header");

    captionCell.style.display = "table-cell";
    captionCell.attributes["role"] = "columnheader";
    captionCell.style.whiteSpace = "nowrap";
    SpanElement caption = new SpanElement();
    caption.innerHtml = col.header;
    caption.style.float = "left";
    captionCell.children.add(caption);


    header.children.add(captionCell);
    if (col.closable) {
      SpanElement closeIcon = new SpanElement();
      closeIcon.style.float = "right";
      closeIcon.classes.add("ui-icon");
      closeIcon.classes.add("ui-icon-close");
      closeIcon.onClick.listen((MouseEvent event) {
        closeColumn(event, captionCell);
      });
      captionCell.children.add(closeIcon);
    }
    if (col.sortable) {
      SpanElement sortIcon = new SpanElement();
      sortIcon.style.float = "right";
      sortIcon.classes.add("ui-icon");
      sortIcon.classes.add("ui-sortable-column-icon");
      sortIcon.classes.add("ui-icon-carat-2-n-s");
      sortIcon.onClick.listen((MouseEvent event) {
        _sortColumn(captionCell);
      });
      captionCell.children.add(sortIcon);

    }
  }

  /** Called by the action listener of the close button. Hides a particular column. */
  closeColumn(MouseEvent event, DivElement close) {
    DivElement headerRow = shadowTableContent.children[0];
    int index = 0;
    for ( ; index < headerRow.children.length; index++) {
      if (headerRow.children[index] == close) {
        columnHeaders[index].hidden = true;
        ElementList headers = shadowTableContent.querySelectorAll(".pui-datatable-th");
        headers[index].style.display = "none";
        ElementList rows = puiDatatableElement.querySelectorAll(".tr");
        rows.forEach((Element row) {
          if (!row.classes.contains("ui-datatable-empty-message"))
            row.children[index].style.display = "none";
        });
        break;
      }
    }
    _drawLeftBoundaryLine();
  }

  _sortColumn(DivElement sortColumn) {
    // ugly hack to force the ng-repeat watch to fire
    triggerFiltering();
    // end of the ugly hack
    DivElement headerRow = shadowTableContent.children[0];
    int index = 0;
    for ( ; index < headerRow.children.length; index++) {
      if (headerRow.children[index] == sortColumn) {
        int dir = columnHeaders[index].sortDirection;
        bool sortUp;
        HtmlCollection iconDivs =
            headerRow.children[index].getElementsByClassName("ui-sortable-column-icon");
        if (dir == 0) {
          iconDivs[0].classes.remove("ui-icon-carat-2-n-s");
          iconDivs[0].classes.add("ui-icon-triangle-1-n");
          dir = 1;
        } else if (dir == 1) {
          iconDivs[0].classes.remove("ui-icon-triangle-1-n");
          iconDivs[0].classes.add("ui-icon-triangle-1-s");
          dir = 2;
        } else {
          iconDivs[0].classes.remove("ui-icon-triangle-1-s");
          iconDivs[0].classes.add("ui-icon-triangle-1-n");
          dir = 1;
        }
        columnHeaders[index].sortDirection = dir;
      } else {
        columnHeaders[index].sortDirection=0;
        // remove sort icon (if necessary)
        HtmlCollection iconDivs =
            headerRow.children[index].getElementsByClassName("ui-sortable-column-icon");
        if (iconDivs.isNotEmpty) {
          iconDivs[0].classes.add("ui-icon-carat-2-n-s");
          iconDivs[0].classes.remove("ui-icon-triangle-1-n");
          iconDivs[0].classes.remove("ui-icon-triangle-1-s");
        }
      }
    }
  }

  /** ugly hack to force the ng-repeat watch to fire */
  void triggerFiltering() {
    // ugly hack to force the ng-repeat watch to fire
    if (myList.any((e)=> e==null))
    {
      myList.retainWhere((e)=>e!=null);
    }
    else
    {
      myList.insert(0, null);
    }
    // end of the ugly hack

  }

  void addHTMLToDiv(innerHtml, DivElement cell) {
    try {
      Element inside = PuiHtmlUtils.parseResponse(innerHtml);
      ViewFactory template = compiler([inside], directives);
      Scope childScope = scope.createChild(scope.context);
      Module module = new Module()..value(Scope, childScope);
      List<Module> modules = new List<Module>();
      modules.add(module);
      Injector childInjector = injector.createChild(modules);
      template(childInjector, [inside]);
      cell.children.add(inside);
    } catch (error) {
      print("Error: $error");
      cell.innerHtml = "error";
    }
  }

  /** If the first column(s) is/are hidden, the left border line has to be provided by the first visible cell of each row. */
  void _drawLeftBoundaryLine() {
    if (columnHeaders[0].hidden) {
      int firstVisibleIndex = 0;
      for ( ; firstVisibleIndex < columnHeaders.length; firstVisibleIndex++) {
        if (!(columnHeaders[firstVisibleIndex].hidden)) break;
      }
      shadowTableContent.children.forEach((Element row) {
        if (row.children.length > firstVisibleIndex) {
          row.children[firstVisibleIndex].style.borderLeftWidth = "1px";
        }
      });
    }
  }


  addColumn(Column column) {
    columnHeaders.add(column);
  }

}

/** PuiDatatable adds or removes empty row to force the watch watching the collection to fire. This filter prevents the empty rows from being shown. */
@NgFilter(name: 'puiEmptyRowsFilter')
class PuiEmptyRowsFilter {

  List call(List original, PuiDatatableComponent pui) {
      List newList = new List();
      // fix the null values introduced by the ugly hack in sortColumn()
     original.forEach((r){ if (null!=r) newList.add(r);});
     if (null != pui && null != pui.myList) {
       scheduleMicrotask((){pui.myList.retainWhere((e)=>e!=null);});
     }
     return newList;
  }
}

/** The puiSortFilter sorts a ng-repeat list according to the sort order of the puiDatatable */
@NgFilter(name: 'puiDatatableSortFilter')
class PuiDatatableSortFilter {
  /** AngularDart's orderBy filter sorts a list by a column name (which is given as a String) */
  OrderByFilter _orderBy;

  /** AngularDart's FilterFilter filters a list according to the values of a given column */
  FilterFilter _contentFilter;

  /** PuiDatatable adds or removes empty row to force the watch watching the collection to fire. This filter prevents the empty rows from being shown. */
  PuiEmptyRowsFilter _emptyRowsFilter;

  Parser _parser;

  PuiDatatableSortFilter(this._parser){
    _orderBy=new OrderByFilter(_parser);
    _emptyRowsFilter=new PuiEmptyRowsFilter();
    _contentFilter=new FilterFilter(_parser);
  }

  /** PuiFilters aren't bound to a particular component, so every PuiDatatable registers itself to the filter in order to link filters and table */
  static Map<String, PuiDatatableComponent> tables = new Map<String, PuiDatatableComponent>();

  /** PuiFilters aren't bound to a particular component, so every PuiDatatable registers itself to the filter in order to link filters and table */
  static register(String name, PuiDatatableComponent puiDatatableComponent) {
    tables[name]=puiDatatableComponent;
  }

  /** Finds out, which PuiDatatable is to be sorted, finds out, by which column and in which order it is to be sorted and delegates sorting the Angular's OrderByFilter */
  List call(List original, String tableName, [bool descending=false]) {
    PuiDatatableComponent pui = tables[tableName];
    if (pui==null) return original;

    List nonEmptyRows = _emptyRowsFilter.call(original, pui);

    /* The next few lines might benefit from a little explanation.
     *  Basically, the idea is to implement a <pui-datatable> column filter
     *  by using a standard filter of ng-repeat (array | column: "criteria").
     *  The standard filter is the variable FilterFilter.
     *  It takes two parameters when call: the list to be filtered, and the
     *  algorithm filtering a single row. The latter is called a predicate.
     *  The inner loop constructs a predicate that takes the name of the column,
     *  uses AngularDart's parser to get a function that maps an entry of the list
     *  to the value of a particular column of the entry. By calling this mapper function
     *  we get the value, which can be compared by a traditional string method.
     */
    pui.columnHeaders.forEach((Column col ){
      if (col.filterby!=null && col.filterby!="" && col.currentFilter!="") {
        Expression parsed = _parser(col.filterby);
        var mapper = (e) => parsed.eval(e);
        if (col.filterMatchMode == "contains")
        {
          var predicate = (v) => mapper(v).toString().toLowerCase().contains(col.currentFilter.toLowerCase());
          nonEmptyRows= _contentFilter.call(nonEmptyRows, predicate);
        }
        else if (col.filterMatchMode == "endsWith")
        {
          var predicate = (v) => mapper(v).toString().toLowerCase().endsWith(col.currentFilter.toLowerCase());
          nonEmptyRows= _contentFilter.call(nonEmptyRows, predicate);
        }
        else if (col.filterMatchMode == "exact")
        {
          var predicate = (v) => mapper(v).toString().toLowerCase()== col.currentFilter.toLowerCase();
          nonEmptyRows= _contentFilter.call(nonEmptyRows, predicate);
        }
        else // if (col.filterMatchMode == "startsWith")
        {
          var predicate = (v) => mapper(v).toString().toLowerCase().startsWith(col.currentFilter.toLowerCase());
          nonEmptyRows= _contentFilter.call(nonEmptyRows, predicate);
        }
      }
    });
    /* End of the sophisticated algorithm :) */


    try
    {
      Column firstWhere = pui.columnHeaders.firstWhere((Column c) => c.sortDirection!=0);
      return _orderBy.call(nonEmptyRows, firstWhere.sortBy, firstWhere.sortDirection==2);
    }
    catch (notSortedException)
    {
      if ((pui!=null) && (pui.initialsort!=null))
      {
        bool descending="down"==pui.initialsortorder;
        return _orderBy.call(nonEmptyRows, pui.initialsort, descending);
      }
      return nonEmptyRows;
    }
  }
}

