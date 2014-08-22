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
