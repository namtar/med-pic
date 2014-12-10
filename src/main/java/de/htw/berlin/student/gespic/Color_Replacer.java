package de.htw.berlin.student.gespic;

import ij.ImagePlus;

/**
 * Replaces everythin what is not black to white.
 *
 * @author by Matthias Drummer on 09.12.2014
 */
public class Color_Replacer extends Abstract_ImagePlugin {

//    @Override
//    public int setup(String arg, ImagePlus imp) {
//        super.setup(arg, imp);
//        return DOES_8G + NO_CHANGES;
//    }

    @Override
    public void runLogic() {

        ByteArrayTwoDimensionWrapper wrapper = getTwoDimensionWrapper();

        for (int y = 0; y < wrapper.getImageHeight(); y++) {
            for (int x = 0; x < wrapper.getImageWidth(); x++) {
                int pixel = wrapper.getEndianPixel(x, y);
                if(pixel > 180) {
                    pixel = 255; // set white
                }
                wrapper.setPixel(x, y, (byte) pixel);
            }
        }

        updateDrawAndShow();
    }

}
