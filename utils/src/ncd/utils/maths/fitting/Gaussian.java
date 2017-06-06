/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package ncd.utils.maths.fitting;

import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;

import ncd.parameter.DoubleParameter;
import ncd.parameter.DoubleRange;

/**
 * Class which expands on the AFunction class to give the properties of a gaussian. A 1D implementation
 */
public class Gaussian extends Peak {

	private static final long				serialVersionUID	= 8499241906073552802L;

	private static final String				NAME				= "Gaussian";
	private static final String				DESCRIPTION			= "y(x) = A exp(-((x-b)^2)/(2*c^2))";
	public static final String[]			NAMES				= new String[] { "position", "fwhm", "area", "offset" };
	private static final DoubleParameter[]	PARAMETERS			= new DoubleParameter[] {
			new DoubleParameter(NAMES[POSITION], 0.0, DoubleRange.INFINITE, DoubleRange.INFINITE),
			new DoubleParameter(NAMES[FWHM], 1.0, DoubleRange.POSITIVE, DoubleRange.POSITIVE),
			new DoubleParameter(NAMES[AREA], 1.0, DoubleRange.INFINITE, DoubleRange.INFINITE),
			new DoubleParameter(NAMES[OFFSET], 0.0, DoubleRange.INFINITE, DoubleRange.INFINITE) };

	public Gaussian() {
		this(PARAMETERS);
		this.setName(NAME);
		this.setDescription(DESCRIPTION);
	}

	public Gaussian(DoubleParameter... params) {
		if (params.length != PARAMETERS.length) throw new IllegalArgumentException("A gaussian peak requires 3 parameters, and it has been given " + params.length);
		int nParams = params.length;
		this.parameters = new DoubleParameter[nParams];
		for (int i = 0; i < nParams; i++) {
			this.parameters[i] = new DoubleParameter(params[i].getName(), params[i].getValue(), params[i].getMinimumRange(), params[i].getMaximumRange());
		}
		this.setName(NAME);
		this.setDescription(DESCRIPTION);
	}

	public double val(double value) {
		double CONST = Math.sqrt(4. * Math.log(2.));

		double pos = getParameterValue(POSITION);
		double sigma = getParameterValue(FWHM) / CONST;
		if (sigma == 0) return getParameterValue(OFFSET);

		double area = getParameterValue(AREA);
		double height = area / (Math.sqrt(Math.PI) * sigma);
		double offset = getParameterValue(OFFSET);

		double arg = (value - pos) / sigma;

		return offset + height * Math.exp(-arg * arg);
	}

	public DoubleDataset fillWithValues(DoubleDataset coords) {
		double CONST = Math.sqrt(4. * Math.log(2.));

		double pos = getParameterValue(POSITION);
		double sigma = getParameterValue(FWHM) / CONST;
		double area = getParameterValue(AREA);
		double height = area / (Math.sqrt(Math.PI) * sigma);
		double offset = getParameterValue(OFFSET);

		double[] x = coords.getData();
		int n = x.length;
		double[] buffer = new double[n];
		for (int i = 0; i < n; i++) {
			double arg = offset;
			if (sigma != 0) arg = (x[i] - pos) / sigma;

			buffer[i] = offset + height * Math.exp(-arg * arg);
		}
		return (DoubleDataset) DatasetFactory.createFromObject(buffer);
	}

}
