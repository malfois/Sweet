package ncd.gui.chart;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Window;
import ncd.data.XyData;
import ncd.gui.chart.configuration.Plot;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.configuration.Renderers;
import ncd.gui.chart.renderer.DialogParameter;
import ncd.gui.chart.renderer.MetadataRenderer;
import ncd.gui.chart.renderer.RendererDialog;
import ncd.gui.chart.table.DefaultLegend;
import ncd.gui.chart.table.DefaultRow;
import ncd.gui.chart.table.MetadataItem;
import ncd.gui.event.ActionOnAllPlotEvent;
import ncd.gui.event.ButtonEvent;
import ncd.gui.event.ChartPaneEvent;
import ncd.gui.event.ContextMenuEvent;
import ncd.gui.event.LegendEvent;
import ncd.gui.event.MousePositionEvent;
import ncd.gui.event.ToggleButtonEvent;
import ncd.gui.file.FileSelection;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.AFile;
import ncd.utils.file.AsciiFile;
import ncd.utils.file.Filter;
import ncd.utils.file.Filters;
import ncd.utils.file.Metadata;
import ncd.utils.file.Xml;
import ncd.utils.plot.GraphicScale;

public class ChartPanel extends VBox {

	private HBox					title		= new HBox(0);
	private DefaultToolBar			toolBar		= new DefaultToolBar();
	private Chart					chart;
	private DefaultLegend			legend;
	private SimpleInformationPanel	information;
	private DefaultActionPane		actionPane	= new DefaultActionPane();

	private VBox					leftPane	= new VBox(0);
	private SplitPane				splitPane	= new SplitPane();

	private Xml<Plot>				configuration;

	@SuppressWarnings("unchecked")
	public ChartPanel(String name) {
		setAlignment(Pos.CENTER);

		FileConfiguration fileConfiguration = new FileConfiguration("./xml", name + "Plot", Filter.XML);
		this.configuration = new Xml<>(fileConfiguration, new Plot(name));
		this.configuration.read();

		this.legend = new DefaultLegend(new Xml<Renderers>(this.configuration.getConfiguration().getRendererFile(), new Renderers()));

		this.chart = new Chart();
		this.chart.setGraphicScale(this.configuration.getConfiguration().getGraphicScale());
		this.information = new SimpleInformationPanel("x", "y");

		VBox.setVgrow(this.chart, Priority.ALWAYS);
		VBox.setVgrow(this.actionPane, Priority.NEVER);
		// VBox.setVgrow(this.title, Priority.NEVER);

		Text text = new Text(name);
		text.setTextAlignment(TextAlignment.CENTER);
		Font defaultFont = Font.getDefault();
		Font font = Font.font(defaultFont.getName(), FontWeight.BOLD, FontPosture.ITALIC, 16);
		text.setFont(font);
		this.title.setAlignment(Pos.CENTER);
		this.title.getChildren().add(text);

		leftPane.setPadding(new Insets(3, 3, 3, 3));
		leftPane.getChildren().addAll(this.toolBar, this.chart, this.information);

		VBox rightPane = new VBox(0);
		rightPane.setPadding(new Insets(3, 3, 3, 3));
		rightPane.getChildren().addAll(this.legend, this.actionPane);

		splitPane.getItems().addAll(leftPane, rightPane);

		getChildren().addAll(title, splitPane);

		this.chart.selectedCurveProperty().addListener(new ChangeListener<XyData>() {

			@Override
			public void changed(ObservableValue<? extends XyData> observable, XyData oldValue, XyData newValue) {
				String name = null;
				if (newValue != null) {
					name = legend.getSelectedItem().getMetadata().get(AsciiFile.Y_NAME);
				}
				information.setName(name);
			}
		});

		this.legend.getPlotIndexes().addListener(new MapChangeListener<Integer, Renderer>() {

			public void onChanged(MapChangeListener.Change<? extends Integer, ? extends Renderer> arg0) {
				int nCurves = chart.getNumberOfCurves();
				boolean disableClear = false;
				boolean disablePlot = false;
				boolean disableConfiguration = false;
				if (nCurves != 0 && legend.getPlotIndexes().size() == nCurves) {
					disablePlot = true;
				}
				if (nCurves != 0 && legend.getPlotIndexes().size() == 0) {
					disableClear = true;
				}
				if (nCurves == 0) {
					disableConfiguration = true;
				}
				actionPane.getClear().setDisable(disableClear);
				actionPane.getPlot().setDisable(disablePlot);
				actionPane.getConfiguration().setDisable(disableConfiguration);
			}
		});

		this.chart.addEventHandler(MousePositionEvent.MOUSE_MOVED, event -> updateInformation(event));

		this.toolBar.addEventHandler(ToggleButtonEvent.GRAPHIC_SCALE, event -> viewChanged());
		this.toolBar.addEventHandler(ButtonEvent.SAVE_IMAGE, event -> saveImage());
		this.toolBar.addEventHandler(ButtonEvent.PRINT, event -> print());

		this.legend.addEventHandler(LegendEvent.SELECTION_CHANGED, event -> updateSelection());
		this.legend.addEventFilter(LegendEvent.PLOT_CHANGED, event -> updatePlot());
		this.legend.addEventFilter(LegendEvent.PLOT_AND_SELECTION_CHANGED, event -> plotAndSelectionChanged());
		this.legend.addEventHandler(ContextMenuEvent.RENDERER_ACTION, event -> rendererAction());
		this.legend.addEventHandler(ContextMenuEvent.SAVE_DATA, event -> saveData((ContextMenuEvent<DefaultRow>) event));

		this.actionPane.addEventHandler(ActionOnAllPlotEvent.CLEAR_ALL, event -> clearAll());
		this.actionPane.addEventHandler(ActionOnAllPlotEvent.PLOT_ALL, event -> plotAll());
		this.actionPane.addEventHandler(ContextMenuEvent.RENDERER_ACTION, event -> rendererAction());

		this.toolBar.addEventFilter(ButtonEvent.UNZOOM, event -> unzoom());
	}

