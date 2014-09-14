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
package de.beyondjava.jsf.sample.carshop;

import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@SessionScoped
public class FilterBean {
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.sample.carshop.FilterBean");

	
    String brand = "Honda";
    @NotEmpty
    String color = "silver";

	String type = "Civic";

	@Min(1886)
    @Max(2014)
    @NotEmpty
    int year;

	String mileage = "< 200000 km";

	String price;

	String fuel = "diesel";

	public String getBrand() {
		return brand;
	}

	public String getColor() {
		return color;
	}

	public String getFuel() {
		return fuel;
	}

	public String getMileage() {
		return mileage;
	}

	public String getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}

	public int getYear() {
		return year;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void doFilter(AjaxBehaviorEvent event) {
		LOGGER.info("doFilter called");
	}
	public String doFilterAction() {
		LOGGER.info("doFilterAction called");
		return null;
	}
	
	public String getCounter() {
		double d = Math.random()*1000000;
		long l = (long)Math.floor(d + 0.5d);
		if (null != brand) return String.valueOf(l) + " " + brand; 
		return String.valueOf(l);
	}

}
