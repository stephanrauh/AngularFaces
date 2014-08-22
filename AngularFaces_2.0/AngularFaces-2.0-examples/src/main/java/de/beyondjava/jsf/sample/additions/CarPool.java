package de.beyondjava.jsf.sample.additions;

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
			new Car("Honda", "Civic", 2009, "silver"), new Car("Carriage", "Stanhope", 333, "black"), new Car("Volvo", "V40", 2001, "green"),
			new Car("Opel", "Corsa", 1999, "red"), new Car("Opel", "Kadett", 1992, "white"), new Car("Scoda", "Octavia", (2001), "silver"),
			new Car("Renault", "R4", (1972), "red"), new Car("BMW", "E30", (1982), "blue"), new Car("Volvo", "V70", (2007), "red"),
			new Car("Fiat", "Panda", (2008), "black")
	);
	
	public List<Car> getCarPool() {
		return carPool;
	}

	public void setCarPool(List<Car> carpool) {
		this.carPool = carpool;
	}
}
