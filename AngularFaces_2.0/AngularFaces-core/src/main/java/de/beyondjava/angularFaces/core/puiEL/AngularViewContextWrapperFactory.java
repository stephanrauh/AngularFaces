package de.beyondjava.angularFaces.core.puiEL;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

import org.primefaces.expression.SearchExpressionResolverFactory;

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
