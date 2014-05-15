package de.beyondjava.jsf.sample.election.controller;

import java.util.List;

import javax.el.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.*;

import de.beyondjava.jsf.sample.election.domain.Country;

@FacesConverter("CountryConverter")
public class CountryConverter implements Converter {

    /** Copied from de.beyondjava.angularFaces.core.ELTools */
    private static Object evalAsObject(String p_expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        ValueExpression vex = expressionFactory.createValueExpression(elContext, p_expression, Object.class);
        Object result = vex.getValue(elContext);
        return result;
    }

    private List<Country> countries;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        initCountries();
        for (Country country : countries) {
            if (country.getName().equals(value)) {
                return country;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (null == value) {
            return "";
        }
        return ((Country) value).getName();
    }

    @SuppressWarnings("unchecked")
    private void initCountries() {
        if (null == countries) {
            countries = (List<Country>) evalAsObject("#{electionController.countries}");
        }
    }
}