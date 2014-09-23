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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CarPool implements Serializable {
	private static final long serialVersionUID = 1L;

	private final static int SIZE_OF_INITIAL_CAR_POOL = 10000;

	@ManagedProperty("#{optionBean}")
	private OptionBean options;

	private List<String> types;

	private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

	public void setOptions(OptionBean options) {
		this.options = options;
	}

	private List<Car> carPool;

	public List<Car> getCarPool() {
		return carPool;
	}

	private List<Car> selectedCars;

	public List<Car> getSelectedCars() {
		return selectedCars;
	}

	@PostConstruct
	private void initRandomCarPool() {
		types = options.getTypesToBrand(null);
		carPool = new ArrayList<Car>();
		for (int i = 0; i < SIZE_OF_INITIAL_CAR_POOL; i++) {
			carPool.add(getRandomCar());
		}
		selectedCars = carPool;
	}

	public void setCarPool(List<Car> carpool) {
		this.carPool = carpool;
	}

	private Car getRandomCar() {
		int typeIndex = (int) Math.floor(Math.random() * (types.size() - 1));
		String type = types.get(typeIndex + 1);
		String brand = options.getBrandToType(type);
		int year = (int) (Math.floor((currentYear - 1980) * Math.random())) + 1980;
		int age = currentYear - year;

		int price = 60000 / (1 + age) + (int) Math.floor(Math.random() * 10000);

		int mileage = (int) (Math.floor((age + 1) * 20000 * Math.random()));

		int colorIndex = (int) Math.floor(Math.random() * (options.getColors().size() - 1));
		String color = options.getColors().get(colorIndex + 1);

		int fuelIndex = (int) Math.floor(Math.random() * (options.getFuels().size() - 1));
		String fuel = options.getFuels().get(fuelIndex + 1);

		Car c = new Car(brand, type, year, color, mileage, fuel, price);
		return c;
	}

	public void applyFilter(FilterBean b) {
		selectedCars = new ArrayList<Car>();
		for (Car c : carPool) {
			String criterium = b.getBrand();
			if (criterium != null && criterium.length() > 0) {
				if (!c.getBrand().equals(criterium))
					continue;
			}
			criterium = b.getType();
			if (criterium != null && criterium.length() > 0) {
				if (!c.getType().equals(criterium))
					continue;
			}
			criterium = b.getColor();
			if (criterium != null && criterium.length() > 0) {
				if (!c.getColor().equals(criterium))
					continue;
			}
			criterium = b.getFuel();
			if (criterium != null && criterium.length() > 0) {
				if (!c.getFuel().equals(criterium))
					continue;
			}
			int year = b.getYear();
			if (c.getYear() < year)
				continue;
			criterium= b.getMileage();
			if (criterium != null && criterium.length() > 0) {
				int km = Integer.parseInt(criterium.substring(2, criterium.length()-3));
				if (c.getMileage() > km)
					continue;
			}
			criterium= b.getPrice();
			if (criterium != null && criterium.length() > 0) {
				int euro = Integer.parseInt(criterium.substring(3, criterium.length()));
				if (c.getPrice() > euro)
					continue;
			}
			selectedCars.add(c);
		}
	}

}
