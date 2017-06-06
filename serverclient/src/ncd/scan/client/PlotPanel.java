package ncd.scan.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.eclipse.january.dataset.DoubleDataset;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Window;
import ncd.data.XyData;
import ncd.gui.chart.ChartPanel;
import ncd.gui.event.ButtonEvent;
import ncd.gui.file.FileSelection;
import ncd.gui.icon.ButtonUtility;
import ncd.gui.icon.FileUtility;
import ncd.scan.spock.Spock;
import ncd.scan.spock.Value;
import ncd.scan.utils.Server;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.AFile;
import ncd.utils.file.Filter;
import ncd.utils.file.Filters;
import ncd.utils.file.Metadata;
import ncd.utils.file.Xml;
import ncd.utils.system.OSUtils;

public class PlotPanel extends ChartPanel {

	private final static String		NAME					= "Data";

	private MinMaxInformationPane	informationPane			= new MinMaxInformationPane("x", "y");
	private ServerIcon				serverIcon				= new ServerIcon();

	private Xml<FileConfiguration>	openFileConfiguration	= new Xml<>(new FileConfiguration("./xml", "open" + NAME + "FileConfiguration", Filter.XML), new FileConfiguration());

	private BooleanProperty			scanFileExist			= new SimpleBooleanProperty(true);

	public PlotPanel(String name) {
		super(name);
		this.initialise();
	}

	public PlotPanel() {
		super(NAME);
		this.initialise();
	}

	private void initialise() {
		this.openFileConfiguration.read();

		this.serverIcon.setState();

		Button configurationButton = ButtonUtility.getButton(FileUtility.CONFIGURATION);
		Button newFile = ButtonUtility.getButton(FileUtility.OPEN_FILE);
		Button loadScan = ButtonUtility.getButton(FileUtility.LOAD_FILE);
		loadScan.getTooltip().setText("Upload the last scan");

		ObservableList<Node> toolBar = FXCollections.observableArrayList();
		toolBar.addAll(configurationButton, newFile, loadScan, new Separator(Orientation.VERTICAL));

		HBox hbox = new HBox();
		HBox.setHgrow(hbox, Priority.ALWAYS);

		this.getToolBar().getItems().addAll(0, toolBar);
		this.getToolBar().getItems().addAll(hbox, serverIcon);

		super.setInformationPanel(this.informationPane);

		newFile.setOnAction(event -> openFile());
		loadScan.setOnAction(event -> loadScan());
		configurationButton.setOnAction(event -> changeConfiguration());

		if (!OSUtils.pathExists(Spock.getInstance().getFileConfiguration().getScan().getCanonicalPath())) {
			System.out.println(ScanFitGUI.PREFIX + " Load button disabled");
			this.scanFileExist.set(false);
			loadScan.setDisable(true);
		}

		this.scanFileExist.addListener((observable, oldValue, newValue) -> loadScan.setDisable(!newValue));

		Server.getInstance().scanFileNameProperty().addListener((observable, oldValue, newValue) -> {
			writeScanFile(newValue);
			Platform.runLater(() -> loadScan());
		});

		Server.getInstance().diagnosticProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> informationPane.setName(newValue)));

	}

	private void changeConfiguration() {
		ConfigurationDialog dialog = new ConfigurationDialog("Dialog");
		Window window = this.getScene().getWindow();

		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(window);
		dialog.showAndWait();

		FileConfiguration scan = Spock.getInstance().getFileConfiguration().getScan();
		this.scanFileExist.set(scan.exists() && !scan.isEmpty());
	}

	public void loadScan() {
		AFile file = readScanFile();
		if (file == null) {
			this.scanFileExist.set(false);
			return;
		}
		Metadata metadata = file.getMetadata();
		metadata.setFirstSelected(Server.getInstance().getDiagnostic());
		this.plot(file);
		this.scanFileExist.set(true);
	}

	public void writeScanFile(String filename) {
		String path = Spock.getInstance().getFileConfiguration().getScan().getCanonicalPath();
		System.out.println(ScanFitGUI.PREFIX + " Writting scan file name [" + filename + "] in " + path);
		File file = new File(path);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(filename);
			// Close writer
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void openFile() {
		FileSelection fileSelection = new FileSelection(this.openFileConfiguration.getConfiguration(), this, "Open Files", Filters.ScanData());
		List<File> files = fileSelection.openMultiFiles();
		if (files == null || files.size() == 0) return;

		List<XyData> data = new ArrayList<>();
		List<Metadata> metadata = new ArrayList<>();

		Boolean selected = false;
		Filter selectedFilter = new Filter(fileSelection.getSelectedFilter());
		for (File file : files) {
			AFile f = AFile.Factory(selectedFilter, file.getAbsolutePath());
			f.readData();
			if (!selected) {
				Metadata mdata = f.getMetadata();
				selected = mdata.setFirstSelected(Server.getInstance().getDiagnostic());
			}
			data.addAll((Collection<? extends XyData>) f.getData());

			metadata.add(f.getMetadata());
		}
		this.plot(metadata, data);
		this.updateInformationPane();

		this.openFileConfiguration.setConfiguration(fileSelection.getConfiguration());

		this.fireEvent(new ButtonEvent(ButtonEvent.OPEN_FILE));
	}

	@SuppressWarnings("unchecked")
	public void plot(AFile file) {
		List<XyData> data = new ArrayList<>();
		List<Metadata> metadata = new ArrayList<>();

		data.addAll((Collection<? extends XyData>) file.getData());
		metadata.add(file.getMetadata());

		plot(metadata, data);

		this.updateInformationPane();
	}

	private void updateInformationPane() {
		int index = this.getLegend().getSelectedIndex();
		if (index < 0) return;
		XyData d = this.getChart().getDataset().get(index);
		DoubleDataset x = d.getAxis();
		DoubleDataset y = d.getData();
		Value min = new Value(x.get(y.argMin()), y.min(true).doubleValue());
		Value max = new Value(x.get(y.argMax()), y.max(true).doubleValue());
		this.informationPane.setValue(min, max);
	}

	public Value getMinInformation() {
		return this.informationPane.getMinInformation();
	}

	public Value getMaxInformation() {
		return this.informationPane.getMaxInformation();
	}

	public void exit() {
		super.writeConfiguration();
		this.openFileConfiguration.write();
	}

	private AFile readScanFile() {
		String path = Spock.getInstance().getFileConfiguration().getScan().getCanonicalPath();
		try {
			File file = new File(OSUtils.getAbsolutePath(path));
			if (!file.exists()) return null;
			BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String filename = br.readLine();
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			System.out.println(ScanFitGUI.PREFIX + " File read: " + filename + " at " + sdf.format(cal.getTime()));
			br.close();
			filename = OSUtils.getAbsolutePath(filename);
			AFile aFile = AFile.Factory(Filter.SCAN_DATA, filename);
			aFile.readData();
			return aFile;
		} catch (FileNotFoundException e) {
			System.out.println(ScanFitGUI.PREFIX + " File [" + path + "] not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
