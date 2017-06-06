package ncd.gui.chart.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.configuration.Renderers;
import ncd.gui.chart.renderer.MetadataRenderer;
import ncd.gui.event.RowEvent;
import ncd.utils.file.AsciiFile;
import ncd.utils.file.Metadata;
import ncd.utils.file.MetadataType;

public class MetadataItem extends VBox {

	private DefaultRow			title;
	private List<MetadataItem>	treeItems;

	public MetadataItem(Metadata metadata) {
		this.initialise(0, metadata);
	}

	public MetadataItem(int space, Metadata metadata) {
		this.initialise(space, metadata);
	}

	private void initialise(int space, Metadata metadata) {
		this.treeItems = new ArrayList<>();
		if (metadata.getChildren().isEmpty()) {
			this.createCurveRow(metadata);
		} else {
			this.createTitleRow(metadata);
			this.title.setTranslateX(space);
			space += 10;
			for (Metadata mdata : metadata.getChildren()) {
				MetadataItem item = new MetadataItem(space, mdata);
				item.setTranslateX(space);
				treeItems.add(item);
			}
			this.getChildren().addAll(this.treeItems);
		}
	}

	private void createCurveRow(Metadata metadata) {
		this.title = new DefaultRow(metadata.getHeaders());
		this.title.setWidths(DefaultLegend.widths);
		this.title.setSelectable();

		this.getChildren().add(title);
	}

	@SuppressWarnings("unchecked")
	private void createTitleRow(Metadata metadata) {
		this.title = new DefaultRow(metadata.getHeaders());
		this.title.setWidths(DefaultLegend.widths);
		this.getChildren().add(title);

		this.title.addEventHandler(RowEvent.DATA_PLOT_CLICKED, event -> dataPlotChanged((RowEvent<DefaultRow>) event));
		this.title.addEventHandler(RowEvent.VISIBILITY_CHANGED, event -> visibilityChanged((RowEvent<DefaultRow>) event));
	}

	private void visibilityChanged(RowEvent<DefaultRow> event) {
		DefaultRow row = event.getResult();
		event.consume();
		if (row.isExpanded()) {
			this.getChildren().addAll(this.treeItems);
			if (this.title.isSelected()) {
				this.title.setSelected(false);
				this.title.upgradeBackground();
			}
		} else {
			this.getChildren().removeAll(this.treeItems);
			if (this.isChildSelected()) {
				this.title.setSelected(true);
				this.title.upgradeBackground();
			}

		}
	}

	private void dataPlotChanged(RowEvent<DefaultRow> event) {
		EventType<?> eventType = RowEvent.DATA_PLOT_REMOVED;
		event.consume();
		DefaultRow row = event.getResult();
		Boolean plot = row.getPlot();
		if (plot) {
			eventType = RowEvent.DATA_PLOT_ADDED;
		}
		this.initialiseChildrenPlotCheckBox(row.getPlot());
		this.initialiseParentPlotCheckBox(row.getPlot());
		RowEvent<MetadataItem> e = new RowEvent<>(eventType, this.getTitleNode().getRoot());
		this.fireEvent(e);
	}

	public void initialiseRenderer(Renderers configuration) {
		if (this.treeItems.isEmpty()) {
			Integer index = Integer.parseInt(this.title.getHeaders().get(MetadataType.CURVE_INDEX));
			this.title.setRenderer(configuration.getRenderer(index));
		}

		for (MetadataItem item : this.treeItems) {
			item.initialiseRenderer(configuration);
		}

	}

	public Boolean isChildSelected() {
		if (this.treeItems.isEmpty()) { return Boolean.parseBoolean(this.title.getHeaders().get(MetadataType.SELECTED)); }

		for (MetadataItem item : this.treeItems) {
			Boolean selected = item.isChildSelected();
			if (selected) { return selected; }
		}
		return false;
	}

	public int initialisePlotCheckBox() {
		return this.initialisePlotCheckBox(0);
	}

	public int initialisePlotCheckBox(int nPlot) {
		if (this.treeItems.isEmpty()) {
			Boolean plot = Boolean.parseBoolean(this.title.getHeaders().get(AsciiFile.PLOT));
			if (plot) {
				nPlot++;
			}
			return nPlot;
		} else {
			int iPlot = 0;
			for (MetadataItem item : this.treeItems) {
				iPlot = item.initialisePlotCheckBox(iPlot);
			}
			int nCurve = Integer.parseInt(this.title.getHeaders().get(AsciiFile.NUMBER_OF_CURVES));
			if (iPlot == nCurve) {
				this.title.setPlot(true, false);
			}
			if (iPlot == 0) {
				this.title.setPlot(false, false);
			}
			if (iPlot > 0 && iPlot < nCurve) {
				this.title.setPlot(false, true);
			}
			nPlot += iPlot;
			return nPlot;
		}
	}

