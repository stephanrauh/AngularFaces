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

/** Common tasks like converting HTML fragments to partial DOM trees */
class PuiHtmlUtils {
  static NodeValidator nodeValidator=new TolerantNodeValidator();

  /** Converts the HTML fragment to a partial DOM tree */
  static Element parseResponse(resp) {
    Element includedElement = new Element.html(resp, validator:nodeValidator);
    return includedElement;
  }

}

/** By default HTML.element() remove the pui elements, so we need a more tolerant validator */
class TolerantNodeValidator implements NodeValidator
{
  /** allow every attribute / prevent attributes from being filtered */
  @override
  bool allowsAttribute(Element element, String attributeName, String value) {
    return true;
  }

  /** allow every element / prevent elements from being filtered */
  @override
  bool allowsElement(Element element) {
    return true;
  }
}