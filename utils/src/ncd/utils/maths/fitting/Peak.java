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

import ncd.parameter.DoubleParameter;

/**
 * A peak function is determined at least three parameters: position, full width at half-maximum, area
 * <p>
 * If height of peak can be calculated then set height in the overriding calcCachedParameters method.
 */
public abstract class Peak extends Function {
	private static final long	serialVersionUID	= -7207577796913894867L;

	public static final int		POSITION			= 0;
	public static final int		FWHM				= 1;
	public static final int		AREA				= 2;
	public static final int		OFFSET				= 3;

	public Peak(DoubleParameter... params) {
		super(params);
	}

	public double getPosition() {
		return getParameterValue(POSITION);
	}

	public double getFWHM() {
		return getParameterValue(FWHM);
	}

	public double getArea() {
		return getParameterValue(AREA);
	}

	public double getOffset() {
		return getParameterValue(OFFSET);
	}

	public double getHeight() {
		double constant = Math.sqrt(4. * Math.log(2.));

		double area = getParameterValue(AREA);
		double sigma = getParameterValue(FWHM) / constant;
		double height = area / (Math.sqrt(Math.PI) * sigma);

		return height;
	}

}
