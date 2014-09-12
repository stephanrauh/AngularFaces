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
package de.beyondjava.angularFaces.flavors.kendo.puiInputText;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.common.ILabel;
import de.beyondjava.angularFaces.common.IModel;
import de.beyondjava.angularFaces.common.IStyle;
import de.beyondjava.angularFaces.common.IStyleClass;

/**
 * PuiSlider is a slider to set numeric values.
 */
@FacesComponent("de.beyondjava.kendoFaces.puiInputText.PuiInputText")
public class PuiInputText extends UIInput implements IModel, IStyle,
		IStyleClass, ILabel, SystemEventListener {

	public PuiInputText() {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		root.subscribeToViewEvent(PreRenderViewEvent.class, this);
	}

	private void insertLabelBeforeThisInputField() {
		HtmlOutputLabel l = new HtmlOutputLabel();
		l.setFor(getId());
		l.setValue(getLabel());
		List<UIComponent> tree = getParent().getChildren();
		for (int i = 0; i < tree.size(); i++) {
			if (tree.get(i) == this) {
				tree.add(i, l);
				break;
			}
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		return (source instanceof UIViewRoot);
	}

	/**
	 * Catching the PreRenderViewEvent allows AngularFaces to modify the JSF
	 * tree by adding a label and a message.
	 */
	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			// if (!(getParent() instanceof Column)) {
			insertLabelBeforeThisInputField();
			insertMessageBehindThisInputField();
		}
		// }
	}

	private void insertMessageBehindThisInputField() {
		// NGMessage l = new NGMessage();
		// l.setFor(getId());
		// l.setDisplay("text");
		// l.setTarget(this);
		// List<UIComponent> tree = getParent().getChildren();
		// for (int i = 0; i < tree.size(); i++) {
		// if (tree.get(i) == this) {
		// tree.add(i + 1, l);
		// break;
		// }
		// }
	}

	public StateHelper getStateHelper() {
		return super.getStateHelper();
	}

	protected String getTypeSpecificAttributes() {
		return "";
	}
}
