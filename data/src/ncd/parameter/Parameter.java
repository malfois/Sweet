package ncd.parameter;

public abstract class Parameter<T extends Number> implements IParameter<T> {

	public final static IParameter<Double>	NaN		= new DoubleParameter(Double.NaN);

	protected T								value;
	protected IRange<T>						minimum;
	protected IRange<T>						maximum;
	protected Boolean						fixed	= false;
	protected Format						format	= new Format();
	protected String						name	= "";

	protected Parameter(T number, IRange<T> minimum, IRange<T> maximum) {
		this.value = number;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	protected Parameter(String name, T number, IRange<T> minimum, IRange<T> maximum) {
		this.name = name;
		this.value = number;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	public Format getFormat() {
		return this.format;
	}

	@Override
	public Boolean isFixed() {
		return fixed;
	}

	@Override
	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public IRange<T> getMinimumRange() {
		return this.minimum;
	}

	public IRange<T> getMaximumRange() {
		return this.maximum;
	}

	public T getValue() {
		return this.value;
	}

	public T getMinimum() {
		return this.minimum.getValue();
	}

	public T getMaximum() {
		return this.maximum.getValue();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Parameter [value=" + value + ", minimum=" + minimum + ", maximum=" + maximum + ", fixed=" + fixed + ", format=" + format + "]";
	}

}
