package ncd.utils.file;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class Filters {

	public static ObservableList<FileChooser.ExtensionFilter> Image() {
		ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
		filters.addAll(Filter.JPG.toFileChooserFilter(), Filter.PNG.toFileChooserFilter());
		return filters;
	}

	public static ObservableList<FileChooser.ExtensionFilter> Xml() {
		ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
		filters.addAll(Filter.XML.toFileChooserFilter());
		return filters;
	}

	public static ObservableList<FileChooser.ExtensionFilter> ScanData() {
		ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
		filters.add(Filter.SCAN_DATA.toFileChooserFilter());
		return filters;
	}

	public static ObservableList<FileChooser.ExtensionFilter> Ascii() {
		ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
		filters.add(Filter.ASCII_TAB.toFileChooserFilter());
		filters.add(Filter.ASCII_COMMA.toFileChooserFilter());
		filters.add(Filter.ASCII_SPACE.toFileChooserFilter());
		return filters;
	}

	public static Filter[] toFilters(ObservableList<FileChooser.ExtensionFilter> filters) {
		int n = filters.size();
		Filter[] fs = new Filter[n];
		for (int i = 0; i < n; i++) {
			fs[i] = new Filter(filters.get(i));
		}
		return fs;
	}
}
