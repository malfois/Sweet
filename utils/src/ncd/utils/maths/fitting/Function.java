/*-
 * Copyright 2011 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ncd.utils.maths.fitting;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.DatasetUtils;
import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.IDataset;

import ncd.parameter.DoubleParameter;
import ncd.utils.file.Metadata;

public abstract class Function implements Serializable {

	private static final long	serialVersionUID	= -3635786543741672175L;
	public static final String	NAME				= "Name";
	public static final String	DESCRIPTION			= "Description";

	/** The array of parameters which specify all the variables in the minimisation problem */
	protected DoubleParameter[]	parameters;

	/** The name of the function, a description more than anything else. */
	protected String			name				= "default";

	/** The description of the function */
	protected String			description			= "default";

	/**
	 * Constructor which is given a set of parameters to begin with.
	 * 
	 * @param params
	 *            An array of parameters
	 */
	public Function(DoubleParameter... params) {
		if (parameters == null) parameters = new DoubleParameter[params.length];
		int n = Math.min(params.length, parameters.length);
		for (int i = 0; i < n; i++) {
			DoubleParameter p = params[i];
			parameters[i] = new DoubleParameter(p.getName(), p.getValue(), p.getMinimumRange(), p.getMaximumRange());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String newDescription) {
		description = newDescription;
	}

	public DoubleParameter getParameter(int index) {
		return parameters[index];
	}

	public DoubleParameter[] getParameters() {
		DoubleParameter[] params = new DoubleParameter[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			params[i] = parameters[i];
		}
		return params;
	}

	public int getNoOfParameters() {
		return parameters.length;
	}

	public double getParameterValue(int index) {
		return parameters[index].getValue();
	}

	final public double[] getParameterValues() {
		int n = getNoOfParameters();
		double[] result = new double[n];
		for (int j = 0; j < n; j++) {
			result[j] = getParameterValue(j);
		}
		return result;
	}

	public void setParameter(int index, DoubleParameter parameter) {
		parameters[index] = parameter;
	}

	public String toExtendedString() {
		StringBuffer out = new StringBuffer();
		int n = getNoOfParameters();
		out.append(String.format("'%s' has %d parameters:\n", name, n));
		for (int i = 0; i < n; i++) {
			DoubleParameter p = getParameter(i);
			out.append(String.format("%d) %s = %g in range [%g, %g]\n", i, p.getName(), p.getValue(), p.getMinimum(), p.getMaximum()));
		}
		return out.toString();
	}

	public String parameterValuesToString() {
		StringBuffer out = new StringBuffer();
		int n = getNoOfParameters();
		out.append(String.format("Function[Name=%s, description=[%s] ", name, this.description));
		for (int i = 0; i < n; i++) {
			DoubleParameter p = getParameter(i);
			out.append(String.format("%s=%g ", p.getName(), p.getValue()));
		}
		return out.toString();
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "Function [parameters=" + (parameters != null ? Arrays.asList(parameters).subList(0, Math.min(parameters.length, maxLen)) : null) + ", name=" + name
				+ ", description=" + description + "]";
	}

	public DoubleDataset calculateValues(IDataset coords) {
		DoubleDataset axis = (DoubleDataset) DatasetUtils.convertToDataset(coords).cast(Dataset.FLOAT64);
		DoubleDataset result = fillWithValues(axis);
		result.setName(name);
		return result;
	}

	/**
	 * Fill dataset with values. Implementations should reset the iterator before use
	 * 
	 * @param data
	 * @param it
	 */
	abstract public DoubleDataset fillWithValues(DoubleDataset axis);

	abstract public double val(double x);

	public double residual(IDataset data, IDataset coords) {
		double residual = 0;
		DoubleDataset y1 = (DoubleDataset) DatasetUtils.convertToDataset(data).cast(Dataset.FLOAT64);
		residual = y1.residual(calculateValues(coords), null, false);
		return residual;
	}

	public Metadata getMetadata() {
		Map<String, String> map = new HashMap<>();
		map.put(Function.NAME, this.name);
		map.put(Function.DESCRIPTION, this.description);
		for (DoubleParameter parameter : this.parameters) {
			map.put(parameter.getName(), String.valueOf(parameter.getValue()));
		}
		return new Metadata(map);
	}
}
