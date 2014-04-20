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

import 'dart:html' as dom;
import 'package:angular/angular.dart';


@NgDirective(
    selector: '[puiinput2]'
)
class AInputtextDirective {
  dom.Element element;
  
  @NgOneWay('caption')
  String caption;
  
  @NgOneWay('bean')
  String bean;
  
  @NgOneWay('value')
  String value;
  
  dom.Element captionElem;
  dom.InputElement inputField;


  AInputtextDirective(this.element) {
    print('Constructor called');
    caption = element.getAttribute('caption');
    bean = element.getAttribute('bean');
    value = element.getAttribute('value');
    
    captionElem = new dom.SpanElement();
    captionElem.innerHtml=caption;
    element.append(captionElem);
    inputField = new dom.InputElement();
    inputField.setAttribute('ng-model', bean);
    inputField.name=bean;
    inputField.value=value;
    
    element.append(inputField);
    
  }

  _createTemplate() {
    assert(caption != null);
  }

}

class AInputtextModel {
  String caption;
  String model;

  AInputtextModel(this.caption, this.model);
}