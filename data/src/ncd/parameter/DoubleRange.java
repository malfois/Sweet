package ncd.parameter;

import ncd.logger.NcdLogger;

public class DoubleRange extends Range<Double> {

	public final static DoubleRange	INFINITE	= new DoubleRange(new Double(0.0), -Double.MAX_VALUE, Double.MAX_VALUE);
	public final static DoubleRange	POSITIVE	= new DoubleRange(new Double(0.0), new Double(0.0), Double.MAX_VALUE);

	public DoubleRange(Double value) {
		super(value, -Double.MAX_VALUE, Double.MAX_VALUE);
	}

	public DoubleRange(Double value, Double lowerLimit, Double upperLimit) {
		super(value);
		this.setLimits(lowerLimit, upperLimit);
	}

	public boolean setLimits(Double minimum, Double maximum) {
		if (minimum > maximum) {
			String message = "Cannot set limits: You are trying to set the lower bound " + minimum + " to greater than the upper limit " + maximum;
			NcdLogger.Error(this.getClass().getName(), "setLimits", message);
			return false;
		}
		if (value == null) {
			return true;
		}
		if (value < minimum) {
			String message = "Parameter value " + this.value + " is lower than this new lower bound " + minimum + " - Adjusting value to equal new lower bound value ";
			NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
			value = minimum;
		}

		if (value > maximum) {
			String message = "Parameter value " + this.value + " is higher than this new upper bound " + maximum + " - Adjusting value to equal new upper bound value ";
			NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
			value = maximum;
		}

		this.lowerLimit = minimum;
		this.upperLimit = maximum;
		return true;
	}

	@Override
	public boolean setMinimum(Double minimum) {
		if (minimum > upperLimit) {
			String message = "Cannot set limits: You are trying to set the lower bound " + minimum + " to greater than the upper limit " + this.upperLimit;
			NcdLogger.Error(this.getClass().getName(), "setLimits", message);
			return false;
		}
		if (value == null) {
			return true;
		}

		if (value < minimum) {
			String message = "Parameter value " + this.value + " is lower than this new lower bound " + minimum + " - Adjusting value to equal new lower bound value ";
			NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
			value = lowerLimit;
		}
		this.lowerLimit = minimum;
		return true;

	}

	@Override
	public boolean setMaximum(Double maximum) {
		if (maximum < lowerLimit) {
			String message = "Cannot set limits: You are trying to set the lower bound " + this.lowerLimit + " to greater than the upper limit " + maximum;
			NcdLogger.Error(this.getClass().getName(), "setLimits", message);
			return false;
		}
		if (value == null) {
			return true;
		}

		if (value > maximum) {
			String message = "Parameter value " + this.value + " is higher than this new upper bound " + maximum + " - Adjusting value to equal new upper bound value ";
			NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
			value = maximum;
		}
		this.upperLimit = maximum;
		return true;
	}

	public boolean setValue(Double value) {
		if (value != null) {
			if (value > upperLimit) {
				String message = "Parameter value " + this.value + " is higher than the upper bound " + this.upperLimit + " - Adjusting value to equal upper bound value ";
				NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
				this.value = upperLimit;
				return false;
			}
			if (value < lowerLimit) {
				String message = "Parameter value " + this.value + " is lower than the lower bound " + this.lowerLimit + " - Adjusting value to equal the lower bound value ";
				NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
				this.value = lowerLimit;
				return false;
			}
		}
		this.value = value;
		return true;
	}

	@Override
	public boolean contains(Double number) {
		if (number >= this.lowerLimit && number <= this.upperLimit) {
			return true;
		}
		return false;
	}

	@Override
	public IRange<Double> copy() {
		return new DoubleRange(this.value, this.lowerLimit, this.upperLimit);
	}

	@Override
	public Double parse(String text) {
		return Double.parseDouble(text);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lowerLimit == null) ? 0 : Double.valueOf(lowerLimit).hashCode());
		result = prime * result + ((upperLimit == null) ? 0 : Double.valueOf(upperLimit).hashCode());
		result = prime * result + ((value == null) ? 0 : Double.valueOf(value).hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DoubleRange other = (DoubleRange) obj;
		if (lowerLimit == null) {
			if (other.lowerLimit != null) return false;
		} else if (lowerLimit != other.lowerLimit) return false;
		if (upperLimit == null) {
			if (other.upperLimit != null) return false;
		} else if (upperLimit != other.upperLimit) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (value != other.value) return false;
		return true;
	}

}
