/**
 * (C) 2014 Rudy De Busscher  http://www.beyondjava.net
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

@NgController(selector: '[puiButtonDemo]', publishAs: 'ctrlBtn')
class PuiButtonDemoController {

  void showMsg(String msg) {
    window.alert("This alert is shown by Dart. Received parameter = "+msg);
  }

}
