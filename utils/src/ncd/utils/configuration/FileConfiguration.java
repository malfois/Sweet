package ncd.utils.configuration;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import ncd.utils.file.Filter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "directory", "filename", "selectedFilter" })
public class FileConfiguration {

	private String directory;
	private String filename;
	private Filter selectedFilter;

	public FileConfiguration() {
	}

	public FileConfiguration(String directory, String filename, Filter selectedFilter) {
		this.directory = directory;
		this.filename = filename;
		this.selectedFilter = selectedFilter;
	}

	public static FileConfiguration Default(Filter selectedFilter) {
		return new FileConfiguration(System.getProperty("user.home"), "", selectedFilter);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public Filter getSelectedFilter() {
		return selectedFilter;
	}

	public void setSelectedFilter(Filter selectedFilter) {
		this.selectedFilter = selectedFilter;
	}

	public String getPath() {
		if (this.directory == null || filename == null) {
			return null;
		}
		if (this.directory.endsWith(File.pathSeparator)) {
			String path = this.directory + this.filename;
			return path;
		}
		return this.directory + File.separator + this.filename;
	}

	@Override
	public String toString() {
		return "FileConfiguration [directory=" + directory + ", filename=" + filename + ", selectedFilter="
				+ selectedFilter + "]";
	}

}
