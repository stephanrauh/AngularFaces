package de.beyondjava.jsfComponents.body;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.renderkit.CoreRenderer;

/**
 * This is an AngularJS-enabled html body tag.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = NGBody.COMPONENT_FAMILY, rendererType = "de.beyondjava.Body")
public class NGBodyRenderer extends CoreRenderer
{
   /**
    * Begin the body tag. This is where the attributes ng-app, ng-controller and
    * onload are set.
    * 
    * @author Stephan Rauh http://www.beyondjava.net
    */
   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException
   {
      ResponseWriter writer = context.getResponseWriter();
      writer.append("\r\n");
      writer.startElement("body", null);
      String ngApp = (String) component.getAttributes().get("ng-app");
      if (null != ngApp)
      {
         writer.writeAttribute("ng-app", ngApp, null);
      }
      else
      {
         writer.append(" ng-app ");
      }
      String ngController = (String) component.getAttributes().get("ng-controller");
      if (null != ngController)
      {
         writer.writeAttribute("ng-controller", ngController, null);
      }

      writer.writeAttribute("onload", "restoreValues()", null);
      writer.append("\r\n");
      writer.append("\r\n");
      writer.append("<script src='glue.js'></script>\r\n");
      writer.append("<script src='angular.js'></script>\r\n");
      writer.append("<script src='" + ngController + ".js'></script>\r\n");
   }

   /**
    * Finishes the body tag. Adds a call to the Javascript function
    * storeValues().
    * 
    * @author Stephan Rauh http://www.beyondjava.net
    */
   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException
   {
      ResponseWriter writer = context.getResponseWriter();
      writer.append("\r\n");
      writer.append("  <script>storeValues();</script>");
      writer.append("\r\n");
      writer.append("</body>");
   }
}
