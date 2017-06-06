package ncd.scan.client;

import java.io.File;

import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.Maths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ncd.data.XyData;
import ncd.gui.ScenePane;
import ncd.gui.beamline.DiagnosticPane;
import ncd.gui.chart.table.DefaultRow;
import ncd.gui.event.ButtonEvent;
import ncd.gui.event.ChartPaneEvent;
import ncd.gui.event.ToggleButtonEvent;
import ncd.scan.client.event.FitMouseEvent;
import ncd.scan.spock.Fit;
import ncd.scan.spock.Result;
import ncd.scan.spock.Spock;
import ncd.scan.spock.Value;
import ncd.scan.utils.Server;
import ncd.utils.file.Metadata;
import ncd.utils.file.MetadataType;
import ncd.utils.file.ScanFile;
import ncd.utils.file.Xml;

public class ScanFitGUI extends Application {

	public final static String	PREFIX				= "CLIENT [" + ScanFitGUI.class.getSimpleName() + "]";

	private PlotPanel			plot;
	private FitPanel			fitDataPanel		= new FitPanel("pos");
	private FitPanel			fitDerivativePanel	= new FitPanel("edge");
	private DiagnosticPane		diagnostic			= new DiagnosticPane();

	@Override
	public void start(Stage primaryStage) throws Exception {
		Server.getInstance(PREFIX);

		File file = new File(".");
		String filename = file.getCanonicalPath();
		System.out.println(PREFIX + " Operating system: " + System.getProperties().get("os.name"));
		System.out.println(PREFIX + " java.version: " + System.getProperties().get("java.version"));
		System.out.println(PREFIX + " javafx.runtime.version: " + System.getProperties().get("javafx.runtime.version"));
		System.out.println(PREFIX + " java bits: " + System.getProperties().get("os.arch")); // if amd64 => 64 bits
		System.out.println(PREFIX + " path directory: " + filename);

		this.plot = new PlotPanel("scan");
		this.plot.getSplitPane().setDividerPositions(0.79);

		this.fitDataPanel.getSplitPane().setDividerPositions(0.55);
		this.fitDerivativePanel.getSplitPane().setDividerPositions(0.55);

		VBox.setVgrow(this.plot.getSplitPane(), Priority.ALWAYS);

		primaryStage.setTitle("Scan");
		Image icon = new Image("file:./icon/scan.png");
		primaryStage.getIcons().add(icon);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
		ScenePane scene = ScenePane.getInstance("Scan", primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());

		SplitPane fit = new SplitPane();
		fit.getItems().addAll(this.fitDataPanel, this.fitDerivativePanel);
		VBox.setVgrow(fitDataPanel.getSplitPane(), Priority.ALWAYS);
		VBox.setVgrow(fitDerivativePanel.getSplitPane(), Priority.ALWAYS);

		SplitPane sp = new SplitPane();
		sp.setOrientation(Orientation.VERTICAL);

		sp.getItems().addAll(this.plot, fit);

		scene.setCentre(sp);

		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event -> closeApplication(event));

		Server.getInstance().scanFileNameProperty().addListener((observable, oldValue, newValue) -> {
			this.plot.writeScanFile(newValue);
			Platform.runLater(() -> loadScan());
		});

		this.plot.addEventHandler(ButtonEvent.OPEN_FILE, event -> fitScan());
		this.plot.addEventHandler(ButtonEvent.LOAD_FILE, event -> loadScan());
		this.plot.addEventHandler(ChartPaneEvent.SELECTION_CHANGED, event -> fitScan());
		this.fitDataPanel.addEventHandler(ToggleButtonEvent.FIT, event -> writeResult());
		this.fitDataPanel.addEventHandler(FitMouseEvent.FIT_PERFORMED, event -> writeResult());
		this.fitDerivativePanel.addEventHandler(ToggleButtonEvent.FIT, event -> writeResult());
		this.fitDerivativePanel.addEventHandler(FitMouseEvent.FIT_PERFORMED, event -> writeResult());

	}

	private void loadScan() {
		this.plot.loadScan();
		fitScan();
	}

	private void fitScan() {
		DefaultRow selectedRow = this.plot.getLegend().getSelectedItem();
		if (selectedRow == null) {
			this.fitDataPanel.clear();
			this.fitDerivativePanel.clear();
			return;
		}

		Metadata metadata = selectedRow.getMetadata();
		int index = Integer.parseInt(metadata.get(MetadataType.CURVE_INDEX));
		XyData data = (XyData) this.plot.getChart().getDataset().get(index);
		this.fitDataPanel.plot(metadata.clone(), data);

		DoubleDataset axis = data.getAxis();
		DoubleDataset y = data.getData();
		DoubleDataset derivative = (DoubleDataset) Maths.derivative(axis, y, 5);
		XyData der = new XyData(axis, derivative);
		this.fitDerivativePanel.plot(metadata.clone(), der);

		this.writeResult();
	}

	private void writeResult() {
		DefaultRow selectedRow = this.plot.getLegend().getSelectedItem();
		Metadata metadata = selectedRow.getMetadata();
		Value min = this.plot.getMinInformation();
		Value max = this.plot.getMaxInformation();
		Fit pos = this.fitDataPanel.getFitparameter();
		Fit edge = this.fitDerivativePanel.getFitparameter();
		String motor = metadata.get(ScanFile.MOTOR);
		String scanID = metadata.get(ScanFile.SCAN_ID);
		Result result = new Result();
		result.update(scanID, motor, min, max, pos, edge);
		Xml<Result> xml = new Xml<Result>(Spock.getInstance().getFileConfiguration().getResults(), result);
		xml.write();
		System.out.println(PREFIX + " Writing fit results in " + xml.getXmlFile().getCanonicalPath());
		// Server.getInstance().send(" RESULT: " + result.getXmlFile().getCanonicalPath());
	}

	private void closeApplication(WindowEvent event) {
		this.plot.exit();
		this.fitDataPanel.writeConfiguration();
		this.fitDerivativePanel.writeConfiguration();
		Platform.exit();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
