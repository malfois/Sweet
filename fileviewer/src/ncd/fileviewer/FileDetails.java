package ncd.fileviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileDetails {

	private File			file;
	private StringProperty	name	= new SimpleStringProperty();
	private StringProperty	date	= new SimpleStringProperty();
	private StringProperty	type	= new SimpleStringProperty();
	private StringProperty	size	= new SimpleStringProperty();

	public FileDetails(File file) throws IOException {
		this.file = file;

		this.name.set(file.getName());
		this.date.set(new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date(file.lastModified())));
		this.type.set(Files.probeContentType(file.toPath()));

		if (file.isFile()) {
			double fileSize = file.length() / 1024;
			this.size.set(fileSize + " KB");
		} else {
			File[] files = file.listFiles();
			if (files != null) {
				this.size.set(files.length + " items");
			}
		}

	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getName() {
		return this.name.get();
	}

	public StringProperty dateProperty() {
		return date;
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public String getDate() {
		return date.get();
	}

	public StringProperty typeProperty() {
		return type;
	}

	public void setType(String type) {
		this.type.set(type);
	}

	public String getType() {
		return type.get();
	}

	public StringProperty sizeProperty() {
		return size;
	}

	public void setSize(String size) {
		this.size.set(size);
	}

	public String getSize() {
		return size.get();
	}

	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return "FileDetails [file=" + file + ", name=" + name + ", date=" + date + ", type=" + type + ", size=" + size + "]";
	}

}
