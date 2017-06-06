package ncd.parameter;

import ncd.logger.NcdLogger;

public class DoubleParameter extends Parameter<Double> {

	public DoubleParameter(Double value) {
		super(value, DoubleRange.INFINITE, DoubleRange.INFINITE);
	}

	public DoubleParameter(Double value, IRange<Double> minimum, IRange<Double> maximum) {
		super(value, minimum, maximum);
	}

	public DoubleParameter(String name, Double value, IRange<Double> minimum, IRange<Double> maximum) {
		super(name, value, minimum, maximum);
	}

	@Override
	public IParameter<Double> copy() {
		return new DoubleParameter(this.getValue(), this.getMinimumRange().copy(), this.getMaximumRange().copy());
	}

	@Override
	public boolean setValue(Double value) {
		if (value != null) {
			if (value > this.maximum.getValue()) {
				String message = "Parameter value " + this.format.toText(this.value) + " is higher than the upper bound " + this.format.toText(this.maximum.getValue())
						+ " - Adjusting value to equal upper bound value ";
				NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
				this.value = this.maximum.getValue();
				return false;
			}
			if (value < this.minimum.getValue()) {
				String message = "Parameter value " + this.format.toText(this.value) + " is lower than the lower bound " + this.format.toText(this.minimum.getValue())
						+ " - Adjusting value to equal the lower bound value ";
				NcdLogger.Warning(this.getClass().getName(), "setLimits", message);
				this.value = this.minimum.getValue();
				return false;
			}
		}
		this.value = value;
		return true;

	}

	@Override
	public String valueToString() {
		return this.format.toText(this.value);
	}

	@Override
	public String minimumToString() {
		return this.format.toText(this.minimum.getValue());
	}

	@Override
	public String maximumToString() {
		return this.format.toText(this.maximum.getValue());
	}

	@Override
	public IRange<Double> getValueRange() {
		return new DoubleRange(this.value, this.minimum.getValue(), this.maximum.getValue());
	}

	@Override
	public boolean setMinimumRange(IRange<Double> range) {
		return this.setLimits(range, this.maximum);
	}

	@Override
	public boolean setMaximumRange(IRange<Double> range) {
		return this.setLimits(this.minimum, range);
	}

	@Override
	public boolean setLimits(IRange<Double> minimum, IRange<Double> maximum) {
		IRange<Double> range = new DoubleRange(this.value);
		if (range.setLimits(minimum.getValue(), maximum.getValue())) {
			this.minimum = minimum;
			this.maximum = maximum;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fixed == null) ? 0 : fixed.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((maximum == null) ? 0 : maximum.hashCode());
		result = prime * result + ((minimum == null) ? 0 : minimum.hashCode());
		result = prime * result + ((value == null) ? 0 : Double.valueOf(value).hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DoubleParameter other = (DoubleParameter) obj;
		if (fixed == null) {
			if (other.fixed != null) return false;
		} else if (!fixed.equals(other.fixed)) return false;
		if (format == null) {
			if (other.format != null) return false;
		} else if (!format.equals(other.format)) return false;
		if (maximum == null) {
			if (other.maximum != null) return false;
		} else if (!maximum.equals(other.maximum)) return false;
		if (minimum == null) {
			if (other.minimum != null) return false;
		} else if (!minimum.equals(other.minimum)) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		return true;
	}

}
