package ncd.fileviewer;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ncd.gui.component.ToolTipText;

public class FileViewer extends VBox {

	private ToggleButton	columnButton	= new ToggleButton();
	private ToggleButton	listButton		= new ToggleButton();
	private Button			reset			= new Button("Reset");

	private ScrollPane		scrollPane		= new ScrollPane();
	private IFileViewer		viewer			= new FileViewerColumn(this.scrollPane);

	public FileViewer() {
		this.columnButton.setTooltip(new ToolTipText("Reset order to inital order"));

		ToggleGroup group = new ToggleGroup();

		File fileColum = new File("//beamlines/bl11/controls/scans/icon/application_view_columns.png");
		ImageView columnImage = new ImageView(new Image(fileColum.toURI().toString()));
		this.columnButton.setGraphic(columnImage);

		this.columnButton.setTooltip(new ToolTipText("List view"));
		this.columnButton.setToggleGroup(group);
		this.columnButton.setSelected(true);

		this.listButton.setTooltip(new ToolTipText("Details view"));

		File fileList = new File("//beamlines/bl11/controls/scans/icon/application_view_list.png");
		ImageView listImage = new ImageView(new Image(fileList.toURI().toString()));
		this.listButton.setGraphic(listImage);
		this.listButton.setToggleGroup(group);

		HBox box = new HBox();
		box.getChildren().addAll(this.columnButton, this.listButton, new Separator(), this.reset);

		scrollPane.setContent(this.viewer.getRegion());
		this.viewer.getRegion().prefWidthProperty().bind(this.scrollPane.widthProperty());

		this.getChildren().addAll(box, this.scrollPane);

		this.listButton.setOnAction(event -> viewDetails());
		this.columnButton.setOnAction(event -> viewList());
		this.reset.setOnAction(event -> reset());
	}

	private void reset() {
		this.viewer.reset();
	}

	private void viewList() {
		FileViewerColumn list = new FileViewerColumn(this.scrollPane);
		list.update(this.viewer.getPath());
		this.scrollPane.setContent(list);
		this.viewer = list;
	}

	private void viewDetails() {
		FileViewerDetails details = new FileViewerDetails(this.viewer.getPath(), this.scrollPane);
		this.scrollPane.setContent(details);
		this.viewer = details;
	}

	public void update(String path) {
		this.viewer.update(path);
	}

	public ScrollPane getScrollPane() {
		return this.scrollPane;
	}
}
