/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.loanCalculator;

import java.util.*;

public class AmortizationPrinterBean {
   private List<AmortizationRow> amortizationPlan = new ArrayList<>();

   private double interestRate = 5;

   private double loanAmount = 10000;

   private double loanTerm = 36; // Dauer des Kredits

   private double monthlyPayments = 200;

   public AmortizationPrinterBean() {
      // amortizationPlan.add(new AmortizationRow(10000.0d, 41));
      // amortizationPlan.add(new AmortizationRow(9000.0d, 36));
   }

   public List<AmortizationRow> getAmortizationPlan() {
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

   public void setInterestRate(Double interestRate) {
      this.interestRate = interestRate;
   }

   public void setLoanAmount(double loanAmount) {
      this.loanAmount = loanAmount;
   }

   public void setLoanAmount(Double loanAmount) {
      this.loanAmount = loanAmount;
   }

   public void setLoanTerm(double loanTerm) {
      this.loanTerm = loanTerm;
   }

   public void setLoanTerm(Double loanTerm) {
      this.loanTerm = loanTerm;
   }

   public void setMonthlyPayments(double monthlyPayments) {
      this.monthlyPayments = monthlyPayments;
   }

   public void setMonthlyPayments(Double monthlyPayments) {
      this.monthlyPayments = monthlyPayments;
   }
}
