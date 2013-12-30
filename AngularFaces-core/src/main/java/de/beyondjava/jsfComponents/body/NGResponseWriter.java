/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsfComponents.body;

import java.io.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGResponseWriter extends HtmlResponseWriter {
   private UIComponent currentComponent;
   String currentElement = "";
   String currentNGModel = null;
   String ngPrefix = "";

   /**
    * @param writer
    * @param contentType
    * @param encoding
    * @throws FacesException
    */
   public NGResponseWriter(Writer writer, String contentType, String encoding, String ngPrefix) throws FacesException {
      super(writer, contentType, encoding);
      if ((null != ngPrefix) && (ngPrefix.length() > 0)) {
         this.ngPrefix = ngPrefix + ".";
      }
   }

   @Override
   public void startElement(String arg0, UIComponent arg1) throws IOException {
      currentElement = arg0;
      currentComponent = arg1;
      if (null != currentComponent) {
         if (ELTools.hasValueExpression(currentComponent)) {
            if (!currentElement.equals("div")) {
               if (!(currentComponent instanceof SelectOneMenu)) {
                  currentNGModel = ELTools.getNGModel(currentComponent);
                  if (!currentComponent.getPassThroughAttributes().containsKey("ng-model")) {
                     currentComponent.getPassThroughAttributes().put("ng-model", ngPrefix + currentNGModel);
                  }
               }
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

   // @Override
   // public void writeAttribute(String attribute, Object value, String arg2)
   // throws IOException {
   // if ("ng-model".equals(attribute)) {
   // super.writeAttribute(attribute, ngPrefix + value, arg2);
   // }
   // else if ((!"value".equals(attribute)) && (!"checked".equals(attribute)))
   // {
   // // value attributes have to be suppressed, because they collide with
   // // ng-model\"s magic
   // super.writeAttribute(attribute, value, arg2);
   // }
   // }
}
