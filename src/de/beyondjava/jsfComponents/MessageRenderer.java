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
package de.beyondjava.jsfComponents;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.UINotificationRenderer;

@FacesRenderer(componentFamily = "de.beyondjava.Message", rendererType = "de.beyondjava.Message")
public class MessageRenderer extends UINotificationRenderer
{

   private boolean hasMax = false;

   private boolean hasMin = false;

   private boolean isInteger = false;

   private boolean isRequired = false;

   private long max = 0;

   private long min = 0;

   public void encodeEnd(FacesContext context, UIComponent component, UIComponent target) throws IOException
   {
      ResponseWriter writer = context.getResponseWriter();

      Message uiMessage = (Message) component;
      String display = uiMessage.getDisplay();
      boolean iconOnly = display.equals("icon");
      boolean escape = uiMessage.isEscape();
      boolean showSummary = uiMessage.isShowSummary();
      boolean showDetail = uiMessage.isShowDetail();

      Iterator<FacesMessage> msgs = context.getMessages(target.getClientId(context));

      writer.startElement("div", uiMessage);
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
      generateAngularMessages(writer, target.getClientId(), iconOnly);
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

   private void generateAngularMessages(ResponseWriter writer, String id, boolean iconOnly) throws IOException
   {
      String sc = "class=\'" + getStyleClass(iconOnly, FacesMessage.SEVERITY_ERROR) + "'";
      if (isInteger)
      {
         writer.append("<div " + sc + " ng-show='myform." + id
               + ".$error.integer'>");
         encodeIcon(writer, "error", "", iconOnly);
         writer.append(" This is not an integer.(Angular)</div>");
      }
      if (hasMin)
      {
         writer.append("<div " + sc + " ng-show='myform." + id
               + ".$error.min'>");
         encodeIcon(writer, "error", "", iconOnly);
         writer.append(" must be greater than or equal to " + min + ".(Angular)</div>");
      }
      if (hasMax)
      {
         writer.append("<div " + sc + " ng-show='myform." + id + ".$error.max'>");
         encodeIcon(writer, "error", "", iconOnly);
         writer.append(" must be less or equal to "
               + max + ". (Angular)");
         writer.append("</div>");
      }
      if (isRequired)
      {
         writer.append("<div " + sc + " ng-show='myform." + id
               + ".$error.required'>");
         encodeIcon(writer, "error", "", iconOnly);
         writer.append(" Value is required.(Angular)</div>");
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