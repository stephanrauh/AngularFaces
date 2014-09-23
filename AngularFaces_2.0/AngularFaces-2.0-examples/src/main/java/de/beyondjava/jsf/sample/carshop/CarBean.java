package de.beyondjava.jsf.sample.carshop;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CarBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private Car car;
	
	public CarBean() {
		
	}

	public String showDetails(Car car) {
		this.car = car;
		return "details.jsf";
	}
	public String showDetails2() {
		Map<String,String> params = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	  String selection = params.get("selectedCar");
		return "details.jsf";
	}

	public String getBrand() {
		return car.getBrand();
	}
	
	public void setBrand(String brand) {
		car.setBrand(brand);
	}

	public String getType() {
		return car.getType();
	}

	public void setType(String type) {
		car.setType(type);
	}

	public String getColor() {
		return car.getColor();
	}

	public void setColor(String color) {
		car.setColor(color);
	}

	public int getPrice() {
		return car.getPrice();
	}

	public void setPrice(int price) {
		car.setPrice(price);
	}

	public int getMileage() {
		return car.getMileage();
	}

	public void setMileage(int mileage) {
		car.setMileage(mileage);
	}

	public int getYear() {
		return car.getYear();
	}

	public void setYear(int year) {
		car.setYear(year);
	}

	public String getFuel() {
		return car.getFuel();
	}
	public void setFuel(String fuel) {
		car.setFuel(fuel);
	}
}