	public SplitPane getSplitPane() {
		return this.splitPane;
	}

	public HBox getTitle() {
		return this.title;
	}

	public void clear() {
		this.chart.clear();
		this.legend.clear();
		this.information.clear();
	}

	public void plot(List<Metadata> metadata, List<XyData> data) {
		this.chart.setDataset(data);
		this.legend.create(metadata);
		this.replot();
	}

	public void add(Metadata metadata, List<XyData> data) {
		this.chart.addDataset(data);
		this.legend.add(metadata);
	}

	public void replot() {
		this.chart.setSelectedCurve(this.legend.getSelectedIndex());
		this.chart.replot(this.legend.getPlotIndexes());
	}

	public void setInformationPanel(SimpleInformationPanel panel) {
		this.leftPane.getChildren().remove(this.information);
		this.information = panel;
		this.leftPane.getChildren().add(this.information);
	}

	public int getNumberOfCurves() {
		return this.chart.getDataset().size();
	}

	private void clearAll() {
		int index = this.legend.clearAll();
		this.chart.setSelectedCurve(index);
		this.chart.clear();
		ChartPaneEvent event = new ChartPaneEvent(ChartPaneEvent.SELECTION_CHANGED);
		this.fireEvent(event);
	}

	private void plotAll() {
		this.legend.plotAll();
		this.chart.replot(this.legend.getPlotIndexes());
	}

	private void rendererAction() {
		List<MetadataRenderer> renderers = this.legend.getDialogRendererParameter();
		DialogParameter parameter = new DialogParameter(this.configuration.getConfiguration().getRendererFile(), renderers);
		Window window = this.getScene().getWindow();
		RendererDialog dialog = new RendererDialog("Plot configuration", parameter);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(window);
		Optional<DialogParameter> result = dialog.showAndWait();
		if (!result.isPresent()) return;

		parameter = result.get();
		FileConfiguration rendererFile = parameter.getFileConfiguration();
		this.configuration.getConfiguration().setRendererFile(rendererFile);
		Xml<Renderers> xmlConf = new Xml<>(rendererFile, new Renderers());
		xmlConf.read();
		Renderers conf = xmlConf.getConfiguration();

		this.legend.getPlotIndexes().clear();
		ObservableList<Node> nodes = this.legend.getPane().getChildren();
		for (Node node : nodes) {
			MetadataItem item = (MetadataItem) node;
			item.initialiseRenderer(conf);
			item.initialisePlotIndexes(this.legend.getPlotIndexes());
		}

		this.updatePlot();
	}

	private void plotAndSelectionChanged() {
		this.updateSelection();
		this.updatePlot();
	}

	private void updateSelection() {
		Integer index = this.legend.getSelectedIndex();
		this.chart.setSelectedCurve(index);
		ChartPaneEvent event = new ChartPaneEvent(ChartPaneEvent.SELECTION_CHANGED);
		this.fireEvent(event);
	}

	private void updatePlot() {
		this.chart.replot(this.legend.getPlotIndexes());
	}

	private void viewChanged() {
		GraphicScale scale = this.toolBar.getGraphicScale();
		this.chart.setGraphicScale(scale);
		this.updatePlot();
		this.configuration.getConfiguration().setScale(scale.toString());
	}

	private void saveData(ContextMenuEvent<DefaultRow> event) {
		DefaultRow row = event.getResult();
		FileConfiguration configurationFile = this.configuration.getConfiguration().getOutputDataFile();
		if (configurationFile == null) {
			configurationFile = new FileConfiguration("data", Filter.ASCII_TAB);
		}
		configurationFile.setName(row.getTitle());
		FileSelection fileSelection = new FileSelection(configurationFile, this, "Save Data", Filters.Ascii());
		FileConfiguration fileConfiguration = fileSelection.save();

		AFile file = (AsciiFile) AFile.Factory(fileConfiguration.getFilter(), fileConfiguration.getCanonicalPath());
		Metadata metadata = new Metadata(row.getHeaders());
		int index = row.getCurveIndex();
		file.writeData(metadata, this.chart.getDataset().get(index));
		this.configuration.getConfiguration().setOutputDataFile(fileConfiguration);
	}

	private void saveImage() {
		FileConfiguration configurationFile = this.configuration.getConfiguration().getImageFile();
		FileSelection fileSelection = new FileSelection(configurationFile, this, "Save Image", Filters.Image());
		FileConfiguration fileConfiguration = fileSelection.save();
		File file = new File(fileConfiguration.getCanonicalPath());

		if (file != null) {
			WritableImage image = this.snapshot(null, null);
			try {
				Filter filter = new Filter(fileSelection.getSelectedFilter());
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), filter.getDescription().toLowerCase(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void writeConfiguration() {
		this.configuration.write();
	}

	private void print() {
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job.showPrintDialog(this.getScene().getWindow()) && job.printPage(this)) job.endJob();
	}

	private void unzoom() {
		this.chart.unzoom();
	}

	public Chart getChart() {
		return chart;
	}

	public DefaultLegend getLegend() {
		return this.legend;
	}

	public ToolBar getToolBar() {
		return this.toolBar;
	}

	public SimpleInformationPanel getInformationPane() {
		return this.information;
	}

	private void updateInformation(MousePositionEvent event) {
		Point2D point = event.getPoint();
		if (point == null) {
			this.information.clearPosition();
			return;
		}

		this.information.setXValue(point.getX());
		this.information.setYValue(point.getY());
	}

}
