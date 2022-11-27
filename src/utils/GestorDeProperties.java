package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Clase que contiene métodos para gestionar ficheros de formato Properties
 * 
 * @author Franco Emanuel Mancinelli
 *
 */
public class GestorDeProperties {
	
	/**
	 * Lee un fichero de tipo Properties y devuelve su contenido
	 * Si no lo encuentra muestra un mensaje de error
	 * 
	 * @param fileName Nombre del fichero
	 * @return La configuración Properties
	 * @throws IOException
	 */
	public static Properties leerPropertiesFile(String fileName) throws IOException {

		Properties prop = new Properties();

		try (FileInputStream fis = new FileInputStream(fileName)) {
			prop.load(fis);
		} catch (Exception e) {
			System.err.println("No se encuentra el archivo de configuración. Se utilizarán los valores por defecto");
		}
		return prop;
	}
	
	/**
	 * Crea un fichero llamado config de formato properties con las propiedades requeridas
	 */
	public static void crearConfigProperties() {
        try (OutputStream output = new FileOutputStream("config.properties")) {

            Properties prop = new Properties();

            // Setear las propiedades del archivo
            prop.setProperty("inputfolder", "C:\\Users\\Pictures\\InputPSP");
            prop.setProperty("outputfolder", "C:\\Users\\Pictures\\OutputPSP");
            prop.setProperty("maxwidth", "250");
            prop.setProperty("maxheigth", "200");

            // Guarda las propiedades en el archivo raiz
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
	}
}


