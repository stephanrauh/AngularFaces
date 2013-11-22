/**
 * This class isn't used currently, but it might be reactived soon.
 * 
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.renderkit;

import com.sun.faces.renderkit.RenderKitFactoryImpl;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class UnusedBJRenderKitFactoryImpl extends RenderKitFactoryImpl {
   public UnusedBJRenderKitFactoryImpl() {
      super();
      renderKits.remove(HTML_BASIC_RENDER_KIT);
      addRenderKit(HTML_BASIC_RENDER_KIT, new UnusedBJRenderKitImpl());
   }

}
