package ncd.utils.file;

public class DataFileException extends Exception {

	private static final long serialVersionUID = -2141711987357816806L;

	public DataFileException(String SFHe) {
		super(SFHe);
	}

	/**
	 * @param SFHe
	 *            should be the string that is thrown when there is an exception.
	 * @param e
	 *            exception that is passed to this method
	 */
	public DataFileException(String SFHe, Throwable e) {
		super(SFHe, e);
	}

}
