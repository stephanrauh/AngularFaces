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

import java.util.*;

public class AmortizationPrinterBean {
   private List<AmortizationRow> amortizationPlan = new ArrayList<AmortizationRow>();

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
