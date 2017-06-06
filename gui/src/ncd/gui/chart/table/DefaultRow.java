package ncd.gui.chart.table;

import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.util.Pair;
import ncd.gui.DefaultContextMenu;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.renderer.RendererGraphic;
import ncd.gui.event.ContextMenuEvent;
import ncd.gui.event.RowEvent;
import ncd.gui.icon.ShapeUtility;
import ncd.logger.NcdLogger;
import ncd.utils.file.AFile;
import ncd.utils.file.AsciiFile;
import ncd.utils.file.Metadata;
import ncd.utils.file.MetadataType;

public class DefaultRow extends Row {

	private Label	arrowIcon		= new Label();
	private Label	plotCheckBox	= new Label();
	private Label	name			= new Label();
	private Label	renderer		= new Label();

	public DefaultRow(Map<String, String> metadata) {
		super(metadata);

		this.getChildren().addAll(this.arrowIcon, this.plotCheckBox, this.name, this.renderer);

		CheckBox checkBox = new CheckBox();
		Boolean plot = Boolean.valueOf(this.metadata.get(AsciiFile.PLOT));
		checkBox.setSelected(plot);
		plotCheckBox.setGraphic(checkBox);
		plotCheckBox.setAlignment(Pos.CENTER_RIGHT);

		String type = this.metadata.get(AFile.TYPE);
		if (type.equalsIgnoreCase(MetadataType.CURVE)) {
			this.buildCurveRow();
		} else {
			this.buildTitleRow();
		}

		this.name.setText("   " + this.metadata.get(AFile.TITLE));

		this.renderer.setAlignment(Pos.CENTER);

		this.upgradeBackground();
	}

	private void buildTitleRow() {
		this.arrowIcon.setGraphic(ShapeUtility.get(ShapeUtility.TRIANGLE_DOWN));
		this.arrowIcon.setAlignment(Pos.CENTER);

		this.getPlotNode().setOnAction(event -> dataPlotChanged());
		this.arrowIcon.setOnMousePressed(event -> visibilityChanged());
	}

	@SuppressWarnings("unchecked")
	private void buildCurveRow() {
		this.getPlotNode().setOnAction(event -> plotChanged());

		this.renderer.setAlignment(Pos.CENTER);
		this.renderer.setGraphic(new RendererGraphic());

		DefaultContextMenu contextMenu = new DefaultContextMenu();
		contextMenu.addEventHandler(ContextMenuEvent.RENDERER_ACTION, event -> rendererSelected((ContextMenuEvent<MenuItem>) event));
		contextMenu.addEventHandler(ContextMenuEvent.SAVE_DATA, event -> saveData((ContextMenuEvent<MenuItem>) event));
		ObservableList<Node> nodes = this.getChildren();
		for (Node node : nodes) {
			Label label = (Label) node;
			label.setContextMenu(contextMenu);
		}

	}

	public SVGPath getTriangleIcon() {
		return (SVGPath) this.arrowIcon.getGraphic();
	}

	public String getTitle() {
		return this.name.getText();
	}

	public Pair<Integer, Renderer> getPlotIndex() {
		return new Pair<>(Integer.parseInt(this.metadata.get(MetadataType.CURVE_INDEX)), ((RendererGraphic) this.renderer.getGraphic()).getRenderer());
	}

	public void setWidths(double[] widths) {
		ObservableList<Node> nodes = this.getChildren();
		int nNodes = nodes.size();
		if (nNodes != widths.length) {
			NcdLogger.Error(this.getClass().getSimpleName(), "setWidths", "Number of nodes different from number of Widths");
			IndexOutOfBoundsException exception = new IndexOutOfBoundsException("Number of nodes different from number of Widths");
			exception.printStackTrace();
		}
		for (int i = 0; i < nNodes; i++) {
			Labeled region = (Labeled) nodes.get(i);
			region.setPrefWidth(widths[i]);
		}

	}

	public void setLeaf() {
		this.arrowIcon.setGraphic(null);
	}

	public void setRenderer(Renderer renderer) {
		renderer.getLine().setLength(this.renderer.getPrefWidth());
		RendererGraphic graphic = (RendererGraphic) this.renderer.getGraphic();
		graphic.setRenderer(renderer);
	}

	public Renderer getRenderer() {
		Node node = this.renderer.getGraphic();
		if (node != null) { return ((RendererGraphic) this.renderer.getGraphic()).getRenderer(); }
		return null;
	}

