package threads;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SlaveThread extends Thread{
	
	String imageInserted;
	String pathInputFile;
	String outputFolder;
	int maxWidth;
	int maxHeigth;
	
	int newWidth;
	int newHeigth;


	public SlaveThread(String imageInserted, String pathInputFile, String outputFolder, int maxWidth, int maxHeigth) {
		super();
		this.imageInserted = imageInserted;
		this.pathInputFile = pathInputFile;
		this.outputFolder = outputFolder;
		this.maxWidth = maxWidth;
		this.maxHeigth = maxHeigth;
	}


	@Override
	public void run() {
		
		int actualImgWidth = 0;
		int actualImgHeigth = 0;
		
		//Comprobamos que el formato de imagen sea JPG
		if(imageInserted.endsWith(".jpg")) {
						
			//Auditoría
			System.out.println("Formato valido");
			System.out.println(pathInputFile);
			

			//Obtengo el tamaño de la imagen original
			BufferedImage bimg = null;
			try {
				bimg = ImageIO.read(new File(pathInputFile));
				actualImgWidth = bimg.getWidth();
				actualImgHeigth = bimg.getHeight();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Compruebo si las dimensiones de la imagen son mayores a los máximos pasados por parametro
			//De ser así, se ajustarán de manera proporcional
			if(maxWidth < actualImgWidth || maxHeigth < actualImgHeigth) {
				int divisorAncho = actualImgWidth / maxWidth;
				int divisorAltura = actualImgHeigth / maxHeigth;
				
				int denominadorComun = (divisorAltura < divisorAncho) ? divisorAncho : divisorAltura;
				
				newWidth = actualImgWidth / denominadorComun;
				newHeigth = actualImgHeigth / denominadorComun;
				
				//Auditoria
				System.out.println(newWidth+" "+newHeigth);
				
				//Reajusto el tamaño
				bimg = resizeImage(bimg,newWidth, newHeigth);
				
			}
			
			//Traslado la imagen a la carpeta de salida, con el mismo nombre
			String fileOutputPath = outputFolder + "\\" + imageInserted;
			
			try {
			    File outputfile = new File(fileOutputPath);
				ImageIO.write(bimg, "jpg", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			//Si el formato de imagen no es JPG...
		} else {
			System.out.println("Formato invalido");
		}
		
		
		// Borramos del input sea lo que sea
		System.out.println("Path to delete: "+pathInputFile);
		File fileToDelete = new File(pathInputFile);
		fileToDelete.delete();
		
	}
	
	/**
	 * Método que re-ajusta el tamaño de una imagen a las medidas especificadas
	 * @param originalImage La imagen original
	 * @param targetWidth El nuevo ancho de la imagen
	 * @param targetHeight El nuevo alto de la imagen
	 * @return La imagen re-ajustada a los tamaños especificados
	 */
	public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
}
