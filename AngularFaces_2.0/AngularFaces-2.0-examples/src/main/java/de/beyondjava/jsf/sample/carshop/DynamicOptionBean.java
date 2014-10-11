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
public class DynamicOptionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> brand2Type = new HashMap<String, String>();
	private List<String> brands = new ArrayList<String>();

	private List<String> types = new ArrayList<String>();

	public DynamicOptionBean() {
		initBrandsAndTypes();

		brands.add("");
		brands.add("Honda");
		brands.add("VW");
		brands.add("BMW");
		brands.add("Volvo");
		brands.add("Opel");
		brands.add("Renault");
		brands.add("Citroen");
		brands.add("Seat");
		brands.add("Fiat");

		types.add("");
		types.add("Civic");
		types.add("Golf");
		types.add("320");
		types.add("V50");
		types.add("Astra");
		types.add("Megane");
		types.add("Picasso");
		types.add("Ibiza");
		types.add("Punto");
	}

	public List<String> getBrands() {
		return brands;
	}

	public String getBrandToType(String type) {
		if (type == null)
			return "";
		return brand2Type.get(type);
	}

	public List<String> getTypes() {
		return getTypesToBrand(brand);
	}

	public List<String> getTypesToBrand(String brand) {
		if (brand == null || brand.length() == 0)
			return types;
		List<String> l = new ArrayList<String>();

		Set<Entry<String, String>> entrySet = brand2Type.entrySet();
		Iterator<Entry<String, String>> iter = entrySet.iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			if (entry.getValue().equals(brand)) {
				l.add(entry.getKey());
			}
		}
		if (l.size() > 0)
			l.add(0, "");
		return l;
	}

	public void initBrandsAndTypes() {
		brand2Type.put("Civic", "Honda");
		brand2Type.put("Jazz", "Honda");
		brand2Type.put("Golf", "VW");
		brand2Type.put("Passat", "VW");
		brand2Type.put("Polo", "VW");
		brand2Type.put("320", "BMW");
		brand2Type.put("C40", "Volvo");
		brand2Type.put("V50", "Volvo");
		brand2Type.put("C60", "Volvo");
		brand2Type.put("V70", "Volvo");
		brand2Type.put("Corsa", "Opel");
		brand2Type.put("Astra", "Opel");
		brand2Type.put("Vectra", "Opel");
		brand2Type.put("Picasso", "Citroen");
		brand2Type.put("León", "Seat");
		brand2Type.put("Ibiza", "Seat");
		brand2Type.put("Exeo", "Seat");
		brand2Type.put("Punto", "Fiat");
		brand2Type.put("500", "Fiat");
		brand2Type.put("Panda", "Fiat");
		brand2Type.put("Megane", "Renault");
	}

	private String brand=null;
	private String type=null;
	public void setBrandAndType(String brand, String type) {
		this.brand=brand;
		this.type=type;
		
	}
}
