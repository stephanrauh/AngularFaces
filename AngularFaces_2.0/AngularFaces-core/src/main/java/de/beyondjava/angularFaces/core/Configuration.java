package de.beyondjava.angularFaces.core;

public class Configuration {
    public static boolean bootsFacesActive = false;
    public static boolean myFaces = false;

    static {
        try {
            Class.forName("net.bootsfaces.component.column.Column");
            bootsFacesActive = true;
        } catch (Exception doWithoutBootsFaces) {
            // Bootsfaces is not active
        }
        StackTraceElement[] stackTrace = new NullPointerException().getStackTrace();
        for (StackTraceElement line : stackTrace) {
            if (line.getClassName().contains("org.apache.myfaces")) {
                myFaces = true;
                break;
            }
            if (line.getClassName().contains("com.sun.faces")) {
                myFaces = false;
                break;
            }
        }

    }

}
