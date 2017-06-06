
package ncd.fileviewer;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import ncd.gui.component.ToolTipText;
import ncd.gui.icon.ShapeUtility;

public class DirectoryPane extends HBox {

	private ComboBox<String>	directory				= new ComboBox<>();
	private final int			MAXIMUM_NUMBER_OF_ITEMS	= 10;

	private Button				reload					= new Button();
	private Button				up						= new Button();

	private String				oldDirectory;

	public DirectoryPane() {
		this.directory.setEditable(true);
		this.directory.setPrefWidth(700);

		this.reload.setTooltip(new ToolTipText("Reload the content of the folder"));
		this.up.setTooltip(new ToolTipText("Go one level up"));

		this.reload.setGraphic(ShapeUtility.get(ShapeUtility.RELOAD));
		this.up.setGraphic(ShapeUtility.get(ShapeUtility.ARROW_UP));

		this.getChildren().addAll(this.directory, this.reload, this.up);

		this.reload.setOnAction(event -> reload());
		this.up.setOnAction(event -> uploadParent());

		this.reload.setDisable(true);
		this.up.setDisable(true);

		directory.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1) {
				File file = new File(t1);
				if (!file.exists()) {
					directory.setValue(oldDirectory);
					return;
				} else {
					FileEvent<File> event = new FileEvent<>(new File(t1));
					fireEvent(event);
				}
			}
		});
	}

	private void uploadParent() {
		String directory = this.directory.getValue();
		File file = new File(directory).getParentFile();
		FileEvent<File> event = new FileEvent<>(file);
		this.fireEvent(event);
	}

	private void reload() {
		String directory = this.directory.getValue();
		FileEvent<File> event = new FileEvent<>(new File(directory));
		this.fireEvent(event);
	}

	public void add(String path) {
		this.reload.setDisable(false);
		this.up.setDisable(false);
		int nItems = this.directory.getItems().size();
		if (nItems == MAXIMUM_NUMBER_OF_ITEMS) {
			this.directory.getItems().remove(0);
		}
		directory.getItems().add(path);
		directory.setValue(path);
		this.oldDirectory = path;
	}
}
