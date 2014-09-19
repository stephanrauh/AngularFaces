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

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

import de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator;

/** Helper class */
public class AngularViewContextWrapperFactory extends PartialViewContextFactory {
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.core.transformation.AngularViewContextWrapperFactory");
	private PartialViewContextFactory wrappedPartialViewContextFactory;

	public AngularViewContextWrapperFactory(PartialViewContextFactory partialViewContextFactory) {
		this.wrappedPartialViewContextFactory = partialViewContextFactory;
		LOGGER.info("Running on AngularFaces 2.1");
	}

	@Override
	public PartialViewContext getPartialViewContext(FacesContext context) {
		return new AngularViewContextWrapper(wrappedPartialViewContextFactory.getPartialViewContext(context));
	}
}
