package ncd.gui;

import java.awt.Dimension;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ImageComponent extends VBox {

	private Dimension	componentSize;

	private Label		xLabel	= new Label("X-axis");
	private Label		yLabel	= new Label("Y-axis");
	private ImageView	image	= new ImageView();

	public ImageComponent(Dimension componentSize) {
		this.componentSize = componentSize;
		int width = this.componentSize.width;
		int height = this.componentSize.height;

		String cssDefault = "-fx-border-color: blue;\n";

		yLabel.setRotate(270);
		yLabel.setTranslateY(243);
		yLabel.setTranslateX(-243);
		yLabel.setPrefHeight(25);
		yLabel.setMinHeight(25);
		yLabel.setStyle(cssDefault);
		yLabel.setAlignment(Pos.CENTER);

		image.setFitWidth(width);
		image.setFitHeight(height);
		image.setTranslateX(-487);
		image.setStyle(cssDefault);

		xLabel.setPrefSize(width, 25);
		xLabel.setStyle(cssDefault);
		xLabel.setAlignment(Pos.CENTER);

		Label emptyBox = new Label("");
		String cssDefault1 = "-fx-border-color: red;\n";
		emptyBox.setPrefSize(25, 25);
		emptyBox.setStyle(cssDefault1);

		HBox hbox1 = new HBox(1, new Group(yLabel), new Group(image));
		hbox1.setAlignment(Pos.CENTER);
		HBox hbox2 = new HBox();
		hbox2.setSpacing(1);
		hbox2.getChildren().addAll(emptyBox, xLabel);

		this.setSpacing(1);
		this.getChildren().addAll(hbox1, hbox2);

	}

	public void setImage(WritableImage image) {
		this.image.setImage(image);
		this.image.setFitWidth(this.componentSize.width);
		this.image.setPreserveRatio(true);

	}
}
