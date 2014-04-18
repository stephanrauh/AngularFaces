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
package de.beyondjava.jsf.sample.secure;

import javax.faces.bean.*;
import javax.validation.constraints.Size;

@ManagedBean
@RequestScoped
public class SecurityDemoBean {
   @Size(max = 30)
   private String firstName = "John";
   @Size(max = 30)
   private String lastName = "Doe";

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String showErrors() {
      return "ok";
   }
}
