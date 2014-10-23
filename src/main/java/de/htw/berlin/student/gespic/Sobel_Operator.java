package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.IJ.ExceptionHandler;
import ij.ImagePlus;
import ij.gui.NewImage;

/**
 * Class that combines four gray pixel to one gray value.
 *
 * @author by Matthias Drummer on 19.10.2014
 */
public class Sobel_Operator extends Abstract_ImagePlugin {

    @Override
    public void runLogic() {

        ByteArrayTwoDimensionWrapper wrapper = getTwoDimensionWrapper();

        byte[][] sobelRows = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
        byte[][] sobelCols = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};

        byte[] rowImagePixels = new byte[getTwoDimensionWrapper().getImageArray().length];
        byte[] colImagePixels = new byte[getTwoDimensionWrapper().getImageArray().length];

        IJ.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handle(Throwable e) {
                IJ.log(e.getMessage());
            }
        });

        for (int i = 0; i < wrapper.getImageHeight(); i++) {
            for (int j = 0; j < wrapper.getImageWidth(); j++) {

                int newRowValue = 0;
                int newColValue = 0;

                // rows
                // this is only ok if solelRows and cols have the same dimension
                for (int row = 0; row < sobelRows.length; row++) {
                    for (int col = 0; col < sobelRows[row].length; col++) {
                        if (canAccess(j, i, col, row, wrapper.getImageWidth(), wrapper.getImageHeight())) {
                            newRowValue += wrapper.getEndianPixel((j + (col - 1)), (i + (row - 1))) * sobelRows[row][col];
                            newColValue += wrapper.getEndianPixel((j + (col - 1)), (i + (row - 1))) * sobelCols[row][col];
                        }
                    }
                }

                newRowValue = normalize(newRowValue);
                newColValue = normalize(newColValue);


                // set new pixel
                rowImagePixels[wrapper.transformCoordinate(j, i)] = (byte) newRowValue;
                colImagePixels[wrapper.transformCoordinate(j, i)] = (byte) newColValue;
            }
        }

//        ImagePlus newRowImage = NewImage.createByteImage("Row image", wrapper.getImageWidth(), wrapper.getImageHeight(), 1, NewImage.FILL_BLACK);
//        newRowImage.getProcessor().setPixels(rowImagePixels);
//        newRowImage.show();
//        newRowImage.updateAndDraw();
//
//        ImagePlus newColImage = NewImage.createByteImage("Col image", wrapper.getImageWidth(), wrapper.getImageHeight(), 1, NewImage.FILL_BLACK);
//        newColImage.getProcessor().setPixels(colImagePixels);
//        newColImage.show();
//        newColImage.updateAndDraw();

        createCombinedImage(rowImagePixels, colImagePixels, wrapper.getImageWidth(), wrapper.getImageHeight());
        IJ.log("Image created.");
    }

//    private boolean canAccess(int accessX, int accessY, int imageWidth, int imageHeight) {
//
//        if (accessX < 0 || accessY < 0 || accessX > imageWidth - 1 || accessY > imageHeight - 1) {
//
//            return false;
//        }
//        return true;
//    }

    private boolean canAccess(int choordX, int choordY, int matrixX, int matrixY, int imageWidth, int imageHeight) {

        // translate matrixX
        int matrixXTrans = matrixX - 1;
        int matrixYTrans = matrixY - 1;

        // translate matrixY
        if ((choordX + matrixXTrans) < 0 || (choordX + matrixXTrans) > (imageWidth - 1)) {
            return false;
        }
        if ((choordY + matrixYTrans) < 0 || (choordY + matrixYTrans) > (imageHeight - 1)) {
            return false;
        }

        return true;
    }

    private int normalize(int value) {
        if (value > 255) {
            value = 255;
        } else if (value < 0) {
            value = 0;
        }
        return value;
    }

    private void createCombinedImage(byte[] rowImagePixels, byte[] colImagePixels, int imageWidth, int imageHeight) {

        if (rowImagePixels.length != colImagePixels.length) {
            throw new IllegalArgumentException("Both pixel arrays must have the same lenght.");
        }

        ImagePlus image = NewImage.createByteImage("CombinedImage", imageWidth, imageHeight, 1, NewImage.FILL_BLACK);
        byte[] pixels = (byte[]) image.getProcessor().getPixels();

        for (int i = 0; i < rowImagePixels.length; i++) {
            int val1 = rowImagePixels[i] & 0xff;
            int val2 = colImagePixels[i] & 0xff;

            int result = (int) Math.sqrt((Math.pow((double) val1, 2d) + Math.pow(((double) val2), 2d)));
            result = normalize(result);
            pixels[i] = (byte) result;
        }

        image.show();
        image.updateAndDraw();
    }
}
