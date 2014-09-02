package de.beyondjava.angularFaces.core.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class I18n implements Serializable {
	private static final long serialVersionUID = 1L;
	Locale locale;
	HashMap<String, String> translations = new HashMap<>();
	String json = "";
	
	@PostConstruct
	public void loadMessageBundles() {
		FacesContext context = FacesContext.getCurrentInstance();
		locale =context.getExternalContext().getRequestLocale();
//		locale = context.getViewRoot().getLocale();
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
			String i18nContents=slurp(i18n, 4096);
			if (null != i18nContents)
			{
				String[] lines = i18nContents.split("\n");
				for (String line:lines) {
					int pos = line.indexOf("=");
					if (pos > 0) {
						String english = line.substring(0, pos).trim();
						String translation = line.substring(pos+1).trim();
						if (translation.startsWith("\"") && translation.endsWith("\"")) translation=translation.substring(1, translation.length()-1);
						this.translations.put(english, translation);
						json+= ", \"" + english + "\"" + "," + "\"" + translation + "\"";
					}
				}
				if (json.length()>1)
				json = "{" + json.substring(1) + "}";
				else json="{}";
			}
			try {
				i18n.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getMessageBundleAsJSon() {
		return json;
	}
	
	/** copied from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string */
	public static String slurp(final InputStream is, final int bufferSize)
	{
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
	    }
	    finally {
	      in.close();
	    }
	  }
	  catch (UnsupportedEncodingException ex) {
	    /* ... */
	  }
	  catch (IOException ex) {
	      /* ... */
	  }
	  return out.toString();
	}
	
	public String translate(String english) {
		if (null==translations || translations.isEmpty()||english==null) return english;
		if (translations.containsKey(english)) return translations.get(english);
		english=english.trim();
		if (translations.containsKey(english)) return translations.get(english);
		return english;
	}
}
