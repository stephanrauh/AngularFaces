/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.loanCalculator;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;

import org.primefaces.context.RequestContext;

@ManagedBean
@RequestScoped
public class AmortizationPrinterBean {
   private ArrayList<AmortizationRow> amortizationPlan = new ArrayList<AmortizationRow>();

   private double interestRate = 5;

   private double loanAmount = 10000;

   private double loanTerm; // Dauer des Kredits

   private double monthlyPayments = 200;

   public void generateAmortizationPlan() {
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Yet to be implemented",
            "This demo is work in progress.<br>Please be patient.");
      RequestContext.getCurrentInstance().showMessageInDialog(message);
   }

   public ArrayList<AmortizationRow> getAmortizationPlan() {
      return amortizationPlan;
   }

   public double getInterestRate() {
      return interestRate;
   }

   public double getLoanAmount() {
      return loanAmount;
   }

   public double getLoanTerm() {
      return loanTerm;
   }

   public double getMonthlyPayments() {
      return monthlyPayments;
   }

   public void setAmortizationPlan(ArrayList<AmortizationRow> amortizationPlan) {
      this.amortizationPlan = amortizationPlan;
   }

   public void setInterestRate(double interestRate) {
      this.interestRate = interestRate;
   }

   public void setLoanAmount(double loanAmount) {
      this.loanAmount = loanAmount;
   }

   public void setLoanTerm(double loanTerm) {
      this.loanTerm = loanTerm;
   }

   public void setMonthlyPayments(double monthlyPayments) {
      this.monthlyPayments = monthlyPayments;
   }

}
