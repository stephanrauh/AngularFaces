package de.beyondjava.jsf.sample.ngTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.beyondjava.angularFaces.components.puiSync.JSONUtilities;

@ManagedBean
@SessionScoped
public class UsersBean {
	private List<User> users = new ArrayList<User>();
	
	public UsersBean() {
		users.add(new User("Moroni", 50));
		users.add(new User("Tiancum", 43));
		users.add(new User("Jacob", 27));
		users.add(new User("Nephi", 29));
		users.add(new User("Enos", 34));
	}

	public String getUsersAsJson() {
		String usersAsJson = JSONUtilities.writeObjectToJSONString(users);
		usersAsJson=usersAsJson.replace('"', '\'');
		return usersAsJson;
	}
	public void setUsers(ArrayList<?> s) {
		if (s == null) {
			users=null;
		} else {
			ArrayList<User> result = new ArrayList<User>(s.size());
			for (Object o:s) {
				if (o instanceof User) result.add((User) o);
				else if (o instanceof Map) {
					String name = (String) ((Map) o).get("name");
					int age;
					Object ageAsObject= ((Map) o).get("age");
					if (ageAsObject instanceof String) {
						age=Integer.valueOf((String)ageAsObject);
					} else age = (Integer) ageAsObject;
					User user = new User(name, age);
					result.add(user);
				}
			}
			users=result;
		}
	}
	public List<User> getUsers() {
		return users;
	}
	
	public void save(ActionEvent event) {
		String u = "";
		for (User user:users) {
			u += " ," + user.getName() + "(" + user.getAge() + ")";
		}
		if (u.length()>0) u = u.substring(2);
		FacesMessage message = new FacesMessage("Currently, the server side list contains " + users.size() + " users:" + u);
		FacesContext.getCurrentInstance().addMessage(null, message );
	}
	
	public void sortAscendingName(ActionEvent even) {
		FacesMessage message = new FacesMessage("Sorting the list ascending by name. The sort algorithm is implemented on the server side.");
		FacesContext.getCurrentInstance().addMessage(null, message );
		Comparator<? super User> ascending = new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};
		Collections.sort(users, ascending);
	}
	public void sortDescendingName(ActionEvent even) {
		FacesMessage message = new FacesMessage("Sorting the list descending by name. The sort algorithm is implemented on the server side.");
		FacesContext.getCurrentInstance().addMessage(null, message );
		Comparator<? super User> descending = new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return -o1.getName().compareTo(o2.getName());
			}
		};
		Collections.sort(users, descending);
	}
	public void sortAscendingAge(ActionEvent even) {
		FacesMessage message = new FacesMessage("Sorting the list ascending by age. The sort algorithm is implemented on the server side.");
		FacesContext.getCurrentInstance().addMessage(null, message );
		Comparator<? super User> ascending = new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getAge()-o2.getAge();
			}
		};
		Collections.sort(users, ascending);
	}
	public void sortDescendingAge(ActionEvent even) {
		FacesMessage message = new FacesMessage("Sorting the list descending by age. The sort algorithm is implemented on the server side.");
		FacesContext.getCurrentInstance().addMessage(null, message );
		Comparator<? super User> descending = new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o2.getAge()-o1.getAge();
			}
		};
		Collections.sort(users, descending);
	}
}
