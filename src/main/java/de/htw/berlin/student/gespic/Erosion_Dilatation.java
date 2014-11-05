package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;

/**
 * Uebung 4 - Aufgabe 1
 * <p/>
 * Created by matthias.drummer on 05.11.14.
 */
public class Erosion_Dilatation extends Abstract_ImagePlugin {

	private final static int BACKGROUND_VAL = 255;
	private final static int FOREGROUND_VAL = 0;

	public Erosion_Dilatation() {
		setExceptionHandler();
	}

	@Override
	public void runLogic() {

		ByteArrayTwoDimensionWrapper wrapper = getTwoDimensionWrapper();

		byte[] dilatatePixels = new byte[getTwoDimensionWrapper().getImageArray().length];
		byte[] erodedPixels = new byte[getTwoDimensionWrapper().getImageArray().length];

		for (int i = 0; i < wrapper.getImageHeight(); i++) {
			for (int j = 0; j < wrapper.getImageWidth(); j++) {

				int pixelOfInterest = wrapper.getEndianPixel(j, i);

				dilatatePixels[wrapper.transformCoordinate(j, i)] = (byte) pixelOfInterest;
				erodedPixels[wrapper.transformCoordinate(j, i)] = (byte) pixelOfInterest;

				if (pixelOfInterest == FOREGROUND_VAL) {

					boolean wasAlreadyEroded = false;
					for (int row = 0; row < 3; row++) {
						for (int col = 0; col < 3; col++) {
							if (row == 1 && col == 1) {
								continue;
							}
							if (ImageHelper.canAccess(j, i, col, row, wrapper.getImageWidth(), wrapper.getImageHeight())) {
								int offsetCol = j + (col - 1);
								int offsetRow = i + (row - 1);

								dilatatePixels[wrapper.transformCoordinate(offsetCol, offsetRow)] = (byte) FOREGROUND_VAL;
								if (!wasAlreadyEroded) {
									if (wrapper.getEndianPixel(offsetCol, offsetRow) == BACKGROUND_VAL) {
										erodedPixels[wrapper.transformCoordinate(j, i)] = (byte) BACKGROUND_VAL;
										wasAlreadyEroded = true;
									}
								}
							}
						}
					}
				}
			}
		}

		ImagePlus dilatatedImage = NewImage.createByteImage("Dilatated image", wrapper.getImageWidth(), wrapper.getImageHeight(), 1, NewImage.FILL_BLACK);
		dilatatedImage.getProcessor().setPixels(dilatatePixels);
		dilatatedImage.show();
		dilatatedImage.updateAndDraw();

		ImagePlus erodedImage = NewImage.createByteImage("Eroded image", wrapper.getImageWidth(), wrapper.getImageHeight(), 1, NewImage.FILL_BLACK);
		erodedImage.getProcessor().setPixels(erodedPixels);
		erodedImage.show();
		erodedImage.updateAndDraw();

		createClosedImage(dilatatePixels, wrapper);
	}

	private void createClosedImage(byte[] dilatatedPixels, ByteArrayTwoDimensionWrapper wrapper) {

		byte[] closedPixels = new byte[getTwoDimensionWrapper().getImageArray().length];

		for (int i = 0; i < wrapper.getImageHeight(); i++) {
			for (int j = 0; j < wrapper.getImageWidth(); j++) {

				int pixelOfInterest = ImageHelper.doEndian(dilatatedPixels[wrapper.transformCoordinate(j, i)]);

				closedPixels[wrapper.transformCoordinate(j, i)] = (byte) pixelOfInterest;

				if (pixelOfInterest == FOREGROUND_VAL) {

					boolean wasAlreadyEroded = false;
					for (int row = 0; row < 3; row++) {
						for (int col = 0; col < 3; col++) {
							if (row == 1 && col == 1) {
								continue;
							}
							if (ImageHelper.canAccess(j, i, col, row, wrapper.getImageWidth(), wrapper.getImageHeight())) {

								if (!wasAlreadyEroded) {
									if (ImageHelper.doEndian(dilatatedPixels[wrapper.transformCoordinate(j, i)]) == BACKGROUND_VAL) {
										closedPixels[wrapper.transformCoordinate(j, i)] = (byte) BACKGROUND_VAL;
										wasAlreadyEroded = true;
									}
								}
							}
						}
					}
				}
			}
		}

		ImagePlus closedImage = NewImage.createByteImage("Closed image", wrapper.getImageWidth(), wrapper.getImageHeight(), 1, NewImage.FILL_BLACK);
		closedImage.getProcessor().setPixels(closedPixels);
		closedImage.show();
		closedImage.updateAndDraw();
	}

	private void setExceptionHandler() {
		IJ.setExceptionHandler(new IJ.ExceptionHandler() {

			@Override
			public void handle(Throwable e) {
				IJ.log(e.getMessage());
			}
		});
	}
}
