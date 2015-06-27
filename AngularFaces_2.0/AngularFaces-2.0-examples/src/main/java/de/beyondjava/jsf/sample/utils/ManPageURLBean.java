package de.beyondjava.jsf.sample.utils;

import java.io.File;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean
public class ManPageURLBean {
	public String getManPageURL() {
		if (File.separatorChar == '/') {
			return "/";
		} else
			return "/angularfaces-2.1.7-documentation/";
	}
}
