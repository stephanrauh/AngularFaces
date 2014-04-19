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
package de.beyondjava.angularFaces.common;

import de.beyondjava.angularFaces.core.SessionUtils;

/**
 * Stores informations about a particular UIComponent.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class NGUIComponentInfo {
    private String arrayIndex = null;

    private String clientID = null;

    private String prefix = null;

    /**
     * @param prefix2
     * @param arrayIndex2
     * @param clientID
     */
    public NGUIComponentInfo(String prefix, String arrayIndex, String clientID) {
        this.prefix = prefix;
        this.arrayIndex = arrayIndex;
        this.clientID = clientID;
    }

    public String getArrayIndex() {
        return arrayIndex;
    }

    public String getClientID() {
        return clientID;
    }

    /**
     * Return the value of the ng-model attribute.
     * 
     * @return
     */
    public String getNGModel() {
        if (SessionUtils.isDartControllerActive()) {
            String controllerName = SessionUtils.getControllerName();
            if (null != controllerName) {
                return controllerName + "." + clientID;
            }
        }
        return clientID;
    }

    public String getPrefix() {
        return prefix;
    }

}
