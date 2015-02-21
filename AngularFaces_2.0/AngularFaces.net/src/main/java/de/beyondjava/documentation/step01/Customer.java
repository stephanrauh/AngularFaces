package de.beyondjava.documentation.step01;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@RequestScoped
@ManagedBean
public class Customer {
    @NotEmpty
    private String firstName = "John";
    @NotEmpty
    private String lastName = "Doe";

    @Past
    private Date dateOfBirth;

    @Email
    private String emailAddress;

    @AssertTrue
    private boolean iAgreeToTheTermsAndConditions = false;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isIAgreeToTheTermsAndConditions() {
        return iAgreeToTheTermsAndConditions;
    }

    public void setIAgreeToTheTermsAndConditions(boolean iAgreeToTheTermsAndConditions) {
        this.iAgreeToTheTermsAndConditions = iAgreeToTheTermsAndConditions;
    }

    public void save() {
    }
}
