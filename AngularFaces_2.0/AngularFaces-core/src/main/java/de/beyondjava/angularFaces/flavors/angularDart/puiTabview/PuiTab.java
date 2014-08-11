package de.beyondjava.angularFaces.flavors.angularDart.puiTabview;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

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
@FacesComponent("de.beyondjava.angularFaces.puiTabview.PuiTab")
public class PuiTab extends UIOutput {
    enum propertyKeys {
        closeable, selected, title
    }

    public String getCloseable() {
        return (String) getStateHelper().eval(propertyKeys.closeable, null);
    }

    public String getSelected() {
        return (String) getStateHelper().eval(propertyKeys.selected, null);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(propertyKeys.title, null);
    }

    public void setCloseable(String closeable) {
        getStateHelper().put(propertyKeys.closeable, closeable);
    }

    public void setSelected(String selected) {
        getStateHelper().put(propertyKeys.selected, selected);
    }

    public void setTitle(String title) {
        getStateHelper().put(propertyKeys.title, title);
    }

}
