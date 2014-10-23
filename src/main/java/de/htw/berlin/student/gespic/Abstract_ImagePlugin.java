package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.filter.PlugInFilter;
import ij.process.Blitter;
import ij.process.ImageProcessor;

import java.awt.*;

/**
 * Abstract class for common functions.
 * <p/>
 * Created by Matthias Drummer on 22.10.14.
 */
public abstract class Abstract_ImagePlugin implements PlugInFilter {

    private ByteArrayTwoDimensionWrapper twoDimensionWrapper;
    private Rectangle roi;
    private ImageProcessor outputImage;

    public abstract void runLogic();

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

    public void run(ImageProcessor ip) {
        //        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        // tips for developers:
        // @See: http://fiji.sc/Tips_for_developers

        // get width, height and the region of interest
        int w = ip.getWidth();
        int h = ip.getHeight();
        roi = ip.getRoi();

        //        System.out.println("Width: " + w);
        //        System.out.println("Height: " + h);

        // create a new image with the same size and copy the pixels of the original image
        ImagePlus corrected = NewImage.createByteImage("Corrected image", w, h, 1, NewImage.FILL_BLACK);
        outputImage = corrected.getProcessor();
        outputImage.copyBits(ip, 0, 0, Blitter.COPY);
        //Pixel-Array des Eingabebildes
        //        byte[] pixelsin = (byte[]) ip.getPixels();
        //Pixelarray des neuen Bildes
        twoDimensionWrapper = new ByteArrayTwoDimensionWrapper(w, h, (byte[]) outputImage.getPixels(), (byte[]) ip.getPixels());

        runLogic();
    }

    public ByteArrayTwoDimensionWrapper getTwoDimensionWrapper() {
        return twoDimensionWrapper;
    }

    public Rectangle getRoi() {
        return roi;
    }

    public void setOutputImagePixels(byte[] pixels) {
        this.outputImage.setPixels(pixels);
    }
}
