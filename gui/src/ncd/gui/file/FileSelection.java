package ncd.gui.file;

import java.io.File;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ncd.logger.NcdLogger;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filter;

public class FileSelection {

	private FileChooser									fileChooser	= new FileChooser();

	private Node										owner;
	private String										title;
	private ObservableList<FileChooser.ExtensionFilter>	filters;
	private FileChooser.ExtensionFilter					selectedFilter;

	private File										directory;

	private FileConfiguration							configuration;

	public FileSelection(FileConfiguration configurationFile) {
		this.configuration = (configurationFile == null) ? new FileConfiguration() : configurationFile;
	}

	public FileSelection(FileConfiguration configurationFile, Node owner, String title, ObservableList<FileChooser.ExtensionFilter> filters) {
		this.configuration = (configurationFile == null) ? new FileConfiguration() : configurationFile;

		this.owner = owner;
		this.title = title;
		this.filters = filters;
		this.selectedFilter = this.configuration.getFilter().toFileChooserFilter();
		this.initialiseDirectory();

		fileChooser.setTitle(this.title);
		fileChooser.getExtensionFilters().addAll(this.filters);
		fileChooser.setSelectedExtensionFilter(this.selectedFilter);
		fileChooser.setInitialDirectory(this.directory);
		fileChooser.setInitialFileName(this.configuration.getFilename());
	}

	public FileChooser.ExtensionFilter getSelectedFilter() {
		return selectedFilter;
	}

	public FileConfiguration getConfiguration() {
		return configuration;
	}

	public List<File> openMultiFiles() {
		Stage stage = (Stage) this.owner.getScene().getWindow();
		List<File> files = fileChooser.showOpenMultipleDialog(stage);

		if (files != null && files.size() != 0) {
			this.updateSelectedFilter();
			this.updateDirectory(files.get(0).getAbsolutePath());
			NcdLogger.Info(FileSelection.class.getName(), "openFile", files.size() + " new file(s) selected. ");
		} else {
			NcdLogger.Info(FileSelection.class.getName(), "openFile", "No file selected. ");
		}
		return files;
	}

	public FileConfiguration save() {
		Stage stage = (Stage) this.owner.getScene().getWindow();
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			this.updateSelectedFilter();
			this.updateDirectory(file.getAbsolutePath());
			int index = file.getName().lastIndexOf(".");
			String filename = file.getName().substring(0, index);
			this.configuration.setName(filename);
		}
		return this.configuration;
	}

	public FileConfiguration openSingleFile() {
		Stage stage = (Stage) this.owner.getScene().getWindow();
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			this.updateSelectedFilter();
			this.updateDirectory(file.getAbsolutePath());
			this.configuration.setName(file.getName());
		}
		return this.configuration;
	}

	protected void updateSelectedFilter() {
		this.selectedFilter = fileChooser.getSelectedExtensionFilter();
		this.configuration.setFilter(new Filter(this.selectedFilter));
	}

	protected void initialiseDirectory() {
		this.directory = new File(System.getProperty("user.home"));
		String dirConfig = this.configuration.getDirectory();
		if (dirConfig != null) {
			this.directory = new File(dirConfig);
		}
	}

	protected void updateDirectory(String directory) {
		if (directory == null) {
			this.directory = new File(System.getProperty("user.home"));
			return;
		}

		File file = new File(directory);
		if (file.isDirectory()) {
			this.directory = file;
		} else {
			String d = file.getParent() + File.separator;
			this.directory = new File(d);
		}
		this.configuration.setDirectory(this.directory.getAbsolutePath());
		return;
	}

}
