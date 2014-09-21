package de.beyondjava.jsf.sample.carshop;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ManagedBean
@ViewScoped
public class NewCarBean {
	private Car car = new Car(null, null, 0, null, 0, null, 0);

	@NotNull
	@Size(min=2, max=10)
	public String getBrand() {
		return car.getBrand();
	}

	@NotNull
	@Size(min=2, max=10)
	public String getColor() {
		return car.getColor();
	}

	@NotNull
	@Size(min=2, max=10)
	public String getFuel() {
		return car.getFuel();
	}

	@Min(0)
	@Max(1000000)
	public int getMileage() {
		return car.getMileage();
	}

	@Min(1)
	@Max(5000000)
	public int getPrice() {
		return car.getPrice();
	}

	@NotNull
	@Size(min=1, max=10)
	public String getType() {
		return car.getType();
	}

	@NotNull
	@Min(1886)
    @Max(2014)
	public int getYear() {
		return car.getYear();
	}

	public void setBrand(String brand) {
		car.setBrand(brand);
	}

	public void setColor(String color) {
		car.setColor(color);
	}

	public void setFuel(String fuel) {
		car.setFuel(fuel);
	}

	public void setMileage(int mileage) {
		car.setMileage(mileage);
	}

	public void setPrice(int price) {
		car.setPrice(price);

	}

	public void setType(String type) {
		car.setType(type);

	}

	public void setYear(int year) {
		car.setYear(year);
	}
}
