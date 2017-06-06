package ncd.fileviewer;

import java.io.File;
import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import ncd.gui.icon.FileSystem;

public class FileViewerColumn extends FlowPane implements IFileViewer {

	private String path;

	@SuppressWarnings("unchecked")
	public FileViewerColumn(ScrollPane scrollPane) {
		super(Orientation.VERTICAL);

		prefWrapLengthProperty().bind(scrollPane.heightProperty());
		setVgap(3);
		setHgap(5);

		this.addEventHandler(FileEvent.CLEAR_SELECTION, event -> clearSelection((FileEvent<LabelFile>) event));
	}

	private void clearSelection(FileEvent<LabelFile> event) {
		LabelFile ref = event.getResult();
		ObservableList<Node> nodes = this.getChildren();
		for (Node node : nodes) {
			LabelFile label = (LabelFile) node;
			if (!ref.equals(label)) {
				label.setSelected(false);
			}
		}
	}

	@Override
	public void update(String path) {
		this.getChildren().clear();
		this.path = path;
		File file = new File(path);
		File[] files = file.listFiles();
		Arrays.sort(files, new NormalComparator());

		for (File f : files) {
			LabelFile l = new LabelFile(f);

			FileSystem icon = new FileSystem(f, l);
			javax.swing.SwingUtilities.invokeLater(icon);

			this.getChildren().add(l);
		}
	}

	@Override
	public Region getRegion() {
		return this;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void reset() {
		this.update(path);
	}
}
