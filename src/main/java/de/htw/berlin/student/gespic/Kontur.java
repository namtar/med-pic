package de.htw.berlin.student.gespic;

import ij.*;
import ij.gui.*;
import java.awt.*;
import java.util.Vector;  
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

/** Graubildtest
  *
  * Klasse zum Testen von BV-Algorithmen in Graubildern
  *
  * @author Kai Saeger
  * @version 1.0
  */
public class Kontur implements PlugInFilter {

	private Vector polyvec;
	/**Initialisierung in ImageJ */
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about"))
			{showAbout(); return DONE;}
				//Zugelassen nur f�r 8-Bit Graubilder
                return DOES_8G+NO_CHANGES;
	}	
	/**About Message zu diesem Plug-In. */
	void showAbout() {
		IJ.showMessage("Graubildtest",
			"Testprogramm"
		);
	}

	/**Ausf�hrende Funktion
	 * @param ip Image Processor. Klasse in ImageJ, beinhaltet das Bild und 
	 * zugeh�rige Metadaten.
	 * 	 */
	public void run(ImageProcessor ip) {
		
		// get width, height and the region of interest
		int w = ip.getWidth();     
		int h = ip.getHeight();    
		Rectangle roi = ip.getRoi();

		// create a new image with the same size and copy the pixels of the original image
		ImagePlus corrected = NewImage.createByteImage ("corrected image", w, h, 1, NewImage.FILL_BLACK);
		ImageProcessor cor_ip = corrected.getProcessor();
		//cor_ip.copyBits(ip,0,0,Blitter.COPY);
		//Pixel-Array des Eingabebildes
		byte[] pixelsin = (byte[])ip.getPixels();
		//Pixelarray des neuen Bildes
		byte[] pixels = (byte[])cor_ip.getPixels();
		/***********An dieser Stelle kann an den einzelnen Pixeln gearbeitet werden.*********/
			
		contour(pixelsin, pixels, roi, w, h);
		
		/*****************Ende**********************************************************/
		
		corrected.show();
		corrected.updateAndDraw();
		
		
	}
	private void contour(byte[] pixelsin, byte[]pixels, Rectangle roi, int w, int h){
		polyvec = new Vector();
		
		//irgendwann wird hier createObject gestartet !
	}
	
		/**createObject renders a new contour line with the transferred threshold.
	 * The calculation is based on the current mouse coordinates.
	 * @param thres The threshold for the rendering of the new contour line.	 */
	 private void createObject(byte[]pixelsin, byte[]pixels, int x, int y, int w)	
	{    
	
		 Polygon polygon = new Polygon();
		
		//Hier Konturverfolgung
		
		//bei gefundenen Konturpunten: polygon.addPoint(x,y); 
		
		//Nach der Konturverfolgung: this.polyvec.addElement(polygon);
	}
	 
	 /**Checks, if a point is inside one of the Objects.
	 * @param x The x image coordinate of the point to be examined.
	 * @param y the y image coordinate of the point to be examined.
	 * @return inside True, if the point lies inside an object, false otherwise.	 */
	public boolean checkcontains(int x, int y) //checks all Polygons
	{
		for (int i = 0; i < this.polyvec.size(); i++)
		{
			Polygon poly = (Polygon) this.polyvec.elementAt(i);
				if(poly.contains(x,y)) return true;
				
			for (int j=0; j<poly.npoints; j++) {
				if(x == poly.xpoints[j] && y == poly.ypoints[j]) return true;
			}
		}
		return false;
	}

}


