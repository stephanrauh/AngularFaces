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
package de.beyondjava.jsf.sample.loanCalculator;

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
