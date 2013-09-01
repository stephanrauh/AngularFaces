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
	@Override
	protected void encodeInput(FacesContext context, SelectOneMenu menu,
			String clientId, List<SelectItem> selectItems, Object values,
			Object submittedValues, Converter converter) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		String inputId = clientId + "_input";

		writer.startElement("div", menu);

		writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

		writer.startElement("select", menu);

		String ngModel;
		ngModel = ELTools.getNGModel(menu);
		writer.writeAttribute("ng-model", ngModel, null);

		writer.writeAttribute("id", inputId, "id");

		writer.writeAttribute("name", inputId, null);

		if (menu.getOnchange() != null)
			writer.writeAttribute("onchange", menu.getOnchange(), null);

		if (menu.isDisabled())
			writer.writeAttribute("disabled", "disabled", null);

		encodeSelectItems(context, menu, selectItems, values, submittedValues,
				converter);

		writer.endElement("select");

		writer.endElement("div");
	}
}
