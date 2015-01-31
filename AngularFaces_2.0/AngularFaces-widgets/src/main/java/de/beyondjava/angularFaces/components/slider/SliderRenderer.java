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
package de.beyondjava.angularFaces.components.slider;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/** Generates a BootsFaces slider running on AngularJS. */
@FacesRenderer(componentFamily = "de.beyondjava", rendererType = "de.beyondjava.angularFaces.components.slider")
public class SliderRenderer extends Renderer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.sliderRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		Slider s = (Slider) component;
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		// ="{orientation: 'horizontal', range: 'min'}
		String settings = ",";
		if (null != s.getOrientation() && s.getOrientation().length() > 0) {
			settings += "orientation:'" + s.getOrientation() + "',";
		}
//		settings += "range:true" + ",";
		settings = settings.substring(1, settings.length() - 1);
		writer.writeAttribute("ui-slider", "{" + settings + "}", "ui-slider");
		String max = "0";
		if (s.getMax() != 0)
			max = String.valueOf(s.getMax());
		writer.writeAttribute("max", max, "max");
		String min = "0";
		if (s.getMin() != 0)
			min = String.valueOf(s.getMin());
		writer.writeAttribute("min", min, "min");
		String step = "1";
		if (s.getStep() != 0)
			step = String.valueOf(s.getStep());
		writer.writeAttribute("step", step, "step");
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

}
