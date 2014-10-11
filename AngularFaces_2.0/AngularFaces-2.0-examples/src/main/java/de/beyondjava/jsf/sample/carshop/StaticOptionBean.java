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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class StaticOptionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<String> colors = new ArrayList<String>();

	private List<String> fuels = new ArrayList<String>();

	private List<String> mileages = new ArrayList<String>();
	private List<String> prices = new ArrayList<String>();
	private List<String> years = new ArrayList<String>();

	public StaticOptionBean() {
		colors.add("");
		colors.add("red");
		colors.add("white");
		colors.add("blue");
		colors.add("yellow");
		colors.add("green");
		colors.add("black");
		colors.add("white");
		colors.add("silver");

		prices.add("");
		prices.add("< €500");
		prices.add("< €1000");
		prices.add("< €2000");
		prices.add("< €3000");
		prices.add("< €4000");
		prices.add("< €5000");
		prices.add("< €7500");
		prices.add("< €10000");
		prices.add("< €15000");
		prices.add("< €20000");
		prices.add("< €30000");
		prices.add("< €40000");
		prices.add("< €50000");

		mileages.add("");
		mileages.add("< 100 km");
		mileages.add("< 2000 km");
		mileages.add("< 5000 km");
		mileages.add("< 10000 km");
		mileages.add("< 25000 km");
		mileages.add("< 50000 km");
		mileages.add("< 100000 km");
		mileages.add("< 200000 km");

		fuels.add("");
		fuels.add("gasoline");
		fuels.add("diesel");
		fuels.add("hybrid");
		fuels.add("electric");

		years.add("");
		int year = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 0; i < 10; i++) {
			years.add(new Integer(year - i).toString() + " or younger");
		}
		for (int i = 10; i < 20; i += 3) {
			years.add(new Integer(year - i).toString() + " or younger");
		}
		for (int i = 20; i < 50; i += 5) {
			years.add(new Integer(year - i).toString() + " or younger");
		}
	}

	public List<String> getColors() {
		return colors;
	}

	public List<String> getFuels() {
		return fuels;
	}

	public List<String> getMileages() {
		return mileages;
	}

	public List<String> getPrices() {
		return prices;
	}

	public List<String> getYears() {
		return years;
	}
}
