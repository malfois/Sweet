package ncd.utils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ncd.data.IData;

public abstract class AFile extends File {

	private static final long	serialVersionUID	= -1466306306784874181L;

	public final static String	DIRECTORY			= "Directory";
	public final static String	TITLE				= "Title";
	public final static String	TYPE				= "Type";

	public abstract void readData();

	public abstract void writeData(Metadata metadata, IData data);

	protected Metadata		metadata;
	protected List<IData>	data	= new ArrayList<>();

	public AFile(String path) {
		super(path);
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public List<IData> getData() {
		return data;
	}

	public static List<AFile> getList(String directory, Filter filter) {
		File file = new File(directory);
		List<AFile> files = new ArrayList<AFile>();
		if (file.isFile() && file.getName().endsWith(filter.getFileExtension())) {
			files.add(AFile.Factory(filter, file.getAbsolutePath()));
			return files;
		}

		File f = null;
		String[] paths;

		try {
			// create new file
			f = new File(directory);
			// array of files and directory
			paths = f.list();
			// for each name in the path array
			for (String p : paths) {
				if (p.toLowerCase().endsWith(filter.getFileExtension())) {
					files.add(AFile.Factory(filter, p));
				}
			}

			return files;
		} catch (Exception e) {
			// if any error occurs
			e.printStackTrace();
		}
		return null;
	}

	public static AFile Factory(Filter filter, String filename) {
		AFile file = null;
		if (filter.equals(Filter.SCAN_DATA)) {
			file = new ScanFile(filename);
		}
		if (filter.equals(Filter.ASCII_TAB)) {
			file = new AsciiTabFile(filename);
		}
		if (filter.equals(Filter.ASCII_SPACE)) {
			file = new AsciiSpaceFile(filename);
		}
		if (filter.equals(Filter.ASCII_COMMA)) {
			file = new AsciiCommaFile(filename);
		}

		return file;
	}

}
