package de.beyondjava.jsfComponents;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "de.beyondjava.InputText")
public class InputTextRenderer extends org.primefaces.component.inputtext.InputTextRenderer
{

   boolean isInteger = false;
   boolean hasMin = false;
   long min = 0;
   boolean hasMax = false;
   long max = 0;
   boolean isRequired = false;

   /**
    * renderPassThruAttributes() is overloaded to add extra behaviour
    */
   protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs)
         throws IOException
   {
      super.renderPassThruAttributes(context, component, attrs);
      ResponseWriter writer = context.getResponseWriter();

      String model = ELTools.getNGModel(component);
      writer.writeAttribute("ng-model", model, "ng-model");

      Annotation[] annotations = ELTools.readAnnotations(component);
      if (null != annotations)
      {
         for (Annotation a : annotations)
         {
            if (a instanceof Max)
            {
               long maximum = ((Max) a).value();
               writer.writeAttribute("max", maximum, "max");
               max=maximum;
               hasMax = true;
            }
            else if (a instanceof Min)
            {
               long minimum = ((Min) a).value();
               writer.writeAttribute("min", minimum, "min");
               hasMin = true;
               min=minimum;
            }
         }
      }

      Class<?> type = ELTools.getType(component);
      if (type == Integer.class || type == int.class)
      {
         // writer.writeAttribute("type", "number", "type");
         writer.writeAttribute("integer", "", "integer");
         isInteger = true;
      }
      Object o = component.getAttributes().get("required");
      if (null != o)
      {
         writer.writeAttribute("required", "", "required");
         isRequired = true;
      }
   }

    @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException
   {
      super.encodeEnd(context, component);
      MessageRenderer mr = new MessageRenderer();
      Message m = new Message();
      m.setFor(component.getClientId());
      mr.setHasMax(hasMax);
      mr.setMin(min);
      mr.setHasMin(hasMin);
      mr.setMax(max);
      mr.setInteger(isInteger);
      mr.setRequired(isRequired);
      mr.encodeEnd(context, m, component);
   }
}
