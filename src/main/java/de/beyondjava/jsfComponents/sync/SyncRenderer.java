package de.beyondjava.jsfComponents.sync;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import de.beyondjava.jsfComponents.common.*;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.Sync")
public class SyncRenderer extends org.primefaces.component.inputtext.InputTextRenderer {
   /*
    * (non-Javadoc)
    * 
    * @see
    * javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
    * javax.faces.component.UIComponent)
    */
   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      // TODO Auto-generated method stub
      // super.encodeBegin(context, component);
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext
    * , javax.faces.component.UIComponent)
    */
   @Override
   public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
      // TODO Auto-generated method stub
      // super.encodeChildren(context, component);
   }

   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
   }

   /**
    * Renders ng-model, min, max, integer and required according to the bean
    * attribute's properties.
    */
   @Override
   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs)
         throws IOException {
      super.renderPassThruAttributes(context, component, attrs);
      ResponseWriter writer = context.getResponseWriter();

      NGUIComponentInfo info = NGUIComponentTools.getInfo(context, component);
      String model = info.getNGModel();
      writer.writeAttribute("ng-model", model, "ng-model");

   };

}
