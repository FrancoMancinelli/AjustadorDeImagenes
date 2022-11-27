package main;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;

import threads.SlaveThread;

public class Dispatcher {

	static String defaultInputFolder = "C:\\Users\\franc\\OneDrive\\Documentos\\DAM2\\PSP\\Practica2Hilos";
	static String defaultOutputFolder = "C:\\Users\\franc\\OneDrive\\Documentos\\DAM2\\PSP\\Practica2Hilos\\output";
	static String defaultMaxWidth = "100";
	static String defaultMaxHeigth = "100";

	public static void main(String[] args) throws IOException, InterruptedException {

		//Intento de lectura del archivo de configuración properties
		Properties prop = readPropertiesFile("config.properties");

		//Si es posible obtener las claves requeridas del fichero de configuración
		//se utilizará esos valores y remplazará a los valores por defecto
			defaultInputFolder = prop.getProperty("inputfolder", defaultInputFolder);
			defaultOutputFolder = prop.getProperty("outputfolder", defaultOutputFolder);
			defaultMaxWidth = prop.getProperty("maxwidth", defaultMaxWidth);
			defaultMaxHeigth = prop.getProperty("maxheigth", defaultMaxHeigth);


		// Auditoría
		System.out.println(defaultInputFolder + " " + defaultOutputFolder + " " + defaultMaxWidth + " " + defaultMaxHeigth);
		
		
	

		//Se crea un WatchService para monitorear la actividad dentro de la carpeta indicada
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path path = Paths.get(defaultInputFolder);
		path.register(watcher, ENTRY_CREATE, ENTRY_DELETE);

		WatchKey key;
		while ((key = watcher.take()) != null) {
			Thread.sleep(500);

			for (WatchEvent<?> event : key.pollEvents()) {

				// Auditoría
				System.out.println("Event type: " + event.kind() + "File affected: " + event.context());

				//Se comprueba si el tipo de evento realizado sobre la carpeta es una insersión
				if (event.kind().toString().equals("ENTRY_CREATE")) {
					
					System.out.println("Se ha añadido algo a la carpeta");
					
					String imageName = event.context().toString();
					String pathInputFolder = defaultInputFolder + "\\" + event.context().toString();
					int maxWidth = Integer.parseInt(defaultMaxWidth);
					int maxHeigth = Integer.parseInt(defaultMaxHeigth);
					
					//Se crea un nuevo Hilo Esclavo que realizará la conversión
					new SlaveThread(imageName, pathInputFolder, defaultOutputFolder, maxWidth, maxHeigth).run();
					
					//Dormimos al Hilo para que no pete
				}

			}
			key.reset();
		}

	}

	/**
	 * Lee un fichero de tipo Properties y devuelve su contenido
	 * @param fileName Nombre del fichero
	 * @return La configuración Properties
	 * @throws IOException
	 */
	public static Properties readPropertiesFile(String fileName) throws IOException {

		Properties prop = new Properties();

		try (FileInputStream fis = new FileInputStream(fileName)) {
			prop.load(fis);
		} catch (Exception e) {
			System.err.println("No se encuentra el archivo de configuración. Se utilizarán los valores por defecto");
		}
		return prop;
	}
}
