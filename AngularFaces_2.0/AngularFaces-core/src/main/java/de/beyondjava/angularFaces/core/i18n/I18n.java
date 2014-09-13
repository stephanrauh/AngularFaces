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
package de.beyondjava.angularFaces.core.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/** Translates a text to the user's preferred language. */
@ManagedBean
@ViewScoped
public class I18n implements Serializable {
	private static final long serialVersionUID = 1L;
	Locale locale;
	HashMap<String, String> translations = new HashMap<String, String>();
	String json = "";

	@PostConstruct
	public void loadMessageBundles() {
		FacesContext context = FacesContext.getCurrentInstance();
		locale = context.getExternalContext().getRequestLocale();
		// locale = context.getViewRoot().getLocale();
		String language = locale.getLanguage();
		String country = locale.getCountry();
		InputStream i18n = getClass().getClassLoader().getResourceAsStream("i18n_" + language + "_" + country + ".properties");
		if (null == i18n) {
			i18n = getClass().getClassLoader().getResourceAsStream("i18n_" + language + ".properties");
		}
		if (null == i18n) {
			i18n = getClass().getClassLoader().getResourceAsStream("i18n.properties");
		}
		if (null == i18n) {
			i18n = getClass().getClassLoader().getResourceAsStream("i18n_en.properties");
		}
		if (null != i18n) {
			String i18nContents = slurp(i18n, 4096);
			if (null != i18nContents) {
				String[] lines = i18nContents.split("\n");
				for (String line : lines) {
					json = convertPropertyFileLineToJSon(line, json);
				}
				if (json.length() > 1)
					json = "{" + json.substring(1) + "}";
				else
					json = "{}";
			}
			try {
				i18n.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Note this method has a side effect - it also fills the HashMap of translations. */
	private String convertPropertyFileLineToJSon(String line, String json) {
 		if (line==null) return json;
		String l = line.trim();
		if (l.length()==0 || l.startsWith("//")) return json;
		boolean inString = l.startsWith("\"");
		String english;
		String rest;
		if (inString) {
			boolean esc = false;
			int i = 1;
			for (; i < l.length(); i++) {
				if (esc)
					esc = false;
				else if (l.charAt(i) == '\\')
					esc = true;
				else if (l.charAt(i) == '\"') {
					i++;
					break;
				}
			}
			english = l.substring(1, i - 1).replace("\\\"", "\"");
			rest = l.substring(i).trim();
		} else {
			int pos = l.indexOf('=');
			if (pos<0) {
				System.out.println("Illegal entry in language file");
				return json;
			} else {
			english = l.substring(0, pos).trim();
			rest = l.substring(pos);
			}
		}
		if (!rest.startsWith("=")) {
			System.out.println("Illegal line in translation file");
		} else {
			String translation;
			rest = rest.substring(1).trim();
			if (rest.startsWith("\"")) {
				int pos = rest.lastIndexOf("\"");
				if (pos > 1)
					translation = rest.substring(1, pos).replace("\\\"", "\"").trim();
				else
					translation = rest.replace("\\\"", "\""); // treat incorrect string gracefully
			} else
				translation = rest;
			this.translations.put(english, translation);
			json += ", \"" + english + "\"" + "," + "\"" + translation + "\"";
		}
		return json;
	}

	public String getMessageBundleAsJSon() {
		return json;
	}

	/** copied from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string */
	public static String slurp(final InputStream is, final int bufferSize) {
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "UTF-8");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			} finally {
				in.close();
			}
		} catch (UnsupportedEncodingException ex) {
			/* ... */
		} catch (IOException ex) {
			/* ... */
		}
		return out.toString();
	}

	public String translate(String english) {
		if (null == translations || translations.isEmpty() || english == null)
			return english;
		if (translations.containsKey(english))
			return translations.get(english);
		english = english.trim();
		if (translations.containsKey(english))
			return translations.get(english);
		return english;
	}
}
