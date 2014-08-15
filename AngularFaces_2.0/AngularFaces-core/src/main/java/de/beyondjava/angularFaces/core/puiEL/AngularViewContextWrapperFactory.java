package de.beyondjava.angularFaces.core.puiEL;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

public class AngularViewContextWrapperFactory extends PartialViewContextFactory {

	private PartialViewContextFactory wrappedPartialViewContextFactory;

	public AngularViewContextWrapperFactory(PartialViewContextFactory partialViewContextFactory) {
		this.wrappedPartialViewContextFactory = partialViewContextFactory;
	}

	@Override
	public PartialViewContext getPartialViewContext(FacesContext context) {
		return new AngularViewContextWrapper(wrappedPartialViewContextFactory.getPartialViewContext(context));
	}
}
