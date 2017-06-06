package ncd.utils.file;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

@XmlRootElement(namespace = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "description", "extension" })
public class Filter {

	public final static Filter	JPG			= new Filter("JPG", "*.jpg");
	public final static Filter	PNG			= new Filter("PNG", "*.png");

	public final static Filter	SCAN_DATA	= new Filter("Scan data", "*.dat");

	public final static Filter	ASCII_TAB	= new Filter("Ascii (Tab delimited)", "*.dat");
	public final static Filter	ASCII_COMMA	= new Filter("Ascii (Comma delimited)", "*.dat");
	public final static Filter	ASCII_SPACE	= new Filter("Ascii (Space delimited)", "*.dat");

	public final static Filter	XML			= new Filter("xml", "*.xml");

	private String				description;
	private String				extension;

	public Filter() {
	}

	public Filter(String description, String extension) {
		this.description = description;
		this.extension = extension;
	}

	public Filter(FileChooser.ExtensionFilter filter) {
		this.description = filter.getDescription();
		this.extension = filter.getExtensions().get(0);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFileExtension() {
		int index = this.extension.indexOf(".");
		return this.extension.substring(index);
	}

	public FileChooser.ExtensionFilter toFileChooserFilter() {
		return new FileChooser.ExtensionFilter(this.description, this.extension);
	}

	public FileChooser.ExtensionFilter getFileChooserFilter(ObservableList<FileChooser.ExtensionFilter> filters) {
		for (FileChooser.ExtensionFilter f : filters) {
			if (f.getDescription().equals(this.description) && f.getExtensions().get(0).equals(this.extension)) {
				return f;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((extension == null) ? 0 : extension.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Filter other = (Filter) obj;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (extension == null) {
			if (other.extension != null) return false;
		} else if (!extension.equals(other.extension)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Filter [description=" + description + ", extension=" + extension + "]";
	}
}
