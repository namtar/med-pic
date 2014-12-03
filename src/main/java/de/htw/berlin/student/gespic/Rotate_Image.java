package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.io.Opener;

import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Plugin that rotates an image.
 * <p/>
 * Created by matthias.drummer on 03.12.14.
 */
public class Rotate_Image extends Abstract_ImagePlugin {

	@Override
	public void runLogic() {

		// [xrot] [cos alpha sin alpha] [x]
		// [yrot] [-sin alpha cos alpha] [y]
		ByteArrayTwoDimensionWrapper wrapper = getTwoDimensionWrapper();

		double rotationAngle = Math.toRadians(30);
		//		AffineTransform transform = new AffineTransform();
		//		transform.rotate(rotationAngle, wrapper.getImageWidth() / 2, wrapper.getImageHeight() / 2);

		//		http://www.java-forum.org/awt-swing-javafx-and-swt/145239-punkte-um-mittelpunkt-drehen.html

		int middleX = wrapper.getImageWidth() / 2;
		int middleY = wrapper.getImageHeight() / 2;

		byte[] outputPixels = new byte[wrapper.getImageArray().length];

		for (int i = 0; i < wrapper.getImageHeight(); i++) {
			for (int j = 0; j < wrapper.getImageWidth(); j++) {

				//				int polarX = middleX - j;
				//				int polarY = middleY - i;
				//				double theta = Math.atan2(polarY, polarX);
				//				double length = Math.sqrt(polarX * polarX + polarY * polarY);
				//
				//				// rotate
				//				theta += rotationAngle;
				//
				//				// back to cartesian coordinates
				//				int newX = (int) (length * Math.cos(theta));
				//				int newY = (int) (length * Math.sin(theta));
				//				newX += middleX;
				//				newY += middleY;
				//
				//				if (newX < 0 || newX > wrapper.getImageWidth() - 1 || newY < 0 || newY > wrapper.getImageHeight() - 1) {
				//					Logger.getLogger(getClass().getName()).info("Cordinate not processed: (x,y) " + newX + ", " + newY);
				//				} else {
				//					int pixel = wrapper.getEndianPixel(j, i);
				//					outputPixels[wrapper.transformCoordinate(newX, newY)] = (byte) pixel;
				//				}

				//				int xRot = (int) (Math.cos(-rotationAngle) * (j - middleX) + Math.sin(-rotationAngle) * (i - middleY));
				//				int yRot = (int) (Math.sin(-rotationAngle) * (-1) * (j - middleX) + Math.cos(-rotationAngle) * (i - middleY));
				//				xRot += middleX;
				//				yRot += middleY;
				//
				//				if (xRot < 0 || xRot > wrapper.getImageWidth() - 1 || yRot < 0 || yRot > wrapper.getImageHeight() - 1) {
				//					Logger.getLogger(getClass().getName()).info("Cordinate not processed: (x,y) " + xRot + ", " + yRot);
				//				} else {
				//					int pixel = wrapper.getEndianPixel(j, i);
				//					outputPixels[wrapper.transformCoordinate(xRot, yRot)] = (byte) pixel;
				//				}

				int xRot = (int) (Math.cos(rotationAngle) / (j - middleX) + Math.sin(rotationAngle) / (i - middleY));
				int yRot = (int) (Math.sin(rotationAngle) * (-1) / (j - middleX) + Math.cos(rotationAngle) / (i - middleY));
				xRot += middleX;
				yRot += middleY;

				if (xRot < 0 || xRot > wrapper.getImageWidth() - 1 || yRot < 0 || yRot > wrapper.getImageHeight() - 1) {
					Logger.getLogger(getClass().getName()).info("Cordinate not processed: (x,y) " + xRot + ", " + yRot);
				} else {
					int pixel = wrapper.getEndianPixel(yRot, xRot);
					outputPixels[wrapper.transformCoordinate(j, i)] = (byte) pixel;
				}
			}
		}

		ImagePlus dilatatedImage = NewImage.createByteImage("Rotated image", wrapper.getImageWidth(), wrapper.getImageHeight(), 1, NewImage.FILL_BLACK);
		dilatatedImage.getProcessor().setPixels(outputPixels);
		dilatatedImage.show();
		dilatatedImage.updateAndDraw();
	}

	public static void main(String[] args) {

		new ImageJ();

		//		https://github.com/imagej/minimal-ij1-plugin/blob/master/src/main/java/Process_Pixels.java
		InputStream is = Rotate_Image.class.getClassLoader().getResourceAsStream("pics/fisch1.tif");
		// @See: http://imagej.net/pipermail/imagej-devel/2013-January/001374.html
		if (is != null) {
			Opener opener = new Opener();
			ImagePlus image = opener.openTiff(is, "fish1.tif");
			image.show();
		}

		IJ.runPlugIn(Rotate_Image.class.getName(), "");
	}

}
