package ncd.utils.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ncd.data.IData;

public class AsciiFile extends AFile {

	private static final long	serialVersionUID	= 6684718168842990618L;

	public final static String	NUMBER_OF_CURVES	= "Number of curves";
	public final static String	NUMBER_OF_POINTS	= "Number of points";
	public final static String	X_UNIT				= "X Unit";
	public final static String	Y_UNIT				= "Y Unit";
	public final static String	X_NAME				= "x name";
	public final static String	Y_NAME				= "y name";
	public final static String	PLOT				= "Plot curve";

	private String				separator;

	public AsciiFile(String filename, String separator) {
		super(filename);
		this.separator = separator;
	}

	@Override
	public void readData() {
	}

	@Override
	public void writeData(Metadata metadata, IData data) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.getAbsolutePath()));
			writer.write(metadata.toText());
			writer.write(data.toText(this.separator));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
