/**
 *  (C) 2013-2015 Stephan Rauh http://www.beyondjava.net
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
package de.beyondjava.angularFaces.components.puiModelSync;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.i18n.I18n;

/**
 * Generates a script tag that loads a script from the JSF resource folders.
 * Originally copied from the Mojarra libraries.
 */
public class PuiScriptRenderer {
    public void encodeScript(FacesContext context, UIComponent component, String name, String library)
          throws IOException {

        Map<Object, Object> contextMap = context.getAttributes();

        String key = name + library;
        
        if (null == name) {
            return;
        }
        
        // Ensure this script is not rendered more than once per request
        if (contextMap.containsKey(key)) {
            return;
        }
        contextMap.put(key, Boolean.TRUE);

        // Special case of scripts that have query strings
        // These scripts actually use their query strings internally, not externally
        // so we don't need the resource to know about them
        int queryPos = name.indexOf("?");
        String query = null;
        if (queryPos > -1 && name.length() > queryPos) {
            query = name.substring(queryPos+1);
            name = name.substring(0,queryPos);
        }


        Resource resource = context.getApplication().getResourceHandler()
              .createResource(name, library);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");

        String resourceSrc;
        if (resource == null) {
            resourceSrc = "RES_NOT_FOUND";
            
            if (context.isProjectStage(ProjectStage.Development)) {
                String msg = "Unable to find resource " + (library == null ? "" : library + ", ") + name;
                context.addMessage(component.getClientId(context),
                               new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                msg,
                                                msg));
            }
            
        } else {
            resourceSrc = resource.getRequestPath();
            if (query != null) {
                resourceSrc = resourceSrc +
                        ((resourceSrc.indexOf("?") > -1) ? "&amp;" : "?") +
                        query;
            }
            resourceSrc = context.getExternalContext().encodeResourceURL(resourceSrc);
        }
        
        writer.writeURIAttribute("src", resourceSrc, "src");
        writer.endElement("script");
    }
}
