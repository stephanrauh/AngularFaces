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
part of showcase;

@NgController(selector: '[puiInputDemo]', publishAs: 'ctrl')
class PuiInputDemoController {

  String firstField = "first model value";
  String secondField = "second model value";
  String thirdField = "third model value";
  bool firstBoolean = true;
  bool secondBoolean = false;
  bool thirdBoolean = false;

  get thirdFieldStyle {
    if (thirdField.isNotEmpty) return "background-color:#FF0"; else return
        "background-color:#F00";
  }
}

