package ncd.utils.maths;

import javafx.geometry.Point2D;
import ncd.utils.plot.GraphicScale;

public class Maths {

	// Find the minimum of a 2D array of floats.
	public static double getMinimum(GraphicScale scale, double[][] array) {
		double yMin = Double.MAX_VALUE;
		boolean bEmpty = true;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] != 0) {
					bEmpty = false;
					break;
				}
			}
		}
		if (bEmpty) return 0;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				Point2D f = scale.to(0, array[i][j]);
				if (f != null) {
					if (f.getY() < yMin) yMin = f.getY();
				}
			}
		}

		return yMin;
	}

	public static double getMinimum(double[][] array) {
		double xMin = Double.MAX_VALUE;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] < xMin) {
					xMin = array[i][j];
				}
			}
		}
		return xMin;
	}

	public static double getMinimum(double[] array) {
		double xMin = Double.MAX_VALUE;

		for (int i = 0; i < array.length; i++) {
			if (array[i] < xMin) {
				xMin = array[i];
			}
		}
		return xMin;
	}

	// Find the minimum of a 2D array of floats between points nFirst and nLast.
	public static double[] getMinMax(double[][] array, double nFirst, double nLast) {
		double[] range = { Double.MAX_VALUE, -Double.MAX_VALUE };
		if (nFirst < 0 || nFirst > array[0].length) {
			return range;
		}
		if (nLast < 0 || nLast > array[0].length) {
			return range;
		}
		if (nFirst > nLast) {
			return range;
		}

		boolean bEmpty = true;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] != 0) {
					bEmpty = false;
					break;
				}
			}
		}
		if (bEmpty) return range;

		for (int i = 0; i < array.length; i++) {
			for (int j = (int) nFirst; j < nLast; j++) {
				if (array[i][j] < range[0]) range[0] = array[i][j];
				if (array[i][j] > range[1]) range[1] = array[i][j];
			}
		}
		return range;
	}

	// Find the maximum of a 2D array of floats.
	public static double getMaximum(GraphicScale scale, double[][] array) {
		double yMax = -Double.MAX_VALUE;
		boolean bEmpty = true;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] != 0) {
					bEmpty = false;
					break;
				}
			}
		}
		if (bEmpty) return 0;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				Point2D f = scale.to(0, array[i][j]);
				if (f != null) {
					if (f.getY() > yMax) yMax = f.getY();
				}
			}
		}

		return yMax;
	}

	public static double getMaximum(double[][] array) {
		double xMax = -Double.MAX_VALUE;

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] > xMax) {
					xMax = array[i][j];
				}
			}
		}
		return xMax;
	}

	public static double getMaximum(double[] array) {
		double xMax = -Double.MAX_VALUE;

		for (int i = 0; i < array.length; i++) {
			if (array[i] > xMax) {
				xMax = array[i];
			}
		}
		return xMax;
	}

	public static double getMinimum(boolean absolute_scale, double[] array) {
		double xMin = Double.MAX_VALUE;
		double x;

		for (int i = 0; i < array.length; i++) {
			if (!absolute_scale) {
				if (array[i] != 0.0) {
					x = java.lang.Math.abs(array[i]);
					x = java.lang.Math.log(x);
					if (x < xMin) {
						xMin = x;
					}
				} else {
					if (array[i] < xMin) xMin = array[i];
				}
			}
		}
		return xMin;
	}

	public static double getMaximum(boolean absolute_scale, double[] array) {
		double xMax = -Double.MAX_VALUE;
		double x;

		for (int i = 0; i < array.length; i++) {
			if (!absolute_scale) {
				if (array[i] != 0.0) {
					x = java.lang.Math.abs(array[i]);
					x = java.lang.Math.log(x);
					if (x > xMax) xMax = x;
				}
			} else {
				if (array[i] > xMax) xMax = array[i];
			}
		}
		return xMax;
	}

	public static double getMinimum(double x1, double x2) {

		double xMin = x1;
		if (x1 > x2) xMin = x2;
		return xMin;
	}

	public static double getMaximum(double x1, double x2) {

		double xMax = x1;
		if (x1 < x2) xMax = x2;
		return xMax;
	}

}
