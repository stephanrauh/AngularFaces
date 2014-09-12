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

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

import org.primefaces.expression.SearchExpressionResolverFactory;

/** Helper class */
public class AngularViewContextWrapperFactory extends PartialViewContextFactory {
	static {
		SearchExpressionResolverFactory.registerResolver("@angular", new AngularExpressionResolver());
	}

	private PartialViewContextFactory wrappedPartialViewContextFactory;

	public AngularViewContextWrapperFactory(PartialViewContextFactory partialViewContextFactory) {
		this.wrappedPartialViewContextFactory = partialViewContextFactory;
	}

	@Override
	public PartialViewContext getPartialViewContext(FacesContext context) {
		return new AngularViewContextWrapper(wrappedPartialViewContextFactory.getPartialViewContext(context));
	}
}
