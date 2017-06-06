package ncd.parameter;

import ncd.logger.NcdLogger;

public abstract class Range<T extends Number> implements IRange<T> {

	protected T	value;
	protected T	lowerLimit;
	protected T	upperLimit;

	public Range(T value) {
		this.value = value;
	}

	public Range(T value, T lowerLimit, T upperLimit) {
		this.value = value;
		if (!this.setLimits(lowerLimit, upperLimit)) {
			NcdLogger.Error(this.getClass().getSimpleName(), "Constructor", "Limits not changed");
		}
	}

	public T getValue() {
		return value;
	}

	public T getLowerLimit() {
		return lowerLimit;
	}

	public T getUpperLimit() {
		return upperLimit;
	}

	@Override
	public String toString() {
		return "Range [value=" + value + ", lowerLimit=" + lowerLimit + ", upperLimit=" + upperLimit + "]";
	}

}
