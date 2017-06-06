package ncd.parameter;

public class RangeException extends Exception {

	private static final long		serialVersionUID	= -6564876774699546108L;

	private Range<? extends Number>	range;

	public RangeException(String message, Range<? extends Number> range) {
		super(message);
		this.range = range;
	}

	public Range<? extends Number> getRange() {
		return range;
	}

}
