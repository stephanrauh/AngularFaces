/*
 * Extended by beyondjava.net 2013.
 * Copyright 2009-2013 PrimeTek.
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
package de.beyondjava.jsfComponents.message;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.UINotificationRenderer;

import de.beyondjava.jsfComponents.common.ELTools;

@FacesRenderer(componentFamily = "de.beyondjava.Message", rendererType = "de.beyondjava.Message")
public class NGMessageRenderer extends UINotificationRenderer
{
   private void readJSR303Annotations(UIComponent component) throws IOException
   {
      Annotation[] annotations = ELTools.readAnnotations(component);
      if (null != annotations)
      {
         for (Annotation a : annotations)
         {
            if (a instanceof Max)
            {
               long maximum = ((Max) a).value();
               max = maximum;
               hasMax = true;
            }
            else if (a instanceof Min)
            {
               long minimum = ((Min) a).value();
               hasMin = true;
               min = minimum;
            }
         }
      }

      Class<?> type = ELTools.getType(component);
      if (type == Integer.class || type == int.class)
      {
         isInteger = true;
      }
      Object o = component.getAttributes().get("required");
      if (null != o)
      {
         isRequired = true;
      }
   }

   private boolean hasMax = false;

   private boolean hasMin = false;

   private boolean isInteger = false;

   private boolean isRequired = false;

   private long max = 0;

   private long min = 0;

   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException
   {
      ResponseWriter writer = context.getResponseWriter();
      NGMessage uiMessage = (NGMessage) component;
      UIComponent target = null;
      String targetID = uiMessage.getFor();
      target = SearchExpressionFacade.resolveComponent(context, component, targetID);

      readJSR303Annotations(target);
      String parentName = getParentName(target);

      String display = uiMessage.getDisplay();
      boolean iconOnly = display.equals("icon");
      boolean escape = uiMessage.isEscape();
      boolean showSummary = uiMessage.isShowSummary();
      boolean showDetail = uiMessage.isShowDetail();

      Iterator<FacesMessage> msgs = context.getMessages(target.getClientId(context));

      writer.startElement("div", uiMessage);
      writer.writeAttribute("ng-show", parentName + target.getClientId() + ".$pristine", null);
      writer.writeAttribute("id", uiMessage.getClientId(context), null);
      writer.writeAttribute("aria-live", "polite", null);

      if (msgs.hasNext())
      {
         FacesMessage msg = msgs.next();
         String severityName = getSeverityName(msg);

         if (!shouldRender(uiMessage, msg, severityName))
         {
            writer.endElement("div");

            return;
         }
         else
         {
            Severity severity = msg.getSeverity();
            String detail = msg.getDetail();
            String summary = msg.getSummary();

            generateMessage(writer, display, iconOnly, escape, severity, detail, summary, showSummary, showDetail);

            msg.rendered();
         }
      }
      writer.endElement("div");
      generateAngularMessages(writer, target.getClientId(), iconOnly, parentName);
   }

   private String getParentName(UIComponent target)
   {
      String parentName = "";
      UIComponent c = target;
      while (c != null)
      {
         c = c.getParent();
         if (null != c)
         {
            String s = (String) c.getAttributes().get("name");
            if (s != null)
               parentName += s + ".";
         }
      }
      return parentName;
   }

   protected void encodeIcon(ResponseWriter writer, String severity, String title, boolean iconOnly) throws IOException
   {
      writer.startElement("span", null);
      writer.writeAttribute("class", "ui-message-" + severity + "-icon", null);
      if (iconOnly)
      {
         writer.writeAttribute("title", title, null);
      }
      writer.endElement("span");
   }

   protected void encodeText(ResponseWriter writer, String text, String severity, boolean escape) throws IOException
   {
      writer.startElement("span", null);
      writer.writeAttribute("class", "ui-message-" + severity, null);

      if (escape)
         writer.writeText(text, null);
      else
         writer.write(text);

      writer.endElement("span");
   }

   private void generateAngularMessages(ResponseWriter writer, String id, boolean iconOnly, String parentName)
         throws IOException
   {
      String sc = "class=\'" + getStyleClass(iconOnly, FacesMessage.SEVERITY_ERROR) + "'";
      if (isInteger)
      {
         writer.append("<div " + sc + " ng-show='!" + parentName + id + ".$pristine && " + parentName + id
               + ".$error.integer'>");
         writer.append(" This is not an integer.</div>");
      }
      if (hasMin)
      {
         writer.append("<div " + sc + " ng-show='!" + parentName + id + ".$pristine && " + parentName + id
               + ".$error.min'>");
         writer.append(" must be greater than or equal to " + min + ".</div>");
      }
      if (hasMax)
      {
         writer.append("<div " + sc + " ng-show='!" + parentName + id + ".$pristine && " + parentName + id
               + ".$error.max'>");
         writer.append(" must be less or equal to " + max + ".");
         writer.append("</div>");
      }
      if (isRequired)
      {
         writer.append("<div " + sc + " ng-show='!" + parentName + id + ".$pristine && " + parentName + id
               + ".$error.required'>");
         writer.append(" Value is required.</div>");
      }

   }

   private void generateMessage(ResponseWriter writer, String display, boolean iconOnly, boolean escape,
         Severity severity, String detail, String summary, boolean showSummary, boolean showDetail) throws IOException
   {
      String severityKey = encodeStyleClass(writer, iconOnly, severity);

      if (!display.equals("text"))
      {
         encodeIcon(writer, severityKey, detail, iconOnly);
      }

      if (!iconOnly)
      {
         if (showSummary)
         {
            encodeText(writer, summary, severityKey + "-summary", escape);
         }
         if (showDetail)
            encodeText(writer, detail, severityKey + "-detail", escape);
      }
   }

   private String encodeStyleClass(ResponseWriter writer, boolean iconOnly, Severity severity) throws IOException
   {
      String styleClass = getStyleClass(iconOnly, severity);

      writer.writeAttribute("class", styleClass, null);
      return getSeverityKey(severity);
   }

   private String getStyleClass(boolean iconOnly, Severity severity)
   {
      String severityKey = getSeverityKey(severity);

      String styleClass = "ui-message-" + severityKey + " ui-widget ui-corner-all";
      if (iconOnly)
      {
         styleClass = styleClass + " ui-message-icon-only ui-helper-clearfix";
      }
      return styleClass;
   }

   private String getSeverityKey(Severity severity)
   {
      String severityKey = null;

      if (severity.equals(FacesMessage.SEVERITY_ERROR))
         severityKey = "error";
      else if (severity.equals(FacesMessage.SEVERITY_INFO))
         severityKey = "info";
      else if (severity.equals(FacesMessage.SEVERITY_WARN))
         severityKey = "warn";
      else if (severity.equals(FacesMessage.SEVERITY_FATAL))
         severityKey = "fatal";
      return severityKey;
   }

   public void setHasMax(boolean hasMax)
   {
      this.hasMax = hasMax;
   }

   public void setHasMin(boolean hasMin)
   {
      this.hasMin = hasMin;
   }

   public void setInteger(boolean isInteger)
   {
      this.isInteger = isInteger;
   }

   public void setMax(long max)
   {
      this.max = max;
   }

   public void setMin(long min)
   {
      this.min = min;
   }

   public void setRequired(boolean isRequired)
   {
      this.isRequired = isRequired;
   }
}