package ncd.parameter;

public interface IParameter<T extends Number> {

	public T getValue();

	public T getMinimum();

	public T getMaximum();

	public IRange<T> getValueRange();

	public IRange<T> getMinimumRange();

	public IRange<T> getMaximumRange();

	public boolean setMinimumRange(IRange<T> range);

	public boolean setMaximumRange(IRange<T> range);

	public boolean setValue(T value);

	public void setFixed(Boolean fixed);

	public Boolean isFixed();

	public IParameter<T> copy();

	public String valueToString();

	public String minimumToString();

	public String maximumToString();

	public Format getFormat();

	public boolean setLimits(IRange<T> minimum, IRange<T> maximum);

	public String getName();

}
