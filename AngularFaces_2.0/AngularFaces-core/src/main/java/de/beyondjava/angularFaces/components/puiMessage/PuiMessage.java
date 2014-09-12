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
package de.beyondjava.angularFaces.components.puiMessage;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class PuiMessage extends HtmlMessage {
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("puimessage", this);
		UIComponent inputField = findComponent(getFor());
		if (inputField.getClass().getName().contains("primefaces")) {
			writer.writeAttribute("primefaces", "true", "primefaces");
		}
//		writer.writeAttribute("angularfacesmessage", getFor(), "angularfacesmessage");
//		FacesContext.getCurrentInstance().getMessageList()
		List<FacesMessage> messageList = FacesContext.getCurrentInstance().getMessageList(getFor());
		if (!messageList.isEmpty()) {
			String msg = "";
			for (FacesMessage m:messageList) {
				String t = m.getDetail();
				if (t.startsWith(getFor())) {
					t=t.substring(getFor().length()+1).trim();
				}
				msg += t;
			}
			writer.writeAttribute("servermessage", msg , "servermessage");
		}
		writer.endElement("puimessage");
	}
	
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
	}

}
