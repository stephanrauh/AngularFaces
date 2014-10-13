/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
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
package de.beyondjava.angularFaces.core;

/**
 * Collection of minor utilities dealing with words and strings.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 */
public class NGWordUtiltites {
	/**
	 * Converts a camelcase variable name to a human readable text.
	 * 
	 * @param camel the String to be converted 
	 * @return the hopefully human readable version
	 */

	public static String labelFromCamelCase(String camel) {
		StringBuilder label = new StringBuilder();
		for (int i = 0; i < camel.length(); i++) {
			char c = camel.charAt(i);
			if (Character.isDigit(c)) {
				if (i > 0 && Character.isAlphabetic(camel.charAt(i - 1)))
					label.append(" ");
			}
			if (c == '_') {
				label.append(" ");
			} else if (Character.isUpperCase(c)) {
				label.append(' ');
				label.append(Character.toLowerCase(c));
			} else {
				label.append(c);
			}
		}
		return label.toString();
	}

	public static String labelFromELExpression(String expression) {
		int pos = expression.indexOf("#{");
		int pos2 = expression.indexOf("}", pos);
		String core;
		if (pos >= 0 && pos2 >= 0)
			core = expression.substring(pos + 2, pos2);
		else
			core = expression;
		pos = core.lastIndexOf('.');
		if (pos > 0)
			core = core.substring(pos + 1);

		return labelFromCamelCase(core);
	}

	public static String lastTerm(String coreValue) {
		int pos = coreValue.indexOf('.');
		if (pos < 0) return coreValue;
		return coreValue.substring(pos+1);
	}
}
