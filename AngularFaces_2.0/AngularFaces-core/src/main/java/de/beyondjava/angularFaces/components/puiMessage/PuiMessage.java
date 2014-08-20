package de.beyondjava.angularFaces.components.puiMessage;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class PuiMessage extends HtmlMessage {
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("angularfacesmessage", this);
//		writer.writeAttribute("angularfacesmessage", getFor(), "angularfacesmessage");
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
		writer.endElement("angularfacesmessage");
	}
	
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
	}

}
