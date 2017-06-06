package ncd.fileviewer;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class TreeFile extends StackPane {

	private TreeView<String> tree;

	public TreeFile() {
		String hostName = "computer";

		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException x) {
		}

		TreeItem<String> rootNode = new TreeItem<>(hostName);

		File[] files = File.listRoots();

		for (File file : files) {
			FilePathTreeItem treeNode = new FilePathTreeItem(file.toPath());
			rootNode.getChildren().add(treeNode);
		}

		rootNode.setExpanded(true);
		tree = new TreeView<String>(rootNode);
		this.getChildren().add(tree);

		this.tree.setOnMouseClicked(event -> changeSelection((MouseEvent) event));

	}

	private void changeSelection(MouseEvent event) {
		FilePathTreeItem item = (FilePathTreeItem) tree.getSelectionModel().getSelectedItem();
		if (item == null) return;

		File file = new File(item.getAbsolutePath());
		FileEvent<File> e = new FileEvent<>(file);
		this.fireEvent(e);
	}

	public MultipleSelectionModel getSelectionModel() {
		return this.tree.getSelectionModel();
	}
}