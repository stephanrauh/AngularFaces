package de.beyondjava.angularFaces.puiTabview;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * A &lt;pui-tabview&gt; consists of a number of &lt;pui-tabs&gt;, each containing content that's hidden of shown
 * depending on whether the tab is active or not. Only one tab can be active at a time.
 *
 * The programming model is much like the API of the PrimeFaces &lt;tabView&gt; component.
 *
 * Usage:<br />
 * &lt;pui-tabview&gt; <br />
 * &lt;pui-tab title="first tab"&gt;<br />
 * content of first tab<br />
 * &lt;/pui-tab&gt; <br />
 * &lt;pui-tab title="default tab" selected="true"&gt;<br />
 * content of second tab <br />
 * &lt;/pui-tab&gt;<br />
 * &lt;pui-tab title="closable tab" closeable="true"&gt; <br />
 * content of closeable tab<br />
 * &lt;/pui-tab&gt;<br />
 * &lt;/pui-tabview&gt;
 *
 * Kudos: This component's development was helped a lot by a stackoverflow answer:
 * http://stackoverflow.com/questions/20531349/struggling-to-implement-tabs-in-angulardart.
 */
@FacesComponent("de.beyondjava.angularFaces.puiTabview.PuiTabView")
public class PuiTabView extends UIOutput {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiTabview.PuiTabView");

    static {
        LOGGER.info("AngularFaces component 'PuiTabView' is available for use.");
    }

    public PuiTabView() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }
}
