package de.beyondjava.angularFaces.core;

public class Configuration {
	public static boolean bootsFacesActive = false;
	static {
		try {
			Class.forName("net.bootsfaces.layout.Column");
			bootsFacesActive=true;
		} catch (Exception doWithoutBootsFaces) {
			// Bootsfaces is not active
		}
	}

}
