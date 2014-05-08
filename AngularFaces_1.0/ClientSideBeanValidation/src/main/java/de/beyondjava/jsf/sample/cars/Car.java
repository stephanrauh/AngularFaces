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

import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.validation.constraints.*;

@ManagedBean
public class Car {
    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.sample.cars.Car");

    @Size(min = 4, max = 10)
    private String brand = "Honda";

    @Size(min = 5, max = 5)
    private String type = "Civic";

    @Min(2000)
    @Max(2015)
    private int year = 2008;

    /**
     *
     */
    public Car() {

    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return this.brand;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * @param brand
     *            the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }
}
