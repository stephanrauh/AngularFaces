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

part of puiDatatable;

@NgComponent(
  visibility: NgDirective.CHILDREN_VISIBILITY,
  selector: 'pui-column-header',
  cssUrl:       'packages/angularprime_dart/puiDatatable/pui-datatable.css',
  template: '',
  applyAuthorStyles: true,
  publishAs: 'cmp'
)
class PuiColumnHeaderComponent extends PuiBaseComponent implements NgShadowRootAware {

  /** The <pui-column-header> field as defined in the HTML source code. */
  Element _puiColumnHeaderElement;

  /** The scope is needed to add watches. */
  Scope _scope;

  /** Can the tab be closed? */
  bool _closable=false;

  /** Can the tab be closed? */
  @NgAttr("closable")
  set closable(String s){_closable="true"==s;}

  bool isClosable() => _closable;

  /** Can the tab be closed? */
  bool _sortable=false;

  /** Can the tab be closed? */
  @NgAttr("sortable")
  set sortable(String s){_sortable="true"==s;}

  bool isSortable() => _sortable;

  /** Optional caption of the column */
  @NgAttr("header")
  String header;

  /** Optional footer of the column */
  @NgAttr("footerText")
  String footerText;

  /** Optional column filter */
  @NgAttr("filterby")
  String filterby;

  /** Optional filter match mode.
   * Legal values are 'startsWith', 'contains', 'exact' and 'endsWith'.
   * Default value is 'startsWith'. */
  @NgAttr("filterMatchMode")
  String filterMatchMode = "startsWith";


  /** This is the datatable component the column is part of */
  PuiDatatableComponent puiDatatableComponent;

  /**
   * Initializes the component by setting the <pui-datatable> field and setting the scope.
   */
  PuiColumnHeaderComponent(this._scope, this._puiColumnHeaderElement, this.puiDatatableComponent, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser): super(compiler, injector, directives, parser) {
  }


  @override
  void onShadowRoot(ShadowRoot shadowRoot) {
    puiDatatableComponent.addColumn(new Column(header, footerText, isClosable(),
                                    isSortable(), _puiColumnHeaderElement.attributes["sortby"],
                                    filterby, filterMatchMode));
  }
}

/** Abstract description of the colum. */
class Column {
  /** Can the column be hidden hy hitting the close button? */
  bool closable;

  /** Caption of the column. */
  String header;

  /** Footer of the column. */
  String footerText;


  /** Can the table be sorted by hitting the columns sort button? */
  bool sortable;

  /** By which variable or function is the table to be sorted? */
  String sortBy;

  /** 0=not sorted, 1=sort upwards, 2=sort downwards */
  int sortDirection=0;

  /** Optional column filter */
  String filterby;

  /** Optional filter match mode.
   * Legal values are 'startsWith', 'contains', 'exact' and 'endsWith'.
   * Default value is 'startsWith'. */
  String filterMatchMode = "startsWith";

  /** Is the current row hidden?
   * Todo: strictly speaking, putting the run-time filter value here is
   * a violation of the separations of concerns principle, because every other
   * attribute describes the immutable properties of the column. */
  bool hidden=false;

  /** Used to store the current filter value entered by the user.
   * Todo: strictly speaking, putting the run-time filter value here is
   * a violation of the separations of concerns principle, because every other
   * attribute describes the immutable properties of the column. */
  String currentFilter="";

  Column(this.header, this.footerText, this.closable, this.sortable, this.sortBy,
      this.filterby, this.filterMatchMode) {
  }
}

