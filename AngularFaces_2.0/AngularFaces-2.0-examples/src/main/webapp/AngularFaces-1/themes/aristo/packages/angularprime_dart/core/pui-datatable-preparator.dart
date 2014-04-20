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

part of angularprime_dart;

/** finds a simple interpolated variable or method. Complex formulas are not detected
 * (by will, because we can't deal with them).
 */
final RegExp VARIABLE_EXPRESSION = new RegExp(r'^\s*{{\s*([a-zA-Z0-9_\(\)\.]+)\s*}}\s*?$');

/** Syntax of an ng-repeat statement. Copied from ngRepeat. */
final RegExp _NG_REPEAT_SYNTAX = new RegExp(r'^\s*(.+)\s+in\s+(.*?)\s*(\s+track\s+by\s+(.+)\s*)?(\s+lazily\s*)?$');

/**
 * TODO: move this method to the puiDataTableComponent class (either to onShadow or onAttach).
 *
 * Converts <pui-datatable> to a format that can be processed by AngularDart.
 * In general, AngularDart is very flexible. However, as we want to provide
 * a radically simple table widget we don't follow Angular's conventions.
 * So we have to rearrange the HTML code (more precisely: the DOM tree) before Angular
 * is initialized.
 */
void prepareDatatables()
{
  HtmlCollection list = window.document.getElementsByTagName('pui-datatable');
  int puiTableID=0;
  list.forEach((Element puiDatatable){
    _prepareDatatable(puiDatatable, puiTableID++);
  });
}

/**
 * Converts a single <pui-datatable> from an abstract declarative style to a more AngularDart-compatible style.
 */
_prepareDatatable(Element puiDatatable, int puiTableID) {
  puiDatatable.attributes["puiTableID"]=puiTableID.toString();
  ElementList columns = puiDatatable.querySelectorAll('pui-column');
  String headers = _prepareTableHeader(columns);

  String content = puiDatatable.innerHtml;
  ElementList rows = puiDatatable.querySelectorAll('pui-row');
  String listName;
  if (rows.isEmpty)
  {
    /** add pui-datatable functionality to ng-repeat */
    String ngRepeat = puiDatatable.attributes["ng-repeat"];
    if (null==ngRepeat)
    {
      String loopVar=puiDatatable.attributes["var"];
      listName=puiDatatable.attributes["value"];
      if (null==listName){
        throw "[pui-datatable-err] Wrong datatable configuration: Please specify either ng-repeat or the list and - optionally - a loop variable";
      }
      if (null==loopVar) loopVar="row";

      ngRepeat = "$loopVar in $listName";
    }
    else {
      listName = extractNameOfCollection(ngRepeat);
      puiDatatable.attributes["value"]=listName;
    }
    ngRepeat = ngRepeat + " | puiDatatableSortFilter:'$listName$puiTableID'";
    puiDatatable.attributes["puiListVariableName"]=listName;
    content = """<pui-row pui-repeat="$ngRepeat" role="row" style="display:table-row" class="tr ui-widget-content">$content</pui-row>""";
    puiDatatable.attributes.remove("ng-repeat");
  }
  else
  {
    rows.forEach((Element row){
      /** add pui-datatable functionality to ng-repeat */
      String ngRepeat = row.attributes["ng-repeat"];
      listName = extractNameOfCollection(ngRepeat);
      ngRepeat = ngRepeat + " | puiDatatableSortFilter:'$listName$puiTableID'";
      puiDatatable.attributes["value"]=listName;
      puiDatatable.attributes["value"]=listName;
      row.attributes["pui-repeat"]=ngRepeat;
      row.attributes.remove("ng-repeat");

      row.attributes["data-ri"]=r'{{$index.toString()}}';
      row.classes.add("tr");
      row.classes.add("ui-widget-content");
      row.style.display="table-row";
      row.attributes["role"]="row";
    });
    content = puiDatatable.innerHtml;
  }
  String emptyMessage=puiDatatable.attributes["emptyMessage"];
  if (null==emptyMessage) emptyMessage="no results found";
  String footer = """<div role="row" style="{{$listName.isEmpty?'display:table-row':'display:none'}}" class="tr ui-widget-content ui-datatable-empty-message">
                         <div role="gridcell" class="pui-datatable-td ui-widget-content">
                            $emptyMessage
                         </div>
                     </div>""";

  content=headers + content + footer;

  String newContent = content.replaceAll("<pui-row", """<div """)
      .replaceAll("</pui-row>", "</div>")
      .replaceAll("<pui-column ", """<div """)
      .replaceAll("</pui-column>", "</div>");
  Element inside = PuiHtmlUtils.parseResponse("<span>$newContent</span>");
  puiDatatable.children.clear();
  puiDatatable.children.addAll(inside.children);
}

/**
 * TODO: move this method to the puiDataTableComponent class (either to onShadow or onAttach).
 *
 * Generates DIV elements to display a header for each column using the informations given in <pui-column> */
String _prepareTableHeader(ElementList columns) {
  String headers="";
  int c = 0;
  columns.forEach((Element col) {
    col.attributes["data-ci"]=c.toString();
    col.classes.add("pui-datatable-td");
    col.classes.add("ui-widget-content");
    col.style.display="table-cell";
    col.attributes["role"]="gridcell";
    String closable=col.attributes["closable"];
    if (closable==null) closable="false";
    String sortable=col.attributes["sortable"];
    if (sortable==null) sortable="false";
    String sortBy=col.attributes["sortBy"];
    if (sortBy!=null) {
      sortable="true";
    }
    else if (sortable=="true"){
      String inner = col.innerHtml;
      Match match = VARIABLE_EXPRESSION.firstMatch(inner);
      if (match == null) {
        throw "[pui-datatable-error] Can't find out by which row variable the column is to be sorted. Please specify sortBy attribute.";
      }
      sortBy=match.group(1);
      int pos = sortBy.indexOf("\.");
      if (pos>0)
      {
        sortBy=sortBy.substring(pos+1);
      }
    }
    String sortByAttribute="";
    if (null!=sortBy) {
      sortByAttribute=""" sortby="$sortBy" """;
    }
    String filterby=col.attributes["filterby"];
    if (null==filterby) filterby="";
    String filterMatchMode=col.attributes["filterMatchMode"];
    if (null == filterMatchMode) filterMatchMode="startsWith";

    headers += """<pui-column-header header="${col.attributes["header"]==null?"":col.attributes["header"]}" closable="$closable"  sortable="$sortable" $sortByAttribute 
                                     footerText="${col.attributes["footerText"]==null?"":col.attributes["footerText"]}"
                                     filterby="$filterby" filterMatchMode="$filterMatchMode"></pui-column-header>
               """;
    c++;
  });
  return headers;
}


/** Copied from ng-repeat.dart. Extracts the name of the list from an ng-repeat statement. */
String extractNameOfCollection(String ngRepeatStatement) {
  Match match = _NG_REPEAT_SYNTAX.firstMatch(ngRepeatStatement);
  if (match == null) {
    throw "[NgErr7] ngRepeat error! Expected expression in form of '_item_ "
        "in _collection_[ track by _id_]' but got '$ngRepeatStatement'.";
  }
  String _listExpr = match.group(2);
  return _listExpr;
}



