package de.beyondjava.jsf.sample.loanCalculator;

/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class AmortizationRow {
   private double balance; // Restschuld am Periodenende
   private double interestPaid;
   private int month;
   private double monthlyPayment;
   private double principalPaid; // Tilgung

   public AmortizationRow() {
   }

   public AmortizationRow(double balance, double interestPaid) {
      this.balance = balance;
      this.interestPaid = interestPaid;
   }

   public double getBalance() {
      return balance;
   }

   public double getInterestPaid() {
      return interestPaid;
   }

   public int getMonth() {
      return month;
   }

   public double getMonthlyPayment() {
      return monthlyPayment;
   }

   public double getPrincipalPaid() {
      return principalPaid;
   }

   public void setBalance(double balance) {
      this.balance = balance;
   }

   public void setInterestPaid(double interestPaid) {
      this.interestPaid = interestPaid;
   }

   public void setMonth(int month) {
      this.month = month;
   }

   public void setMonthlyPayment(double monthlyPayment) {
      this.monthlyPayment = monthlyPayment;
   }

   public void setPrincipalPaid(double principalPaid) {
      this.principalPaid = principalPaid;
   }
}
