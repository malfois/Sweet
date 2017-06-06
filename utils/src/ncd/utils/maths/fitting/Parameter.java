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

/**
 * Class which wraps a single parameter for a function and allows for its change
 */
public class Parameter implements Serializable {

	private static final long	serialVersionUID	= 3042335409382328445L;

	private String				name				= "";

	private double				value				= 0.0;

	private double				upperLimit			= Double.MAX_VALUE;

	private double				lowerLimit			= -(Double.MAX_VALUE);

	/**
	 * Constructor that sets up the value and let everything else to its default value
	 * 
	 * @param value
	 *            Value of the parameter
	 */
	public Parameter(double value) {
		this.value = value;
	}

	/**
	 * Constructor that sets up the value along with the max and min parameters
	 * 
	 * @param name
	 *            Name of the parameter
	 * @param value
	 *            Value of the parameter
	 * @param lowerLimit
	 *            Lower limit the parameter is restricted to
	 * @param upperLimit
	 *            Upper limit the parameter is restricted to
	 */
	public Parameter(String name, double value, double lowerLimit, double upperLimit) {
		this.name = name;
		this.value = value;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public double getUpperLimit() {
		return this.upperLimit;
	}

	public double getLowerLimit() {
		return this.lowerLimit;
	}

	public void setLimits(double newLowerLimit, double newUpperLimit) {

		this.setLowerLimit(newLowerLimit);
		this.setUpperLimit(newUpperLimit);
	}

	public void setLowerLimit(double lowerLimit) {
		if (lowerLimit > upperLimit) {
			System.out.println("Cannot set lower limit: You are trying to set the lower bound to greater than the upper limit");
			return;
		}

		if (value < lowerLimit) {
			String t = String.format("Parameter value %8.3f is lower than this new lower bound %8.3f - Adjusting value to equal new lower bound value ", value, lowerLimit);
			System.out.println(t);
			value = lowerLimit;
		}
		this.lowerLimit = lowerLimit;
	}

	public void setUpperLimit(double upperLimit) {
		if (upperLimit < lowerLimit) {
			System.out.println("Cannot set lower limit: You are trying to set the lower bound to greater than the upper limit");
			return;
		}

		if (value > upperLimit) {
			String t = String.format("Parameter value %8.3f is higher than this new upper bound %8.3f - Adjusting value to equal new upper bound value ", value, upperLimit);
			System.out.println(t);
			value = upperLimit;
		}

		this.upperLimit = upperLimit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(double value) {
		if (value > upperLimit) {
			this.value = upperLimit;
			return;
		}
		if (value < lowerLimit) {
			this.value = lowerLimit;
			return;
		}
		this.value = value;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", value=" + value + ", upperLimit=" + upperLimit + ", lowerLimit=" + lowerLimit + "]";
	}

}
