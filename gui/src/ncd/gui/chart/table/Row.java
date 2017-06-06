package ncd.gui.chart.table;

import java.util.Map;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import ncd.utils.file.MetadataType;

public abstract class Row extends HBox {

	protected Map<String, String> metadata;

	protected abstract void selectionChanged(MouseEvent event);

	public Row(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public Map<String, String> getHeaders() {
		return metadata;
	}

	public void upgradeBackground() {
		String styleCell = " -fx-background-color: derive(-fx-base,26.4%);";
		if (this.isSelected()) {
			styleCell = "-fx-background-color: -fx-accent; ";
		}
		ObservableList<Node> nodes = this.getChildren();
		for (Node node : nodes) {
			node.setStyle(styleCell);
		}
	}

	protected void setSelectable() {
		ObservableList<Node> nodes = this.getChildren();
		for (Node node : nodes) {
			node.setOnMousePressed(event -> selectionChanged(event));
		}
	}

	public void setSelected(Boolean selected) {
		metadata.put(MetadataType.SELECTED, String.valueOf(selected));
	}

	public boolean isSelected() {
		String sel = metadata.get(MetadataType.SELECTED);
		if (sel != null) { return Boolean.valueOf(sel); }
		return false;
	}

}
