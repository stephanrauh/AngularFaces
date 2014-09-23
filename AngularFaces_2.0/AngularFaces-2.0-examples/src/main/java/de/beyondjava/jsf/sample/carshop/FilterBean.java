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
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.beyondjava.angularFaces.core.ELTools;

@ManagedBean
@SessionScoped
public class FilterBean implements Serializable {
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.jsf.sample.carshop.FilterBean");
	
	@ManagedProperty("#{optionBean}")
	private OptionBean options;

	@ManagedProperty("#{carPool}")
	private transient CarPool carPool;

	public void setCarPool(CarPool carPool) {
		this.carPool = carPool;
	}

	public void setOptions(OptionBean options) {
		this.options = options;
	}
	
	/**
	 * This bean is created both by JSF and by the JSON deserializer (Jackson). In the latter case there's not JSF initialization, so we may have to catch up on it.
	 */
	private void initIfNecessary() {
		if (null==options) {
			options=(OptionBean) ELTools.evalAsObject("#{optionBean}");
		}
		if (null==carPool) {
			carPool=(CarPool) ELTools.evalAsObject("#{carPool}");
		}
	}

	private String brand;

    private String color;

    private String type;

    private String yearText;

	private String mileage;

	private String price;

	private String fuel;

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
	
	@JsonIgnore
	public List<String> getTypes() {
		initIfNecessary();
		return options.getTypesToBrand(brand);
	}


	public String getType() {
		return type;
	}

	public int getYear() {
		if (yearText==null || yearText.length()<4) return 0;
		try {
			String year = yearText.substring(0, 4);
			return Integer.parseInt(year);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}

	public void setBrand(String brand) {
		this.brand = brand;
		if (type!=null && type.length()>0) {
			initIfNecessary();
			if (!brand.equals(options.getBrandToType(type)))
				type=null;
		}
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
		initIfNecessary();
		String b = options.getBrandToType(type);
		if (null != b) {
			brand=b;
		}
	}
	
	public void setYearText(String year) {
		yearText=year;
	}
	
	public String getYearText() {
		return yearText;
	}
	
	public void doFilter(AjaxBehaviorEvent event) {
		LOGGER.info("doFilter called");
	}
	public String doFilterAction() {
		LOGGER.info("doFilterAction called");
		return null;
	}
	
	@JsonIgnore
	public String getCounter() {
		initIfNecessary();
		carPool.applyFilter(this);
		long l=carPool.getSelectedCars().size();
//		if (null != brand) return String.valueOf(l) + " " + brand; 
		return String.valueOf(l);
	}
}
