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
package de.beyondjava.jsf.sample.angularButton;

import java.io.Serializable;

import javax.faces.bean.*;
import javax.validation.constraints.*;

@ManagedBean
@RequestScoped
public class UserBean implements Serializable {

   @NotNull
   @Size(min = 3, max = 20)
   String firstName;

   @NotNull
   @Size(min = 1, max = 20)
   String lastName;

   /**
    * @return the firstName
    */
   public String getFirstName() {
      return this.firstName;
   }

   /**
    * @return the lastName
    */
   public String getLastName() {
      return this.lastName;
   }

   /**
    * @param firstName
    *           the firstName to set
    */
   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   /**
    * @param lastName
    *           the lastName to set
    */
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }
}
