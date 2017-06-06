/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package ncd.utils.maths.optimiser;

import org.eclipse.january.dataset.Dataset;

import ncd.parameter.DoubleParameter;
import ncd.parameter.DoubleRange;
import ncd.utils.maths.fitting.Function;
import ncd.utils.maths.fitting.Gaussian;
import ncd.utils.maths.fitting.Peak;

public class Fitter {

	public static double	quality	= 1e-6;
	public static Long		seed	= (long) 1;

	public static Function GaussianFit(Dataset data, Dataset axis) {

		double axisMin = axis.min().doubleValue();
		double axisMax = axis.max().doubleValue();
		double maxFWHM = axisMax - axisMin;
		double peakToPeak = data.peakToPeak().doubleValue();
		if (peakToPeak == 0) {
			peakToPeak = 1;
		}
		double maxArea = Math.abs(axis.peakToPeak().doubleValue() * peakToPeak);
		double dataMax = data.max().doubleValue();
		double dataMin = data.min().doubleValue();
		if (dataMax == dataMin) {
			dataMax = dataMin + 1.0;
		}

		DoubleRange axisMinRange = new DoubleRange(axisMin, -Double.MAX_VALUE, Double.MAX_VALUE);
		DoubleRange axisMaxRange = new DoubleRange(axisMax, -Double.MAX_VALUE, Double.MAX_VALUE);
		DoubleParameter position = new DoubleParameter(Gaussian.NAMES[Peak.POSITION], (axisMax + axisMin) / 2.0, axisMinRange, axisMaxRange);

		DoubleRange fwhmMinRange = new DoubleRange(Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
		DoubleRange fwhmMaxRange = new DoubleRange(maxFWHM, Double.MIN_VALUE, Double.MAX_VALUE);
		DoubleParameter fwhm = new DoubleParameter(Gaussian.NAMES[Peak.FWHM], maxFWHM / 5.0, fwhmMinRange, fwhmMaxRange);

		DoubleRange areaMinRange = new DoubleRange(-maxArea, -Double.MAX_VALUE, Double.MAX_VALUE);
		DoubleRange areaMaxRange = new DoubleRange(maxArea, -Double.MAX_VALUE, Double.MAX_VALUE);
		DoubleParameter area = new DoubleParameter(Gaussian.NAMES[Peak.AREA], maxArea / 2.0, areaMinRange, areaMaxRange);

		DoubleRange offsetMinRange = new DoubleRange(dataMin, -Double.MAX_VALUE, Double.MAX_VALUE);
		DoubleRange offsetMaxRange = new DoubleRange(dataMax, -Double.MAX_VALUE, Double.MAX_VALUE);
		DoubleParameter offset = new DoubleParameter(Gaussian.NAMES[Peak.OFFSET], (dataMax + dataMin) / 2.0, offsetMinRange, offsetMaxRange);

		DoubleParameter[] params = new DoubleParameter[] { position, fwhm, area, offset };
		Gaussian gauss = new Gaussian(params);

		Function f = gauss;
		GeneticAlgorithm ga = new GeneticAlgorithm(quality, new Long(1));
		f = ga.optimize(axis, data, gauss);
		return f;
	}

}
