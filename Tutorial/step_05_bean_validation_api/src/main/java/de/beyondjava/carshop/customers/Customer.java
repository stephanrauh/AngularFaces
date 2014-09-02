package de.beyondjava.carshop.customers;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@ManagedBean
@SessionScoped
public class Customer {
	private Date dateOfBirth;
	
	@Email
	private String emailAddress;
	
	@NotNull
	@Size(min=5, max=20)
	private String firstName;
	
	private boolean iAgreeToTheTermsAndConditions;
	
	@NotNull
	@Size(min=5, max=20)
	private String lastName;
	
	@Min(18)
	@Max(130)
	private int age;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	private boolean stayAnonymous;
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public boolean isIAgreeToTheTermsAndConditions() {
		return iAgreeToTheTermsAndConditions;
	}
	public boolean isStayAnonymous() {
		return stayAnonymous;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setIAgreeToTheTermsAndConditions(boolean iAgreeToTheTermsAndConditions) {
		this.iAgreeToTheTermsAndConditions = iAgreeToTheTermsAndConditions;
	}
	public void setLastName(String lastName) {
		if (null != lastName) lastName=lastName.toUpperCase();
		this.lastName = lastName;
	}
	public void setStayAnonymous(boolean stayAnonymous) {
		this.stayAnonymous = stayAnonymous;
	}
	
	public void save() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Customer data saved."));
	}
}