	public void setPlot(Boolean selected, Boolean indeterminate) {
		CheckBox checkBox = (CheckBox) this.plotCheckBox.getGraphic();
		checkBox.setIndeterminate(indeterminate);
		checkBox.setSelected(selected);
		this.metadata.put(AsciiFile.PLOT, String.valueOf(selected));
	}

	public void setPlot(Boolean selected) {
		this.setPlot(selected, Boolean.FALSE);
	}

	public Boolean getPlot() {
		Label label = (Label) this.getChildren().get(1);
		CheckBox checkBox = (CheckBox) label.getGraphic();
		return checkBox.isSelected();
	}

	public CheckBox getPlotNode() {
		Label label = (Label) this.getChildren().get(1);
		return (CheckBox) label.getGraphic();
	}

	public Metadata getMetadata() {
		Metadata metadata = new Metadata(this.metadata);
		Parent parent = this.getParent();
		while (parent instanceof MetadataItem) {
			if (parent instanceof MetadataItem) {
				MetadataItem item = (MetadataItem) parent;
				DefaultRow row = item.getTitleNode();
				Metadata mdata = new Metadata(row.getHeaders());
				if (!item.isLeaf()) {
					mdata.getChildren().add(metadata);
				}
				metadata = mdata;
			}
			parent = parent.getParent();
		}
		return metadata;
	}

	public Map<String, String> getHeaders() {
		return this.metadata;
	}

	public MetadataItem getParentItem() {
		boolean isFound = false;
		Parent parent = this.getParent();
		while (!isFound) {
			parent = parent.getParent();
			if (parent instanceof MetadataItem) {
				MetadataItem item = (MetadataItem) parent;
				if (item.getTitle() != this.getTitle()) {
					isFound = true;
				}
			}
		}

		return (MetadataItem) parent;
	}

	public MetadataItem getRoot() {
		Parent parent = this.getParent();
		Parent previousParent = parent;
		while (parent instanceof MetadataItem) {
			if (parent instanceof MetadataItem) {
				previousParent = parent;
			}
			parent = parent.getParent();
		}
		MetadataItem item = (MetadataItem) previousParent;
		return item;
	}

	@Override
	protected void selectionChanged(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			RowEvent<DefaultRow> e = new RowEvent<>(RowEvent.SELECTION_MODIFIED, this);
			this.fireEvent(e);
		}
	}

	public int getCurveIndex() {
		return Integer.parseInt(this.metadata.get(MetadataType.CURVE_INDEX));
	}

	private void rendererSelected(ContextMenuEvent<MenuItem> event) {
		event.consume();
		ContextMenuEvent<DefaultRow> e = new ContextMenuEvent<>(ContextMenuEvent.RENDERER_ACTION, this);
		this.fireEvent(e);
	}

	private void plotChanged() {
		EventType<?> eventType = RowEvent.CURVE_PLOT_REMOVED;
		String plot = String.valueOf(Boolean.FALSE);
		if (this.getPlot()) {
			eventType = RowEvent.CURVE_PLOT_ADDED;
			plot = String.valueOf(Boolean.TRUE);
		}
		this.metadata.put(AsciiFile.PLOT, plot);
		RowEvent<DefaultRow> event = new RowEvent<>(eventType, this);
		this.fireEvent(event);
	}

	private void dataPlotChanged() {
		this.fireEvent(new RowEvent<DefaultRow>(RowEvent.DATA_PLOT_CLICKED, this));
	}

	private void visibilityChanged() {
		Shape icon = (Shape) this.arrowIcon.getGraphic();
		if (isExpanded()) {
			icon = ShapeUtility.get(ShapeUtility.TRIANGLE_RIGHT);
		} else {
			icon = ShapeUtility.get(ShapeUtility.TRIANGLE_DOWN);
		}
		this.arrowIcon.setGraphic(icon);
		EventType<?> eventType = RowEvent.VISIBILITY_CHANGED;
		RowEvent<DefaultRow> event = new RowEvent<>(eventType, this);
		this.fireEvent(event);
	}

	private void saveData(ContextMenuEvent<MenuItem> event) {
		ContextMenuEvent<DefaultRow> e = new ContextMenuEvent<>(ContextMenuEvent.SAVE_DATA, this);
		this.fireEvent(e);
	}

	public boolean isExpanded() {
		Polygon icon = (Polygon) this.arrowIcon.getGraphic();
		if (icon.getPoints().equals(((Polygon) ShapeUtility.get(ShapeUtility.TRIANGLE_DOWN)).getPoints())) return true;
		return false;
	}

	@Override
	public String toString() {
		return "DefaultRow [plotCheckBox=" + this.getPlotNode().isSelected() + ", name=" + name.getText() + "]";
	}

}
