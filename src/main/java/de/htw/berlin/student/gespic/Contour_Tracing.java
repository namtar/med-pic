package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.io.Opener;

import javax.swing.text.View;
import java.awt.*;
import java.io.InputStream;
import java.util.Vector;

/**
 * Contour Tracing Plugin.
 * <p/>
 * Created by matthias.drummer on 26.11.14.
 */
public class Contour_Tracing extends Abstract_ImagePlugin {

    private Vector polyvec;

    private final static Integer BACKGROUND = 255;
    private final static Integer FOREGROUND = 0;

    @Override
    public void runLogic() {

        ByteArrayTwoDimensionWrapper wrapper = getTwoDimensionWrapper();
        polyvec = new Vector();

        // blank out all target pixels....
        for (int i = 0; i < wrapper.getImageArray().length; i++) {
            wrapper.setPixel(i, BACKGROUND.byteValue());
        }

        // find start pixel
        for (int i = 0; i < wrapper.getImageHeight(); i++) {
            for (int j = 0; j < wrapper.getImageWidth(); j++) {
                // if pixel is not an background pixel
                // set every target image pixel to background. The traceContour method will set the foreground contour pixels.
//                wrapper.setPixel(j, i, BACKGROUND.byteValue());
                if (getTwoDimensionWrapper().getOriginalEndianPixel(j, i) != BACKGROUND && !checkcontains(j, i)) {
                    // start tracing
                    System.out.println("Found Contourpixel at: x=" + j + ", and y=" + i);
                    traceContour(wrapper, j, i);
                }
            }
        }

        updateDrawAndShow();
    }

    private void traceContour(ByteArrayTwoDimensionWrapper wrapper, int x, int y) {

        int rightCount = 0; // if rightCount + 1 == 4
        ViewDirection actualViewDirection = ViewDirection.EAST;
        Polygon currentPolygon = new Polygon();
        Tuple<Integer, Integer> actualPixelPosition = new Tuple<Integer, Integer>(Integer.valueOf(x), Integer.valueOf(y));
        do {
            int pixel = wrapper.getOriginalEndianPixel(actualPixelPosition.getX(), actualPixelPosition.getY());
            if (pixel == BACKGROUND) {
                Turn toTurn = Turn.RIGHT;
                if (rightCount + 1 == 4) {
                    toTurn = Turn.LEFT;
                }
                wrapper.setPixel(actualPixelPosition.getX(), actualPixelPosition.getY(), BACKGROUND.byteValue());
                actualPixelPosition = actualViewDirection.coordinateDirectionTranscoder(toTurn, actualPixelPosition.getX(), actualPixelPosition.getY());
                actualViewDirection = calculateViewDirectionByTurn(actualViewDirection, toTurn);
                rightCount++;
            } else {
                rightCount = 0;
                currentPolygon.addPoint(actualPixelPosition.getX(), actualPixelPosition.getY());
                wrapper.setPixel(actualPixelPosition.getX(), actualPixelPosition.getY(), FOREGROUND.byteValue());
                actualPixelPosition = actualViewDirection.coordinateDirectionTranscoder(Turn.LEFT, actualPixelPosition.getX(), actualPixelPosition.getY());
                actualViewDirection = calculateViewDirectionByTurn(actualViewDirection, Turn.LEFT);
            }

        } while (actualPixelPosition.getX().intValue() != x || actualPixelPosition.getY().intValue() != y);

        polyvec.add(currentPolygon);
    }

    private ViewDirection calculateViewDirectionByTurn(ViewDirection viewDirection, Turn turn) {
        ViewDirection newViewDirection = null;
        if (turn == Turn.LEFT) {
            newViewDirection = ViewDirection.valueOf(viewDirection.getLeft().toUpperCase());
        } else {
            newViewDirection = ViewDirection.valueOf(viewDirection.getRight().toUpperCase());
        }
        return newViewDirection;
    }

    /**
     * Checks, if a point is inside one of the Objects.
     *
     * @param x The x image coordinate of the point to be examined.
     * @param y the y image coordinate of the point to be examined.
     * @return inside True, if the point lies inside an object, false otherwise.
     */
    public boolean checkcontains(int x, int y) //checks all Polygons
    {
        for (int i = 0; i < this.polyvec.size(); i++) {
            Polygon poly = (Polygon) this.polyvec.elementAt(i);
            if (poly.contains(x, y)) {
                return true;
            }

            for (int j = 0; j < poly.npoints; j++) {
                if (x == poly.xpoints[j] && y == poly.ypoints[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    private enum Turn {
        LEFT,
        RIGHT;
    }

    private enum ViewDirection {
        NORTH("west", "east"),
        EAST("north", "south"),
        SOUTH("east", "west"),
        WEST("south", "north");

        private String left;
        private String right;

        private ViewDirection(String left, String right) {
            this.left = left;
            this.right = right;
        }

        public String getLeft() {
            return left;
        }

        public String getRight() {
            return right;
        }

        private Tuple<Integer, Integer> coordinateDirectionTranscoder(Turn turn, int actualX, int actualY) {

            Integer newX = null;
            Integer newY = null;

            switch (this) {
                case NORTH:
                    newY = actualY;
                    if (turn == Turn.LEFT) {
                        newX = actualX - 1;
                    } else {
                        newX = actualX + 1;
                    }

                    break;
                case EAST:
                    newX = actualX;
                    if (turn == Turn.LEFT) {
                        newY = actualY - 1;
                    } else {
                        newY = actualY + 1;
                    }
                    break;
                case SOUTH:
                    newY = actualY;
                    if (turn == Turn.LEFT) {
                        newX = actualX + 1;
                    } else {
                        newX = actualX - 1;
                    }
                    break;
                case WEST:
                    newX = actualX;
                    if (turn == Turn.LEFT) {
                        newY = actualY + 1;
                    } else {
                        newY = actualY - 1;
                    }
                    break;
                default:
                    throw new IllegalStateException("Given Value not supported: " + this);
            }

            return new Tuple<Integer, Integer>(newX, newY);
        }
    }

    public static void main(String[] args) {

        new ImageJ();

        //		https://github.com/imagej/minimal-ij1-plugin/blob/master/src/main/java/Process_Pixels.java
        InputStream is = Contour_Tracing.class.getClassLoader().getResourceAsStream("pics/Bin-DAPI.tif");
        // @See: http://imagej.net/pipermail/imagej-devel/2013-January/001374.html
        if (is != null) {
            Opener opener = new Opener();
            ImagePlus image = opener.openTiff(is, "Bin-DAPI.tif");
            image.show();
        }


        IJ.runPlugIn(Contour_Tracing.class.getName(), "");
    }
}
