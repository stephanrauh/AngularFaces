package de.beyondjava.jsfComponents;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import de.beyondjava.jsfComponents.common.ELTools;
import de.beyondjava.jsfComponents.common.NGBeanAttributeInfo;

@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.InputText")
public class NGInputTextRenderer extends org.primefaces.component.inputtext.InputTextRenderer
{

   /**
    * renderPassThruAttributes() is overloaded to add extra behaviour
    */
   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs)
         throws IOException
   {
      super.renderPassThruAttributes(context, component, attrs);
      ResponseWriter writer = context.getResponseWriter();

      String model = component.getClientId(); // ELTools.getNGModel(component);
      writer.writeAttribute("ng-model", model, "ng-model");

      readJSR303Annotations(component, writer);
   }

   private void readJSR303Annotations(UIComponent component, ResponseWriter writer) throws IOException
   {
      NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(component);
      if (info.isHasMax())
      {
         writer.writeAttribute("max", info.getMax(), "max");

      }
      if (info.isHasMin())
         writer.writeAttribute("min", info.getMin(), "min");
      if (info.isInteger())
         writer.writeAttribute("integer", "", "integer");
      Object o = component.getAttributes().get("required");
      if (null != o)
      {
         writer.writeAttribute("required", "", "required");
      }
   }

   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException
   {
      super.encodeEnd(context, component);
   }
}
