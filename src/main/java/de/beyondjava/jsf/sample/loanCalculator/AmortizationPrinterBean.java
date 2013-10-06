/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.loanCalculator;

import java.util.ArrayList;

public class AmortizationPrinterBean {
   private ArrayList<AmortizationRow> amortizationPlan = new ArrayList<AmortizationRow>() {
      {
         add(new AmortizationRow(10000.0d, 100.0d));
         add(new AmortizationRow(9500.0d, 95.0d));
      }
   };

   private double interestRate = 5;

   private double loanAmount = 10000;

   private double loanTerm; // Dauer des Kredits

   private double monthlyPayments = 200;

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
