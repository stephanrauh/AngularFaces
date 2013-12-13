/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.selectOneMenu;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.selectonemenu.*;
import org.primefaces.util.ComponentUtils;

import de.beyondjava.jsfComponents.common.*;

/**
 * Add some AngularJS functionality to a standard PrimeFaces
 * SelectOneMenuRenderer (aka Combobox).
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.SelectOneMenu")
public class NGSelectOneMenuRenderer extends SelectOneMenuRenderer {
	/** add the ng-model attribute to the sub-component representing the select-one box */
//	@Override
//	public void encodeEnd(FacesContext context, UIComponent component)
//			throws IOException {
//		super.encodeEnd(context, component);
//	      ResponseWriter writer = context.getResponseWriter();
//	      NGSelectOneMenu combobox = (NGSelectOneMenu) component;
//	      String initValue = combobox.getNotificationJS();
//	      writer.append("\r\n");
//	      writer.append("  <script>\r\n " + initValue + "\r\n</script>");
//	      writer.append("\r\n");
//	}
}
