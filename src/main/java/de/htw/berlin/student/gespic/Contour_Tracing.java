package de.htw.berlin.student.gespic;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

import java.awt.*;
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

		// find start pixel
		for (int i = 0; i < wrapper.getImageHeight(); i++) {
			for (int j = 0; j < wrapper.getImageWidth(); j++) {
				// if pixel is not an background pixel
				if (getTwoDimensionWrapper().getEndianPixel(j, i) != 0 && !checkcontains(j, i)) {
					// start tracing
				}
			}
		}

	}

	private void traceContour(ByteArrayTwoDimensionWrapper wrapper, int x, int y) {

		int rightCount = 0; // if rightCount + 1 == 4
		boolean done = false;
		ViewDirection actualViewDirection = ViewDirection.EAST;
		Polygon currentPolygon = new Polygon();
		Tuple<Integer, Integer> actualPixelPosition = new Tuple<Integer, Integer>(Integer.valueOf(x), Integer.valueOf(y));
		do {
			if (checkcontains(actualPixelPosition.getX(), actualPixelPosition.getY())) {
				// we reached our startpoint.
				done = true;
			} else {
				int pixel = wrapper.getEndianPixel(actualPixelPosition.getX(), actualPixelPosition.getY());
				if (pixel == BACKGROUND) {
					actualPixelPosition = actualViewDirection.coordinateDirectionTranscoder(Turn.RIGHT, actualPixelPosition.getX(), actualPixelPosition.getY());
				} else {

				}
			}

		} while (!done);

		polyvec.add(currentPolygon);
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
					throw new IllegalStateException("Given Value not supported: " + viewDirection);
			}

			return new Tuple<Integer, Integer>(newX, newY);
		}
	}

	public static void main(String[] args) {

		new ImageJ();

		//		https://github.com/imagej/minimal-ij1-plugin/blob/master/src/main/java/Process_Pixels.java
		// open the Clown sample
		ImagePlus image = IJ.openImage("/Users/matthias.drummer/Documents/studium/medBildverarbeitung/Testbilder-UE/Bin-DAPI.tif");
		image.show();

		IJ.runPlugIn(Contour_Tracing.class.getName(), "");
	}
}
