package de.beyondjava.carshop.customers;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@ManagedBean
@ViewScoped
public class Customer3 {
	private Date dateOfBirth;
	
	@Email
	private String emailAdress;
	
	@NotNull
	@Size(min=5, max=20)
	private String firstName;
	
	@NotNull
	@Size(min=5, max=20)
	private String lastName;
	
	private boolean stayAnonymous;
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public String getEmailAdress() {
		return emailAdress;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public boolean isStayAnonymous() {
		return stayAnonymous;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		if (null != lastName) lastName=lastName.toUpperCase();
		this.lastName = lastName;
	}
	public void setStayAnonymous(boolean stayAnonymous) {
		this.stayAnonymous = stayAnonymous;
	}
}
