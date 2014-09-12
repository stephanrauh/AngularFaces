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
package de.beyondjava.angularFaces.core.transformation;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionResolver;

public class AngularExpressionResolver implements SearchExpressionResolver {

	@Override
	public UIComponent resolve(UIComponent source, UIComponent last,
			String expression) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("de.beyondjava.angularFaces.angularRequest", true);
		UIComponent parent = last.getParent();

		while (parent != null) {
			if (parent instanceof HtmlBody) {
				return parent;
			}

			parent = parent.getParent();
		}

		return null;
	}

}
