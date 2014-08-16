package de.beyondjava.angularFaces.core.puiEL;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionResolver;

public class AngularExpressionResolver implements SearchExpressionResolver {

	@Override
	public UIComponent resolve(UIComponent source, UIComponent last,
			String expression) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("de.beyondjava.angularFaces.angularRequest", true);
		UIComponent parent = last.getParent();

		while (parent != null) {
			if (parent instanceof de.beyondjava.angularFaces.flavors.kendo.puiBody.PuiBody) {
				return parent;
			}

			parent = parent.getParent();
		}

		return null;
	}

}
