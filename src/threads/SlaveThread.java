package threads;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Clase Esclava que extiende de Thread. Se encarga de comprobar el formato de
 * una imagen. De ser JPG trasladará la imagen de una carpeta de entrada, a una
 * de salida. A su vez, si es necesario, se redimensionará la imagen para que
 * cumpla con las medidas máximas de alto y ancho. Por último eliminará de la
 * carpeta de entrada la imagen sea JPG o no.
 * 
 * @author Franco Emanuel Mancinelli
 *
 */
public class SlaveThread extends Thread {

	/** Propiedades */
	String imageInserted;
	String pathInputFile;
	String outputFolder;
	int maxWidth;
	int maxHeigth;

	int actualImgWidth = 0;
	int actualImgHeigth = 0;

	int newWidth;
	int newHeigth;

	/** Constructor */
	public SlaveThread(String imageInserted, String pathInputFile, String outputFolder, int maxWidth, int maxHeigth) {
		super();
		this.imageInserted = imageInserted;
		this.pathInputFile = pathInputFile;
		this.outputFolder = outputFolder;
		this.maxWidth = maxWidth;
		this.maxHeigth = maxHeigth;
	}

	/**
	 * Inicializa el Thread
	 */
	@Override
	public void run() {

		// Comprobamos que el formato de imagen sea JPG
		if (imageInserted.endsWith(".jpg")) {
			trasladoDeImagen();
		}

		// Borramos del input sea lo que sea
		File fileToDelete = new File(pathInputFile);
		fileToDelete.delete();
	}

	/**
	 * Leemos la imagen, obtenemos su tamaño original y de ser necesario se
	 * redimensionará para cumplir con las medidas máximas. Posteriormente realizará
	 * el traslado de la imagen a la carpeta de salida
	 */
	private void trasladoDeImagen() {
		// Leemos la imagen con la que trabajar
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(pathInputFile));

			// Obtengo el tamaño de la imagen original
			actualImgWidth = bimg.getWidth();
			actualImgHeigth = bimg.getHeight();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Redimension de la imagen solo si es necesario
		bimg = validateSizeImage(bimg);

		// Traslado la imagen a la carpeta de salida, con el mismo nombre
		String fileOutputPath = outputFolder + "\\" + imageInserted;

		try {
			File outputfile = new File(fileOutputPath);
			ImageIO.write(bimg, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Comprueba si la imagen original cumple con las dimensiones máximas. De no
	 * hacerlo, reajustará la imagen y la devolverá
	 * 
	 * @param bimg La imagen a comprobar y redimensionar
	 * @return La imagen con un tamaño permitido
	 */
	private BufferedImage validateSizeImage(BufferedImage bimg) {
		// Compruebo si las dimensiones de la imagen son mayores a las dimensiones
		// máximas
		if (maxWidth < actualImgWidth || maxHeigth < actualImgHeigth) {

			int divisorAncho = actualImgWidth / maxWidth;
			int divisorAltura = actualImgHeigth / maxHeigth;

			int denominadorComun = (divisorAltura < divisorAncho) ? divisorAncho : divisorAltura;

			newWidth = actualImgWidth / denominadorComun;
			newHeigth = actualImgHeigth / denominadorComun;

			// Reajusto el tamaño
			bimg = resizeImage(bimg, newWidth, newHeigth);

		}
		return bimg;
	}

	/**
	 * Método que re-ajusta el tamaño de una imagen a las medidas especificadas
	 * 
	 * @param originalImage La imagen original
	 * @param targetWidth   El nuevo ancho de la imagen
	 * @param targetHeight  El nuevo alto de la imagen
	 * @return La imagen re-ajustada a los tamaños especificados
	 */
	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;
	}
}
