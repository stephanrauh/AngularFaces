package de.beyondjava.jsf.sample.carshop;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class CarBean {
	private Car car;

	public String showDetails(Car car) {
		this.car = car;
		return "details.jsf";
	}

	public String getBrand() {
		return car.getBrand();
	}

	public String getType() {
		return car.getType();
	}

	public String getColor() {
		return car.getColor();
	}

	public int getPrice() {
		return car.getPrice();
	}

	public int getMileage() {
		return car.getMileage();
	}

	public int getYear() {
		return car.getYear();
	}
	
	public String getFuel() {
		return car.getFuel();
	}
}
