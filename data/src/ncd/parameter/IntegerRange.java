package ncd.parameter;

import ncd.logger.NcdLogger;

public class IntegerRange extends Range<Integer> {

	public final static Range<Integer>	INFINITE	= new IntegerRange(new Integer(0), -Integer.MAX_VALUE, Integer.MAX_VALUE);
	public final static Range<Integer>	POSITIVE	= new IntegerRange(new Integer(0), new Integer(0), Integer.MAX_VALUE);

	public IntegerRange(Integer value) {
		super(value, -Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public IntegerRange(Integer value, Integer lowerLimit, Integer upperLimit) {
		super(value);
		this.setLimits(lowerLimit, upperLimit);
	}

	@Override
	public boolean setValue(Integer value) {
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
		this.value = value;
		return true;
	}

	@Override
	public boolean setLimits(Integer minimum, Integer maximum) {
		if (minimum > maximum) {
			String message = "Cannot set limits: You are trying to set the lower bound " + minimum + " to greater than the upper limit " + maximum;
			NcdLogger.Error(this.getClass().getName(), "setLimits", message);
			return false;
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
	public boolean setMinimum(Integer minimum) {
		if (minimum > upperLimit) {
			String message = "Cannot set limits: You are trying to set the lower bound " + minimum + " to greater than the upper limit " + this.upperLimit;
			NcdLogger.Error(this.getClass().getName(), "setLimits", message);
			return false;
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
	public boolean setMaximum(Integer maximum) {
		if (maximum < lowerLimit) {
			String message = "Cannot set limits: You are trying to set the lower bound " + this.lowerLimit + " to greater than the upper limit " + maximum;
			NcdLogger.Error(this.getClass().getName(), "setLimits", message);
			return false;
		}

		if (value > maximum) {
			String message = "Parameter value " + this.value + " is higher than this new upper bound " + maximum + " - Adjusting value to equal new upper bound value ";
			NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
			value = maximum;
		}

		this.upperLimit = maximum;
		return true;
	}

	@Override
	public boolean contains(Integer number) {
		if (number >= this.lowerLimit && number <= this.upperLimit) {
			return true;
		}
		return false;
	}

	@Override
	public IRange<Integer> copy() {
		return new IntegerRange(this.value, this.lowerLimit, this.upperLimit);
	}

	@Override
	public Integer parse(String text) {
		return Integer.valueOf(text.trim());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lowerLimit == null) ? 0 : lowerLimit);
		result = prime * result + ((upperLimit == null) ? 0 : upperLimit);
		result = prime * result + ((value == null) ? 0 : value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		IntegerRange other = (IntegerRange) obj;
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
