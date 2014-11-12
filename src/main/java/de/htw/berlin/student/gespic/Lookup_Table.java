package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

import java.awt.image.IndexColorModel;

/**
 * Lookup Table Manipulation.
 * <p/>
 * Created by matthias.drummer on 12.11.14.
 */
public class Lookup_Table extends Abstract_ImagePlugin {

	@Override
	public void runLogic() {

		byte[] colorMapRed = new byte[256];
		byte[] colorMapGreen = new byte[256];
		byte[] colorMapBlue = new byte[256];

		for (int i = 0; i < 256; i++) {
			colorMapRed[i] = (byte) ImageHelper.normalize(-2 * i + 256);
			colorMapGreen[i] = (byte) ImageHelper.normalize(2 * i - 256);
			colorMapBlue[i] = (byte) ImageHelper.normalize((i < 128 ? 4 * i - 256 : -4 * i + 256));
		}

		IndexColorModel cm = new IndexColorModel(8, 256, colorMapRed, colorMapGreen, colorMapBlue);
		getOutputImage().setColorModel(cm);

		updateDrawAndShow();
	}

	public static void main(String[] args) {

		new ImageJ();

		//		https://github.com/imagej/minimal-ij1-plugin/blob/master/src/main/java/Process_Pixels.java
		// open the Clown sample
		ImagePlus image = IJ.openImage("/Users/matthias.drummer/Documents/studium/medBildverarbeitung/Testbilder-UE/ratio.tif");
		image.show();

		IJ.runPlugIn(Lookup_Table.class.getName(), "");
	}
}
