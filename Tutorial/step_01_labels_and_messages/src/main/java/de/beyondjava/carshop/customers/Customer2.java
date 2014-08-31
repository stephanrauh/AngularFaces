package de.beyondjava.carshop.customers;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ManagedBean
@ViewScoped
public class Customer2 {
	@NotNull
	@Size(min=5, max=20)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		if (null!=lastName) return lastName.toUpperCase();
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@NotNull
	@Size(min=5, max=20)
	private String lastName;

}
