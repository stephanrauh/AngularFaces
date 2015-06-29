package de.beyondjava.jsf.sample;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UsersBean {
	private String usersAsJson ="[{'name': 'Moroni', 'age': 50},"+
	         "{'name': 'Tiancum', 'age': 43},"+
	         "{'name': 'Jacob', 'age': 27},"+
	         "{'name': 'Nephi', 'age': 29},"+
	         "{'name': 'Enos', 'age': 34}]"; 
	public String getUsersAsJson() {
		return usersAsJson;
	}
	public void setUsersasJson(String s) {
		usersAsJson=s;
	}
	public void setUsers(String s) {
		usersAsJson=s;
	}
}
