/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.faces.config.WebConfiguration.DisableUnicodeEscaping;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGResponseWriter extends HtmlResponseWriter {
	String ngPrefix = "";
	String currentElement = "";
	/** collects a data table's value attributes */
	private List<String> valueExpressions = new ArrayList<>();
	boolean ngModelAlreadyWritten;
	String currentNGModel=null;
	private UIComponent currentComponent;


	/**
	 * @param writer
	 * @param contentType
	 * @param encoding
	 * @throws FacesException
	 */
	public NGResponseWriter(Writer writer, String contentType, String encoding,
			String ngPrefix) throws FacesException {
		super(writer, contentType, encoding);
		if (null != ngPrefix && ngPrefix.length()>0) {
			this.ngPrefix = ngPrefix+".";
		}
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {
		return super.append(csq);
	}
	
	@Override
	public Writer append(char arg0) throws IOException {
		// TODO Auto-generated method stub
		return super.append(arg0);
	}

	@Override
	public void startElement(String arg0, UIComponent arg1) throws IOException {
//		if (currentElement!=null && currentNGModel!=null)
//		{
//			writeAttribute("ng-model", currentNGModel, "ng-model");
//		}
		currentElement = arg0;
		currentComponent=arg1;
		if (null != currentComponent)
		{
			System.out.println("Start: " + arg0 + " "
					+ ((arg1 == null) ? "" : arg1.getClass().getSimpleName()));
			ngModelAlreadyWritten=false;
			String valueExpression = ELTools.getCoreValueExpression(currentComponent);
			System.out.println("VEX: " + valueExpression);
			getValueExpressions().add(valueExpression);
			currentNGModel = ELTools.getNGModel(currentComponent);
			if (!currentComponent.getPassThroughAttributes().containsKey("ng-model"))
			{
				currentComponent.getPassThroughAttributes().put("ng-model", ngPrefix+ currentNGModel);
			}
		}
		super.startElement(arg0, arg1);
	}

	@Override
	public void endElement(String arg0) throws IOException {
		System.out.println("End:" + arg0 + " " + currentComponent);
//		if (!ngModelAlreadyWritten)
//		{
//			if (null != currentNGModel)
//			{
//				System.out.println("Wrote " + currentNGModel);
//				writeAttribute("ng-model", currentNGModel, "ng-model");
//				currentNGModel=null;
//				currentElement=null;
//				ngModelAlreadyWritten=true;
//			}
//		}
		super.endElement(arg0);
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
	
	
	
	public void writeAttribute(String attribute, Object value, String arg2)
			throws IOException {
//		System.out.println(arg0 + " = " + arg1);
		if ("ng-model".equals(attribute)) {
			super.writeAttribute(attribute, ngPrefix + value, arg2);
			ngModelAlreadyWritten=true;
		} else if ((!"value".equals(attribute)) && (!"checked".equals(attribute))) {
			// value attributes have to be suppressed, because they collide with ng-model's magic
			super.writeAttribute(attribute, value, arg2);
		}
		// else {
		// System.out.println("Suppressed Value: " + arg1);
		// }
	}

	/**
	 * @return the valueExpressions
	 */
	public List<String> getValueExpressions() {
		return valueExpressions;
	}

	/**
	 * @param valueExpressions the valueExpressions to set
	 */
	public void setValueExpressions(List<String> valueExpressions) {
		this.valueExpressions = valueExpressions;
	}
}
