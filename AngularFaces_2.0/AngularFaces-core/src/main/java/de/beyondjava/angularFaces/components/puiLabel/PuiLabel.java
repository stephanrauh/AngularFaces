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
package de.beyondjava.angularFaces.components.puiLabel;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/** This generates a label that colors itself red if AngularJS says the input is wrong. */
@FacesComponent("de.beyondjava.puiLabel")
public class PuiLabel extends HtmlOutputLabel {
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("puilabel", this);
		UIComponent inputField = findComponent(getFor());
		writer.writeAttribute("for", inputField.getId(), "for");
		if (inputField.getClass().getName().contains("primefaces")) {
			writer.writeAttribute("primefaces", "true", "primefaces");
		}
		
		UIComponent f = inputField.getParent();
		
		while (f != null && (!(f instanceof HtmlForm))) { f=f.getParent();}
		if (null!=f) {
			HtmlForm form = (HtmlForm) f;
			writer.writeAttribute("formname", form.getClientId(), null);
		}
				
		writer.writeAttribute("label", getValue(), "label");
		List<FacesMessage> messageList = FacesContext.getCurrentInstance().getMessageList(getFor());
		if (!messageList.isEmpty()) {
			writer.writeAttribute("servermessage", "true" , "servermessage");
		}
		writer.writeText("AngularJS hasn't been initialized",null);
		writer.endElement("puilabel");
	}
	
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		// do nothing
	}

}
