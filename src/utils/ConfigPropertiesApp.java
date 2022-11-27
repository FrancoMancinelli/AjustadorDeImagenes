package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigPropertiesApp {
	
	 public static void main(String[] args) {

	        try (OutputStream output = new FileOutputStream("config.properties")) {

	            Properties prop = new Properties();

	            // set the properties value
//	            prop.setProperty("inputfolder", "C:\\Users\\franc\\OneDrive\\Documentos\\DAM2\\PSP\\Practica2Hilos\\myInput");
//	            prop.setProperty("outputfolder", "C:\\Users\\franc\\OneDrive\\Documentos\\DAM2\\PSP\\Practica2Hilos\\myOutput");
	            prop.setProperty("maxwidth", "100");
//	            prop.setProperty("maxheigth", "200");

	            // save properties to project root folder
	            prop.store(output, null);

	            System.out.println(prop);

	        } catch (IOException io) {
	            io.printStackTrace();
	        }

	    }

}
