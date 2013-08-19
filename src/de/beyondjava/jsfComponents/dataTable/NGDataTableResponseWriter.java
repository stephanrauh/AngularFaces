/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.dataTable;

import java.io.*;
import java.util.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGDataTableResponseWriter extends HtmlResponseWriter {
   private UIComponent currentComponent;
   String currentElement = "";
   String currentNGModel = null;
   boolean ngModelAlreadyWritten;
   String ngPrefix = "";
   /** collects a data table's value attributes */
   private List<String> valueExpressions = new ArrayList<>();

   /**
    * @param writer
    * @param contentType
    * @param encoding
    * @throws FacesException
    */
   public NGDataTableResponseWriter(Writer writer, String contentType, String encoding, String ngPrefix)
         throws FacesException {
      super(writer, contentType, encoding);
      if ((null != ngPrefix) && (ngPrefix.length() > 0)) {
         this.ngPrefix = ngPrefix + ".";
      }
   }

   /**
    * @return the valueExpressions
    */
   public List<String> getValueExpressions() {
      return valueExpressions;
   }

   /**
    * @param valueExpressions
    *           the valueExpressions to set
    */
   public void setValueExpressions(List<String> valueExpressions) {
      this.valueExpressions = valueExpressions;
   }

   @Override
   public void startElement(String arg0, UIComponent arg1) throws IOException {
      currentElement = arg0;
      currentComponent = arg1;
      if (null != currentComponent) {
         if (ELTools.hasValueExpression(currentComponent)) {
            ngModelAlreadyWritten = false;
            String valueExpression = ELTools.getCoreValueExpression(currentComponent);
            getValueExpressions().add(valueExpression);
            currentNGModel = ELTools.getNGModel(currentComponent);
            if (!currentComponent.getPassThroughAttributes().containsKey("ng-model")) {
               currentComponent.getPassThroughAttributes().put("ng-model", ngPrefix + currentNGModel);
            }
         }
      }
      super.startElement(arg0, arg1);
   }

   @Override
   public void write(String str) throws IOException {
      if ("select".equals(currentElement)) {
         if (str.contains("<option ")) {
            str = str.replaceAll("selected=\"selected\"", "");
         }
      }
      super.write(str);
   }

   @Override
   public void writeAttribute(String attribute, Object value, String arg2) throws IOException {
      if ("ng-model".equals(attribute)) {
         super.writeAttribute(attribute, ngPrefix + value, arg2);
         ngModelAlreadyWritten = true;
      }
      else if ((!"value".equals(attribute)) && (!"checked".equals(attribute))) {
         // value attributes have to be suppressed, because they collide with
         // ng-model's magic
         super.writeAttribute(attribute, value, arg2);
      }
   }
}
