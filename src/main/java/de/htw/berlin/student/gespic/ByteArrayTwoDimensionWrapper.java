package de.htw.berlin.student.gespic;

/**
 * A wrapper class to access easily a one dimensional array.<br>
 * This is a better solution than to transform the one dimensional array to a two dimension and duplicating therefore the data in the ram,
 * but there is still the whole picture loaded to the ram instead of loading and processing chunks.
 * <p/>
 * Created by Matthias Drummer on 22.10.14.
 */
public class ByteArrayTwoDimensionWrapper {

    private int dimensionX;
    private int dimensionY;

    private byte[] originalArray;
    private byte[] imageArray;

    /**
     * Constructor.
     *
     * @param dimensionX the width of the image
     * @param dimensionY the height of the image
     * @param imageArray an array that contains all bytes of the image.
     */
    public ByteArrayTwoDimensionWrapper(int dimensionX, int dimensionY, byte[] imageArray, byte[] originalArray) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.imageArray = imageArray;
        this.originalArray = originalArray;
    }

    public void setPixel(int x, int y, byte pixel) {
        int arrayLocation = (y * dimensionX) + x;
        imageArray[arrayLocation] = pixel;
    }

    public void setPixel(int index, byte pixel) {
        imageArray[index] = pixel;
    }

    public byte getPixel(int x, int y) {
        int arrayLocation = (y * dimensionX) + x;
        return imageArray[arrayLocation];
    }

    public int getEndianPixel(int x, int y) {
        int arrayLocation = (y * dimensionX) + x;
        int pixel = imageArray[arrayLocation] & 0xff;
        return pixel;
    }

    public byte getOriginalPixel(int x, int y) {
        int arraLocation = (y * dimensionX) + x;
        return originalArray[arraLocation];
    }

    public int getOriginalEndianPixel(int x, int y) {
        int arrayLocation = (y * dimensionX) + x;
        int pixel = originalArray[arrayLocation] & 0xff;
        return pixel;
    }

    public byte[] getImageArray() {
        return imageArray;
    }

    public int getImageWidth() {
        return dimensionX;
    }

    public int getImageHeight() {
        return dimensionY;
    }

    public int transformCoordinate(int x, int y) {
        return (y * dimensionX) + x;
    }
}
