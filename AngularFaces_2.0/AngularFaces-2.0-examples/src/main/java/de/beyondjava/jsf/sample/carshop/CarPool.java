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

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class CarPool {
	private List<Car> carPool = Arrays.asList(
	new Car("Honda", "Civic", 2008, "silver"), new Car("Carriage", "Stanhope", 332, "black"), new Car("Volvo", "V40", 2002, "green"),
			new Car("Opel", "Corsa", 1997, "red"), new Car("Opel", "Kadett", 1990, "white"), new Car("Scoda", "Octavia", (2000), "silver"),
			new Car("Renault", "R4", (1970), "red"), new Car("BMW", "E30", (1980), "blue"), new Car("Volvo", "V70", (2006), "red"),
			new Car("Fiat", "Panda", (2003), "black"),
			new Car("Honda", "Civic", 2009, "red"), new Car("Carriage", "Stanhope", 333, "black"), new Car("Volvo", "V40", 2001, "green"),
			new Car("Opel", "Corsa", 2000, "blue"), new Car("Opel", "Kadett", 1992, "white"), new Car("Scoda", "Octavia", (2001), "silver"),
			new Car("Renault", "R4", (1972), "red"), new Car("BMW", "E30", (1982), "blue"), new Car("Volvo", "V70", (2007), "red"),
			new Car("Fiat", "Panda", (2008), "black"),new Car("Opel", "Astra", 2014, "black")
	);
	
	public List<Car> getCarPool() {
		return carPool;
	}

	public void setCarPool(List<Car> carpool) {
		this.carPool = carpool;
	}
}
