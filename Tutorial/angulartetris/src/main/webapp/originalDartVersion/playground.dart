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
part of angularTetris;

/**
 * <pui-grid> makes it a little easier to create simple but decently looking input dialogs.
 * Typically it contains a number of input fields that are automatically aligned to each other.
 * More precisely, <pui-grid> creates a column consisting of three columns. The first column is the label (which is automatically extracted from the component),
 * the second is the components itself and the third column is the field-specific error message. Alternative, the error message is placed below its field.
 * Likewise, the label can be put above the input field.
 *
 * @ToDo <pui-grid> is limited to a single column of components
 * @ToDo put labels optionally above the component
 * @ToDo put error message optionally behind the component
 */
@Component(
    selector: 'tetris-playground',
    templateUrl: 'playground.html',
/*    template: """<div style="display:table">
  <div style="display:table-row; height:100%;">
    <div style="display:table-cell" class="pui-label-cell">
      <label></label>
    </div>
    <div style="display:table-cell" class="pui-widget-cell">
    </div>
  </div>
</div>""",
 */
    useShadowDom:     false,
    publishAs: 'cmp'
)
class TetrisPlaygroundComponent extends PuiGridComponent {
  int numberOfColumns=10;
  int numberOfRows=25;

  Scope scope;

  /** The <tetris-playground> field as defined in the HTML source code. */
  Element playgroundComponent;

  MainController mainController;

  String color(int r, int c){return getBrickColor(mainController.bricks[r*numberOfColumns+c]);
                          }

  String getBrickColor(int col) {
                              if (0==col) return "#FFFFFF";
                              if (1==col) return "#00F0F0";
                              if (2==col) return "#0000F0";
                              if (3==col) return "#F0A000";
                              if (4==col) return "#F0F000";
                              if (5==col) return "#00F000";
                              if (6==col) return "#F00000";
                              if (7==col) return "#A000F0";
                              return "#00FFFFFF";
  }
  String text(int r, int c){return (mainController.bricks[r*numberOfColumns+c]).toString();}

  /**
   * Initializes the component by setting the <pui-input> field and setting the scope.
   */
  TetrisPlaygroundComponent(MainController this.mainController, this.scope, Element playgroundComponent, Compiler compiler, Injector injector, DirectiveMap directives, Parser parser)
      :  super(playgroundComponent, compiler, injector, directives, parser)
  {
    this.playgroundComponent=playgroundComponent;

    if (playgroundComponent.attributes.containsKey("columns")) numberOfColumns=int.parse(playgroundComponent.attributes["columns"]);
    if (playgroundComponent.attributes.containsKey("rows")) numberOfRows=int.parse(playgroundComponent.attributes["rows"]);
    mainController.rows = numberOfRows;
    mainController.columns = numberOfColumns;
    mainController.init();


    for (int r = 0; r < numberOfRows; r++) {
      for (int c = 0; c < numberOfColumns; c++) {
        String button="<button style=\"width:25px;height:20px\"></button>";
          List<Node> list = PuiHtmlUtils.parseResponse("<span>$button</span>").childNodes;

          ViewFactory template = compiler(list, directives);
          Injector childInjector =
                injector.createChild([new Module()..bind(Scope, toValue: scope)]);
          template(childInjector, list);

          while (list.length>0) {
              Node n = list[0];
              playgroundComponent.children.add(n);
          }

        }
    }
    init();
    mainController.updateGraphicsCallback = () => updateGraphics();
  }

  void updateGraphics() {
    List<Element> htmlBricks = content;
    for (int i = 0; i < htmlBricks.length; i++) {
      int brick = mainController.bricks[i];
      String color = getBrickColor(brick);
      Element htmlBrick = htmlBricks[i];
      htmlBrick.style.backgroundColor=color;
    }
  }
}
