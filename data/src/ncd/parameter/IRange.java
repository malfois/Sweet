package ncd.parameter;

public interface IRange<T extends Number> {

	public T parse(String text);

	public boolean contains(T number);

	public T getLowerLimit();

	public T getUpperLimit();

	public T getValue();

	public boolean setLimits(T minimum, T maximum);

	public boolean setMaximum(T maximum);

	public boolean setMinimum(T minimum);

	public boolean setValue(T value);

	public IRange<T> copy();
}
