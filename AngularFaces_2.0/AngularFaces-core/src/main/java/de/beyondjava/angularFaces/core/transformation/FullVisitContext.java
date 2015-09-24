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
package de.beyondjava.angularFaces.core.transformation;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/** Tells JSF to visit every node of the JSF component tree. */
public class FullVisitContext extends VisitContext {

	// The FacesContext for this request
	private FacesContext facesContext;

	// Our visit hints
	private Set<VisitHint> hints;

	/**
	 * Creates a FullVisitorContext instance.
	 * 
	 * @param facesContext
	 *            the FacesContext for the current request
	 * @throws NullPointerException
	 *             if {@code facesContext} is {@code null}
	 */
	public FullVisitContext(FacesContext facesContext) {
		if (facesContext == null) {
			throw new NullPointerException();
		}

		this.facesContext = facesContext;

		// Copy and store hints - ensure unmodifiable and non-empty
		EnumSet<VisitHint> hintsEnumSet = EnumSet.of(VisitHint.SKIP_UNRENDERED);

		this.hints = Collections.unmodifiableSet(hintsEnumSet);
	}

	/**
	 * @see VisitContext#getFacesContext VisitContext.getFacesContext()
	 */
	@Override
	public FacesContext getFacesContext() {
		return facesContext;
	}

	/**
	 * @see VisitContext#getIdsToVisit VisitContext.getIdsToVisit()
	 */
	@Override
	public Collection<String> getIdsToVisit() {

		// We always visits all ids
		return ALL_IDS;
	}

	/**
	 * @see VisitContext#getSubtreeIdsToVisit
	 *      VisitContext.getSubtreeIdsToVisit()
	 */
	@Override
	public Collection<String> getSubtreeIdsToVisit(UIComponent component) {

		// Make sure component is a NamingContainer
		if (!(component instanceof NamingContainer)) {
			throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
		}

		// We always visits all ids
		return ALL_IDS;
	}

	/**
	 * @see VisitContext#getHints VisitContext.getHints
	 */
	@Override
	public Set<VisitHint> getHints() {
		return hints;
	}

	/**
	 * @see VisitContext#invokeVisitCallback VisitContext.invokeVisitCallback()
	 */
	@Override
	public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callback) {

		// Nothing interesting here - just invoke the callback.
		// (PartialVisitContext.invokeVisitCallback() does all of the
		// interesting work.)
		return callback.visit(this, component);
	}
}
