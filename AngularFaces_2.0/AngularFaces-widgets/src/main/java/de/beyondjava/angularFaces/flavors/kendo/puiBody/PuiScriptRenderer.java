package de.beyondjava.angularFaces.flavors.kendo.puiBody;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Copied from the Mojarra libraries.
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
