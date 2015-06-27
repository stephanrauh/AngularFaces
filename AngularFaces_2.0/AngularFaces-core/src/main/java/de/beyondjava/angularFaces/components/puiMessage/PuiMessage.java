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
package de.beyondjava.angularFaces.components.puiMessage;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/** This error message is generated mostly on the client. */
public class PuiMessage extends HtmlMessage {
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("puimessage", this);
		addPrimeFacesAttribute(writer);
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

	private void addPrimeFacesAttribute(ResponseWriter writer) throws IOException {
		UIComponent inputField = findComponent(getFor());
		if (null != inputField) {
		if (inputField.getClass().getName().contains("primefaces")) {
			writer.writeAttribute("primefaces", "true", "primefaces");
		}
		}
	}
	
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
	}

}
