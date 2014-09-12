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

public class FullVisitContext extends VisitContext {

    /**
     * Creates a FullVisitorContext instance.
     * @param facesContext the FacesContext for the current request
     * @throws NullPointerException  if {@code facesContext}
     *                               is {@code null}
     */    
    public FullVisitContext(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        this.facesContext = facesContext;

        // Copy and store hints - ensure unmodifiable and non-empty
        EnumSet<VisitHint> hintsEnumSet = 
                                          EnumSet.noneOf(VisitHint.class);
                                          

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
     * @see VisitContext#getSubtreeIdsToVisit VisitContext.getSubtreeIdsToVisit()
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
    public VisitResult invokeVisitCallback(UIComponent component, 
                                           VisitCallback callback) {

        // Nothing interesting here - just invoke the callback.
        // (PartialVisitContext.invokeVisitCallback() does all of the 
        // interesting work.)
        return callback.visit(this, component);
    }

    // The FacesContext for this request
    private FacesContext facesContext;

    // Our visit hints
    private Set<VisitHint> hints;
}