	public void initialiseChildrenPlotCheckBox(Boolean plot) {
		if (!this.treeItems.isEmpty()) {
			for (MetadataItem item : this.treeItems) {
				item.getTitleNode().setPlot(plot);
				item.initialiseChildrenPlotCheckBox(plot);
			}
		}
	}

	public void initialiseParentPlotCheckBox(Boolean plot) {
		MetadataItem root = this.title.getRoot();
		if (this.equals(root)) { return; }
		Parent parent = this.getParent();
		while (!parent.equals(root)) {
			if (parent instanceof MetadataItem) {
				MetadataItem item = (MetadataItem) parent;
				item.getTitleNode().setPlot(plot);
			}
			parent = parent.getParent();
		}
		MetadataItem item = (MetadataItem) parent;
		item.getTitleNode().setPlot(plot);
	}

	public void initialisePlotIndexes(Map<Integer, Renderer> indexes) {
		if (this.treeItems.isEmpty()) {
			Boolean plot = Boolean.parseBoolean(this.title.getHeaders().get(AsciiFile.PLOT));
			if (plot) {
				int index = Integer.parseInt(this.title.getHeaders().get(MetadataType.CURVE_INDEX));
				Renderer renderer = this.title.getRenderer();
				indexes.put(index, renderer);
			}
			return;
		}

		for (MetadataItem item : this.treeItems) {
			item.initialisePlotIndexes(indexes);
		}
	}

	public void updatePlotIndexes(Map<Integer, Renderer> indexes) {
		if (this.treeItems.isEmpty()) {
			Boolean plot = Boolean.parseBoolean(this.title.getHeaders().get(AsciiFile.PLOT));
			int index = Integer.parseInt(this.title.getHeaders().get(MetadataType.CURVE_INDEX));
			if (plot) {
				Renderer renderer = this.title.getRenderer();
				indexes.put(index, renderer);
			} else {
				indexes.remove(index);
			}
			return;
		}

		for (MetadataItem item : this.treeItems) {
			item.updatePlotIndexes(indexes);
		}
	}

	public DefaultRow initialiseSelection() {
		if (!this.treeItems.isEmpty()) {
			for (MetadataItem item : this.treeItems) {
				DefaultRow row = item.initialiseSelection();
				if (row != null) { return row; }
			}
		} else {
			Boolean selected = Boolean.valueOf(this.title.getHeaders().get(MetadataType.SELECTED));
			if (selected) {
				this.title.upgradeBackground();
				return this.title;
			}
		}
		return null;
	}

	public int initialiseCurveIndex(int index) {
		if (!this.treeItems.isEmpty()) {
			for (MetadataItem item : this.treeItems) {
				index = item.initialiseCurveIndex(index);
			}
		} else {
			this.title.getHeaders().put(MetadataType.CURVE_INDEX, String.valueOf(index));
			this.title.setLeaf();
			return index + 1;
		}
		return index;
	}

	public Metadata getMetadata() {
		Metadata metadata = new Metadata(this.title.getHeaders());
		if (!this.treeItems.isEmpty()) {
			for (MetadataItem item : this.treeItems) {
				Metadata mdata = item.getMetadata();
				metadata.getChildren().add(mdata);
			}
		}
		return metadata;
	}

	public MetadataRenderer getMetadataRenderer() {
		MetadataRenderer metadata = new MetadataRenderer(this.title.getHeaders(), this.title.getRenderer());
		if (!this.treeItems.isEmpty()) {
			for (MetadataItem item : this.treeItems) {
				MetadataRenderer mdata = item.getMetadataRenderer();
				metadata.getChildren().add(mdata);
			}
		}
		return metadata;
	}

	public int getNumberOfCurves(int nCurve) {
		if (!this.treeItems.isEmpty()) {
			for (MetadataItem item : this.treeItems) {
				nCurve = item.getNumberOfCurves(nCurve);

			}
		} else {
			nCurve++;
		}
		return nCurve;
	}

	public DefaultRow getTitleNode() {
		return this.title;
	}

	public String getTitle() {
		return this.title.getTitle();
	}

	public boolean isLeaf() {
		return (this.treeItems.size() == 0) ? Boolean.TRUE : Boolean.FALSE;
	}
}
