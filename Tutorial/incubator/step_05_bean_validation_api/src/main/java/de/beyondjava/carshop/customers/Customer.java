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
