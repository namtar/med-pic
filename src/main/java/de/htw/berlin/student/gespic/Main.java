package de.htw.berlin.student.gespic;

import ij.ImageJ;

import javax.swing.*;

/**
 * Main method of the med pic app.
 * <p/>
 * Created by Matthias Drummer on 08.10.14.
 */
public class Main {

	public static void main(final String[] args) {

		Runnable runnner = new Runnable() {

			@Override
			public void run() {
				Main main = new Main();
				main.invokeImageJ(args);
			}
		};

		SwingUtilities.invokeLater(runnner);
	}

	private void invokeImageJ(String[] args) {
		ImageJ.main(args);

	}

}
