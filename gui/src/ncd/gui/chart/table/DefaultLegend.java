package ncd.gui.chart.table;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.util.Pair;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.configuration.Renderers;
import ncd.gui.chart.renderer.MetadataRenderer;
import ncd.gui.event.LegendEvent;
import ncd.gui.event.RowEvent;
import ncd.logger.NcdLogger;
import ncd.utils.file.Metadata;
import ncd.utils.file.Xml;

public class DefaultLegend extends AbstractLegend {

	public final static double[]	widths			= new double[] { 10, 25, 250, 40 };

	private int						numberOfCurves	= 0;

	public DefaultLegend(Xml<Renderers> rendererConfiguration) {
		super(rendererConfiguration);
	}

	@Override
	public void create(List<Metadata> metadataList) {
		this.clear();
		this.numberOfCurves = 0;
		for (Metadata metadata : metadataList) {
			this.add(metadata);
		}
	}

	@SuppressWarnings("unchecked")
	public void add(Metadata metadata) {
		MetadataItem item = new MetadataItem(metadata);
		this.getPane().getChildren().add(item);

		this.numberOfCurves = item.initialiseCurveIndex(this.numberOfCurves);
		if (this.selectedItem == null) {
			this.selectedItem = item.initialiseSelection();
		}

		// if (conf == null) {
		// conf = new ListOfRenderersConfiguration();
		// }

		item.initialiseRenderer(this.rendererConfiguration.getConfiguration());
		item.initialisePlotCheckBox();
		item.initialisePlotIndexes(this.getPlotIndexes());

		item.addEventHandler(RowEvent.SELECTION_MODIFIED, event -> selectionChanged((RowEvent<DefaultRow>) event));
		item.addEventHandler(RowEvent.CURVE_PLOT_ADDED, event -> curvePlotAdded((RowEvent<DefaultRow>) event));
		item.addEventHandler(RowEvent.CURVE_PLOT_REMOVED, event -> curvePlotRemoved((RowEvent<DefaultRow>) event));
		item.addEventHandler(RowEvent.DATA_PLOT_ADDED, event -> allDataCurvesAdded((RowEvent<MetadataItem>) event));
		item.addEventHandler(RowEvent.DATA_PLOT_REMOVED, event -> allDataCurvesRemoved((RowEvent<MetadataItem>) event));
	}

	public List<Metadata> getMetadata() {
		List<Metadata> metadata = new ArrayList<>();
		ObservableList<Node> nodes = this.getPane().getChildren();
		for (Node node : nodes) {
			MetadataItem item = (MetadataItem) node;
			metadata.add(item.getMetadata());
		}
		return metadata;
	}

	public List<MetadataRenderer> getDialogRendererParameter() {
		List<MetadataRenderer> metadataList = new ArrayList<>();
		ObservableList<Node> nodes = this.getPane().getChildren();
		for (Node node : nodes) {
			MetadataItem item = (MetadataItem) node;
			metadataList.add(item.getMetadataRenderer());
		}
		return metadataList;
	}

	private void allDataCurvesRemoved(RowEvent<MetadataItem> event) {
		MetadataItem item = event.getResult();
		event.consume();
		EventType<?> eventType = LegendEvent.PLOT_CHANGED;
		if (item.isChildSelected()) {
			this.removeSelection();
			eventType = LegendEvent.PLOT_AND_SELECTION_CHANGED;
		}
		item.updatePlotIndexes(this.getPlotIndexes());
		NcdLogger.Info(this.getClass().getSimpleName(), "allDataCurvesRemoved", "All data from " + item.getTitle() + " removed from the plot");

		LegendEvent e = new LegendEvent(eventType);
		this.fireEvent(e);

	}

