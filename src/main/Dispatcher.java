package main;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;

import threads.SlaveThread;
import utils.GestorDeProperties;

/**
 * Clase principal
 * 
 * @author Franco Emanuel Mancinelli
 *
 */
public class Dispatcher {

	static String defaultInputFolder = "..\\Practica2Hilos";
	static String defaultOutputFolder = "..\\Practica2Hilos\\output";
	static String defaultMaxWidth = "100";
	static String defaultMaxHeigth = "100";

	public static void main(String[] args) throws IOException, InterruptedException {

		// Intento de lectura del archivo de configuración properties
		Properties prop = GestorDeProperties.leerPropertiesFile("cosnfig.properties");

		// Si es posible obtener las claves requeridas del fichero de configuración
		// se utilizará esos valores y remplazará a los valores por defecto
		defaultInputFolder = prop.getProperty("inputfolder", defaultInputFolder);
		defaultOutputFolder = prop.getProperty("outputfolder", defaultOutputFolder);
		defaultMaxWidth = prop.getProperty("maxwidth", defaultMaxWidth);
		defaultMaxHeigth = prop.getProperty("maxheigth", defaultMaxHeigth);

		// Se crea un WatchService para monitorear la actividad dentro de la carpeta
		// indicada
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path path = Paths.get(defaultInputFolder);
		path.register(watcher, ENTRY_CREATE, ENTRY_DELETE);

		WatchKey key;
		while ((key = watcher.take()) != null) {
			Thread.sleep(500);

			for (WatchEvent<?> event : key.pollEvents()) {

				// Se comprueba si el tipo de evento realizado sobre la carpeta es una insersión
				if (event.kind().toString().equals("ENTRY_CREATE")) {

					String imageName = event.context().toString();
					String pathInputFolder = defaultInputFolder + "\\" + event.context().toString();
					int maxWidth = Integer.parseInt(defaultMaxWidth);
					int maxHeigth = Integer.parseInt(defaultMaxHeigth);

					// Se crea un nuevo Hilo Esclavo que realizará la conversión
					new SlaveThread(imageName, pathInputFolder, defaultOutputFolder, maxWidth, maxHeigth).run();

				}

			}
			key.reset();
		}

	}

}
