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
package de.beyondjava.jsf.sample.additions;

import javax.faces.bean.ManagedBean;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.NotEmpty;

public class Car {
    String brand;
    @NotEmpty
    String color;

	String type;

	@Min(1886)
    @Max(2014)
    @NotEmpty
    int year;
	
	public Car(String brand, String type, int year, String color) {
    	this.brand=brand;
    	this.type=type;
    	this.year=year;
    	this.color=color;
    }

	public String getBrand() {
		return brand;
	}

	public String getColor() {
		return color;
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

    public void setType(String type) {
		this.type = type;
	}
    
    public void setYear(int year) {
		this.year = year;
	}
}
