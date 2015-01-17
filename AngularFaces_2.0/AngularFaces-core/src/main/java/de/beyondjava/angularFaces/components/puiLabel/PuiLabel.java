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
package de.beyondjava.angularFaces.components.puiLabel;

import java.io.IOException;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.beyondjava.angularFaces.core.Configuration;

/** This generates a label that colors itself red if AngularJS says the input is wrong. */
@FacesComponent("de.beyondjava.puiLabel")
public class PuiLabel extends HtmlOutputLabel {
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("puilabel", this);
		UIComponent inputField = findComponent(getFor());
		if ((inputField==null && getFor()!=null) || (getClientId().indexOf(':')>=0)) {
			throw new FacesException("The PuiLabel component doesn't find its input field. Most likely that's a configuration error. The preferred solution is to add the attribute prependId=\"false\" to the form. Alternatively, you can set the javax.faces.separatorChar=\"%\" in the web.xml. However, the latter solution doesn't conform to the JSF specification, so it's not recommended (even if it may work).");
		}
		writer.writeAttribute("for", inputField.getId(), "for");
		if (inputField.getClass().getName().contains("primefaces")) {
			writer.writeAttribute("primefaces", "true", "primefaces");
		}
		if (Configuration.bootsFacesActive)
			writer.writeAttribute("bootsfaces", "true", "bootsfaces");
		
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
		writer.writeText(getValue(),null);
		writer.endElement("puilabel");
	}
	
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		// do nothing
	}

}