	private void allDataCurvesAdded(RowEvent<MetadataItem> event) {
		MetadataItem item = event.getResult();
		event.consume();
		EventType<?> eventType = LegendEvent.PLOT_CHANGED;

		item.initialisePlotIndexes(this.getPlotIndexes());

		NcdLogger.Info(this.getClass().getSimpleName(), "allDataCurvesAdded", "All data from " + item.getTitle() + " added to the plot");

		LegendEvent e = new LegendEvent(eventType);
		this.fireEvent(e);

	}

	private void curvePlotRemoved(RowEvent<DefaultRow> event) {
		DefaultRow item = event.getResult();
		event.consume();
		EventType<?> eventType = LegendEvent.PLOT_CHANGED;
		if (item.isSelected()) {
			this.removeSelection();
			eventType = LegendEvent.PLOT_AND_SELECTION_CHANGED;
		}

		this.plotIndexes.remove(item.getCurveIndex());
		this.selectedItem.getRoot().initialisePlotCheckBox();

		NcdLogger.Info(this.getClass().getSimpleName(), "CurvePlotRemoved", "Curve " + item.getTitle() + " removed from plot");

		LegendEvent e = new LegendEvent(eventType);
		this.fireEvent(e);

	}

	private void curvePlotAdded(RowEvent<DefaultRow> event) {
		DefaultRow item = event.getResult();
		event.consume();
		EventType<?> eventType = LegendEvent.PLOT_CHANGED;
		if (!item.isSelected()) {
			this.removeSelection();
			item.setSelected(true);
			item.upgradeBackground();
			this.selectedItem = item;
			eventType = LegendEvent.PLOT_AND_SELECTION_CHANGED;
		}
		Pair<Integer, Renderer> pair = this.selectedItem.getPlotIndex();
		this.plotIndexes.put(pair.getKey(), pair.getValue());

		this.selectedItem.getRoot().initialisePlotCheckBox();

		NcdLogger.Info(this.getClass().getSimpleName(), "CurvePlotAdded", "Curve " + item.getTitle() + " plotted");

		LegendEvent e = new LegendEvent(eventType);
		this.fireEvent(e);

	}

	private void selectionChanged(RowEvent<DefaultRow> event) {
		this.removeSelection();
		DefaultRow item = event.getResult();
		event.consume();
		item.setSelected(true);
		item.upgradeBackground();
		this.selectedItem = item;
		EventType<?> eventType = LegendEvent.SELECTION_CHANGED;
		if (!this.selectedItem.getPlot()) {
			this.selectedItem.setPlot(true);
			Pair<Integer, Renderer> pair = this.selectedItem.getPlotIndex();
			this.plotIndexes.put(pair.getKey(), pair.getValue());
			eventType = LegendEvent.PLOT_AND_SELECTION_CHANGED;
			this.selectedItem.getRoot().initialisePlotCheckBox();
		}
		NcdLogger.Info(this.getClass().getSimpleName(), "SelectionChanged", "Curve " + item.getTitle() + " selected");
		LegendEvent e = new LegendEvent(eventType);
		this.fireEvent(e);
	}

	private void removeSelection() {
		if (this.getSelectedIndex() < 0) { return; }
		this.selectedItem.setSelected(false);
		this.selectedItem.upgradeBackground();
		this.plotIndexes.remove(this.getSelectedIndex());
	}

	public int clearAll() {
		ObservableList<Node> nodes = this.getPane().getChildren();
		for (Node node : nodes) {
			MetadataItem item = (MetadataItem) node;
			item.getTitleNode().setPlot(false);
			item.initialiseChildrenPlotCheckBox(false);
			if (item.isChildSelected()) {
				this.removeSelection();
			}

			this.plotIndexes.clear();
		}
		return this.getSelectedIndex();
	}

	public void plotAll() {
		ObservableList<Node> nodes = this.getPane().getChildren();
		for (Node node : nodes) {
			MetadataItem item = (MetadataItem) node;
			item.getTitleNode().setPlot(true);
			item.initialiseChildrenPlotCheckBox(true);
			item.initialisePlotIndexes(this.getPlotIndexes());
		}
	}

}
