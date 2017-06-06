package ncd.utils.file;

public class AsciiTabFile extends AsciiFile {

	private static final long serialVersionUID = -8710102108447536672L;

	public AsciiTabFile(String filename) {
		super(filename, "\t");
	}

}
