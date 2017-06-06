package ncd.scan.spock;

import ncd.scan.configuration.SpockFile;

public class Spock {

	private static volatile Spock	INSTANCE;

	private SpockFile				fileConfiguration	= new SpockFile();

	private Spock() {
	}

	public SpockFile getFileConfiguration() {
		return fileConfiguration;
	}

	public void setFileConfiguration(SpockFile fileConfiguration) {
		this.fileConfiguration = fileConfiguration;
	}

	// generate an instance
	private static synchronized Spock tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Spock();
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 *
	 * @return the instance of this class
	 */
	public static Spock getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		Spock s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

}
