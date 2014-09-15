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
package de.beyondjava.angularFaces.core;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import de.beyondjava.angularFaces.common.IAngularController;

public interface NGModelRendererUtils extends RendererUtils {
    /**
     * Extracts the ng-model attribute from the bean attribute name.
     *
     * @param input
     * @param writer
     * @return
     * @throws IOException
     */
    public default String renderNGModel(UIComponent input, ResponseWriter writer)  {
        String coreValueExpression = ELTools.getCoreValueExpression(input);
        while ((input != null) && (!(input instanceof IAngularController))) {
            input = input.getParent();
        }
        if (null != input) {
            String publishAs = ((IAngularController) input).getPublishAs();
            String selector = ((IAngularController) input).getSelector();
            coreValueExpression = coreValueExpression.replace(selector, publishAs);
        }
        renderNonEmptyAttribute(writer, "ng-model", coreValueExpression);
        return coreValueExpression;
    }

}
