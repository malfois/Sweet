package ncd.utils.maths.fitting;

import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;

import ncd.parameter.DoubleParameter;
import ncd.parameter.DoubleRange;

public class Step extends Peak {

	private static final long				serialVersionUID	= 3727470127674104203L;

	private static final String				NAME				= "Step";
	private static final String				DESCRIPTION			= "y(x) = a if min <x < max ";
	public static final String[]			NAMES				= new String[] { "position", "range", "area", "offset" };
	private static final DoubleParameter[]	PARAMETERS			= new DoubleParameter[] {
			new DoubleParameter(NAMES[POSITION], 0.0, DoubleRange.INFINITE, DoubleRange.INFINITE),
			new DoubleParameter(NAMES[FWHM], 1.0, DoubleRange.POSITIVE, DoubleRange.POSITIVE),
			new DoubleParameter(NAMES[AREA], 1.0, DoubleRange.INFINITE, DoubleRange.INFINITE),
			new DoubleParameter(NAMES[OFFSET], 0.0, DoubleRange.INFINITE, DoubleRange.INFINITE) };

	public Step() {
		this(PARAMETERS);
		this.setName(NAME);
		this.setDescription(DESCRIPTION);
	}

	public Step(DoubleParameter... params) {
		if (params.length != PARAMETERS.length) throw new IllegalArgumentException("A step function requires 4 parameters, and it has been given " + params.length);
		int nParams = params.length;
		this.parameters = new DoubleParameter[nParams];
		for (int i = 0; i < nParams; i++) {
			this.parameters[i] = new DoubleParameter(params[i].getName(), params[i].getValue(), params[i].getMinimumRange(), params[i].getMaximumRange());
		}
		this.setName(NAME);
		this.setDescription(DESCRIPTION);
	}

	public double val(double value) {
		double pos = getParameterValue(POSITION);
		double range = getParameterValue(FWHM);
		double area = getParameterValue(AREA);
		double height = area / range;
		double offset = getParameterValue(OFFSET);

		double arg = offset;
		if (value > (pos - range / 2.0) && value < (pos + range / 2.0)) {
			arg = arg + height;
		}
		return arg;
	}

	public DoubleDataset fillWithValues(DoubleDataset coords) {

		double pos = getParameterValue(POSITION);
		double range = getParameterValue(FWHM);
		double area = getParameterValue(AREA);
		double height = area / range;
		double offset = getParameterValue(OFFSET);

		double[] x = coords.getData();
		int n = x.length;
		double[] buffer = new double[n];
		for (int i = 0; i < n; i++) {
			double arg = offset;
			if (x[i] > (pos - range / 2.0) && x[i] < (pos + range / 2.0)) {
				arg = arg + height;
			}
			buffer[i] = arg;
		}
		return (DoubleDataset) DatasetFactory.createFromObject(buffer);
	}

}
