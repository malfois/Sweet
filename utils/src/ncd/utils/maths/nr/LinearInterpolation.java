package ncd.utils.maths.nr;

import org.eclipse.january.dataset.DoubleDataset;

/**
 * Piecewise linear interpolation object.
 * 
 * Construct with x and y vectors, then call interp for interpolated values.
 * 
 * Copyright (C) Numerical Recipes Software 1986-2007 Java translation Copyright (C) Huang Wen Hui 2012
 * 
 * @author hwh
 * 
 */
public class LinearInterpolation extends BaseInterpolation {

	public LinearInterpolation(final DoubleDataset xv, final DoubleDataset yv) {
		super(xv, yv, 2);
	}

	public double rawinterp(final int j, final double x) {
		if (xx.getDouble(new int[] { j }) == xx.getDouble(new int[] { j + 1 })) return yy.getDouble(new int[] { j });
		else return yy.getDouble(new int[] { j }) + ((x - xx.getDouble(new int[] { j })) / (xx.getDouble(new int[] { j + 1 }) - xx.getDouble(new int[] { j })))
				* (yy.getDouble(new int[] { j + 1 }) - yy.getDouble(new int[] { j }));
	}

}
