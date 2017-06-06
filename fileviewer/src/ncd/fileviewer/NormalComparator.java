package ncd.fileviewer;

import java.io.File;
import java.util.Comparator;

public class NormalComparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
		if (this.isSameType(o1, o2))
			return o1.getName().compareToIgnoreCase(o2.getName());
		else {
			if (o1.isDirectory()) return -1;
			if (o2.isDirectory()) return 1;
		}
		return 0;
	}

	private boolean isSameType(File file1, File file2) {
		return (file1.isDirectory() && file2.isDirectory()) || (file1.isFile() && file2.isFile());
	}
}
