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

import org.eclipse.january.dataset.DoubleDataset;

import ncd.parameter.DoubleParameter;

/** This class basically wraps the function y(x) = c */
public class Offset extends Function {
	private static final long	serialVersionUID	= -4157269958414576188L;

	private static final String	NAME				= "Offset";

	public Offset(DoubleParameter... params) {
		super(params);
		name = NAME;
	}

	@Override
	public DoubleDataset fillWithValues(DoubleDataset axis) {
		return axis.fill(getParameterValue(0));
	}

	@Override
	public double val(double x) {
		return getParameterValue(0);
	}

}
