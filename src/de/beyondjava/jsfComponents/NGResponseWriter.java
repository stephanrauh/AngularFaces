/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.faces.config.WebConfiguration.DisableUnicodeEscaping;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGResponseWriter extends HtmlResponseWriter {
	String ngPrefix = "";
	String currentElement = "";

	/**
	 * @param writer
	 * @param contentType
	 * @param encoding
	 * @param isScriptHidingEnabled
	 * @param isScriptInAttributeValueEnabled
	 * @param disableUnicodeEscaping
	 * @param isPartial
	 * @throws FacesException
	 */
	public NGResponseWriter(Writer writer, String contentType, String encoding,
			Boolean isScriptHidingEnabled,
			Boolean isScriptInAttributeValueEnabled,
			DisableUnicodeEscaping disableUnicodeEscaping, boolean isPartial)
			throws FacesException {
		super(writer, contentType, encoding, isScriptHidingEnabled,
				isScriptInAttributeValueEnabled, disableUnicodeEscaping,
				isPartial);
	}

	/**
	 * @param writer
	 * @param contentType
	 * @param encoding
	 * @throws FacesException
	 */
	public NGResponseWriter(Writer writer, String contentType, String encoding,
			String ngPrefix) throws FacesException {
		super(writer, contentType, encoding);
		if (null != ngPrefix) {
			this.ngPrefix = ngPrefix;
		}
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {
//		System.out.println(">>" + csq);
		return super.append(csq);
	}

	@Override
	public void startElement(String arg0, UIComponent arg1) throws IOException {
		System.out.println("Start: " + arg0 + " "
				+ ((arg1 == null) ? "" : arg1.getClass().getSimpleName()));
		currentElement = arg0;
		super.startElement(arg0, arg1);
	}

	@Override
	public void endElement(String arg0) throws IOException {
		if ("select".equals(arg0))
			System.out.println("End:" + arg0 + this.toString());
		super.endElement(arg0);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void write(String str) throws IOException {
		if ("select".equals(currentElement))
		{
			if (str.contains("<option "))
			{
				str = str.replaceAll("selected=\"selected\"", "");
			}
		}
		super.write(str);
	}
	
	public void writeAttribute(String arg0, Object arg1, String arg2)
			throws IOException {
//		System.out.println(arg0 + " = " + arg1);
		if ("ng-model".equals(arg0)) {
			super.writeAttribute(arg0, ngPrefix + "." + arg1, arg2);
		} else if (!"value".equals(arg0) && (!"id".equals(arg0))
				&& (!"selected".equals(arg0))) {
			super.writeAttribute(arg0, arg1, arg2);
		}
		// else {
		// System.out.println("Suppressed Value: " + arg1);
		// }
	}
}
