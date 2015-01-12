/**
 *  (C) 2013-2015 Stephan Rauh http://www.beyondjava.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
