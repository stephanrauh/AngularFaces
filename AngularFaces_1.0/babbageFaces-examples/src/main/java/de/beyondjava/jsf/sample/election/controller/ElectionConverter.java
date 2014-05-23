package de.beyondjava.jsf.sample.election.controller;

import javax.el.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.*;

import de.beyondjava.jsf.sample.election.domain.*;

@FacesConverter("ElectionConverter")
public class ElectionConverter implements Converter {

    /** Copied from de.beyondjava.angularFaces.core.ELTools */
    private static Object evalAsObject(String p_expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        ValueExpression vex = expressionFactory.createValueExpression(elContext, p_expression, Object.class);
        Object result = vex.getValue(elContext);
        return result;
    }

    private Country country;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        initCountries();
        for (Election e : country.getElections()) {
            if (String.valueOf(e.getYear()).equals(value)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (null == value) {
            return "";
        }
        return String.valueOf(((Election) value).getYear());
    }

    private void initCountries() {
        country = (Country) evalAsObject("#{singleElectionChartController.selectedCountry}");
    }
}