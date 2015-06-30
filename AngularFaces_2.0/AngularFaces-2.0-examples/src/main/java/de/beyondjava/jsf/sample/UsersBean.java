package de.beyondjava.jsf.sample;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class UsersBean {
	private List<User> users = new ArrayList<User>();
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
	public void setUsers(ArrayList<User> s) {
		users=s;
	}
	public List<User> getUsers() {
		return users;
	}
	
	public void countUsers(ActionEvent event) {
		FacesMessage message = new FacesMessage("Currently, the server side list contains " + users.size() + " users.");
		FacesContext.getCurrentInstance().addMessage(null, message );
	}
}
