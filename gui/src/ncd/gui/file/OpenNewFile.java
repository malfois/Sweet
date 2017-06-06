package ncd.gui.file;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class OpenNewFile extends Button {

	public final static String NAME = "New";

	public OpenNewFile() {
		StackPane pane = new StackPane();

		Image img = new Image(OpenNewFile.class.getResourceAsStream("open_file.png"));
		ImageView imgView = new ImageView(img);

		Text text = new Text(NAME);
		text.setFont(Font.font("Verdana", 10));
		pane.getChildren().addAll(imgView, text);

		this.setGraphic(pane);

		this.setPrefHeight(32);
		this.setMaxHeight(32);
		this.setMinHeight(32);

		this.setPrefWidth(32);
		this.setMaxWidth(32);
		this.setMinWidth(32);
	}

}
