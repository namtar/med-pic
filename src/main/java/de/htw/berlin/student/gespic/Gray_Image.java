package de.htw.berlin.student.gespic;

import de.htw.berlin.student.gespic.ByteArrayTwoDimensionWrapper;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.filter.PlugInFilter;
import ij.process.Blitter;
import ij.process.ImageProcessor;

import java.awt.*;

/**
 * Graubildtest
 * <p/>
 * Klasse zum Testen von BV-Algorithmen in Graubildern
 *
 * @author Kai Saeger
 * @author Matthias Drummer
 * @version 1.0
 */
public class Gray_Image implements PlugInFilter {

	/**
	 * Initialisierung in ImageJ
	 */
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}
		//Zugelassen nur f¸r 8-Bit Graubilder
		return DOES_8G + NO_CHANGES;
	}

	/**
	 * About Message zu diesem Plug-In.
	 */
	void showAbout() {
		IJ.showMessage("Graubildtest", "Testprogramm");
	}

	/**
	 * Ausf¸hrende Funktion
	 *
	 * @param ip Image Processor. Klasse in ImageJ, beinhaltet das Bild und
	 *           zugehˆrige Metadaten.
	 */
	public void run(ImageProcessor ip) {

		//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

		// get width, height and the region of interest
		int w = ip.getWidth();
		int h = ip.getHeight();
		Rectangle roi = ip.getRoi();

		// create a new image with the same size and copy the pixels of the original image
		ImagePlus corrected = NewImage.createByteImage("Corrected image", w, h, 1, NewImage.FILL_BLACK);
		ImageProcessor cor_ip = corrected.getProcessor();
		cor_ip.copyBits(ip, 0, 0, Blitter.COPY);
		//Pixel-Array des Eingabebildes
		//        byte[] pixelsin = (byte[]) ip.getPixels();
		//Pixelarray des neuen Bildes
		ByteArrayTwoDimensionWrapper twoDimensionWrapper = new ByteArrayTwoDimensionWrapper(w, h, (byte[]) cor_ip.getPixels(), (byte[]) ip.getPixels());
		/***********An dieser Stelle kann an den einzelnen Pixeln gearbeitet werden.*********/

		// create a two dimensional array matching to the image

		// set System.out back to default.
		//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		//        System.out.println("Roi: X: " + roi.getX() + ", Y: " + roi.getY() + ", Width: " + roi.getWidth() + ", Height: " + roi.getHeight());

		for (int i = (int) roi.getY(); i < (int) (roi.getY() + roi.getHeight()); i++) {
			for (int j = (int) roi.getX(); j < (int) (roi.getX() + roi.getWidth()); j++) {
				//                twoDimensionWrapper.setPixel(j, i, (byte) (255 - twoDimensionWrapper.getPixel(j, i)));
				twoDimensionWrapper.setPixel(j, i, transformGreyValuesToFour(twoDimensionWrapper.getOriginalPixel(j, i)));
			}
		}
		cor_ip.setPixels(twoDimensionWrapper.getImageArray());

		/*****************Ende**********************************************************/

		corrected.show();
		corrected.updateAndDraw();
	}

	private byte transformGreyValuesToFour(byte value) {

		int pixel = value & 0xff;

		int greyIntervall = 255 / 3;
		if (pixel < greyIntervall / 2) {
			return 0;
		} else if (pixel < greyIntervall) {
			return (byte) greyIntervall;
		} else if (pixel < greyIntervall + (greyIntervall / 2)) {
			return (byte) greyIntervall;
		} else if (pixel < greyIntervall * 2) {
			return (byte) (greyIntervall * 2);
		} else if (pixel < greyIntervall * 2 + greyIntervall / 2) {
			return (byte) (greyIntervall * 2);
		} else {
			return (byte) (greyIntervall * 3);
		}
	}
}

