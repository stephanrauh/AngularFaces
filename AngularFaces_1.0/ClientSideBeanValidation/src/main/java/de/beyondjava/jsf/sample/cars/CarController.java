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
package de.beyondjava.jsf.sample.cars;

import java.util.*;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class CarController {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.sample.cars.CarController");

    private List<Car> cars = new ArrayList<>();

    /**
     *
     */
    public CarController() {
        cars.add(new Car());
    }

    /**
     * @return the cars
     */
    public List<Car> getCars() {
        return this.cars;
    }

    public String save() {
        LOGGER.info("save action called");
        if (true) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Validation failed"));
            return null;
        }
        return "table.jsf";
    }

    /**
     * @param cars
     *            the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

}
