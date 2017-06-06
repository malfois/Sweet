package ncd.gui.chart.table;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.configuration.Renderers;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Metadata;
import ncd.utils.file.Xml;

public abstract class AbstractLegend extends VBox {

	private Label								header		= new Label("Legend");
	private Pane								pane;
	private ScrollPane							scrollPane	= new ScrollPane();
	private Pane								option;

	protected ObservableMap<Integer, Renderer>	plotIndexes	= FXCollections.observableHashMap();
	protected DefaultRow						selectedItem;

	protected Xml<Renderers>					rendererConfiguration;

	public abstract void create(List<Metadata> metadata);

	public AbstractLegend(Xml<Renderers> rendererConfiguration) {
		this.initialise("Legend", rendererConfiguration);
	}

	public AbstractLegend(String title, Xml<Renderers> rendererConfiguration) {
		this.initialise(title, rendererConfiguration);
	}

	private void initialise(String title, Xml<Renderers> rendererConfiguration) {
		this.rendererConfiguration = rendererConfiguration;
		this.rendererConfiguration.read();

		String style = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;-fx-alignment: center; -fx-size: 2em;-fx-background-insets: 0, 0 1 1 0, 1 2 2 1;-fx-text-fill: -fx-selection-bar-text;-fx-padding: 0.166667em;";
		header = new Label(title);
		header.setStyle(style);

		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setPrefWidth(415);
		scrollPane.setPrefHeight(455);
		this.getChildren().addAll(header, scrollPane);

		this.setLegendPane(new VBox());
		this.header.prefWidthProperty().bind(this.widthProperty());
	}

	public void setLegendPane(Pane legend) {
		this.pane = legend;
		scrollPane.setContent(this.pane);
		this.pane.prefHeightProperty().bind(scrollPane.heightProperty());
	}

	public void setOptionPane(Pane option) {
		this.option = option;
		this.option.prefWidthProperty().bind(this.widthProperty());
	}

	public Pane getPane() {
		return this.pane;
	}

	public void addNode(Node node) {
		this.pane.getChildren().add(node);
	}

	public void setRendererFile(FileConfiguration rendererFile) {
		this.rendererConfiguration = new Xml<>(rendererFile, new Renderers());
		this.rendererConfiguration.read();
	}

	public Xml<Renderers> getRendererFile() {
		return rendererConfiguration;
	}

	public DefaultRow getSelectedItem() {
		if (this.selectedItem == null) { return null; }
		if (this.selectedItem.isSelected()) { return this.selectedItem; }
		return null;
	}

	public int getSelectedIndex() {
		if (this.selectedItem == null) { return -1; }
		if (this.selectedItem.isSelected()) { return this.selectedItem.getCurveIndex(); }
		return -1;
	}

	public ObservableMap<Integer, Renderer> getPlotIndexes() {
		return this.plotIndexes;
	}

	public void clear() {
		this.pane.getChildren().clear();
		this.plotIndexes.clear();
		this.selectedItem = null;
	}

}
