package de.beyondjava.angularFaces.core.puiEL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGBeanAttributeInfo;

public class AddTypeCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		if (component instanceof UIInput) {
			NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(component);
			if (infos.isNumeric()) {
				if (component.getClass().getName().equals("org.primefaces.component.inputtext.InputText")) {
					Method method;
					try {
						method = component.getClass().getMethod("setType", String.class);
						method.invoke(component, "number");
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						// catch block required by compiler, can't happen in reality
					}
				}
				else {
					component.getPassThroughAttributes().put("type", "number");
				}
			}
		}
		return VisitResult.ACCEPT;
	}

}
