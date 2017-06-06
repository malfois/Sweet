package ncd.gui.icon;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class PaneUtility {

	public static final String	UNKNOWN	= "Unknown";
	public static final String	LOG_Y	= "log y";
	public static final String	XML		= "xml";

	public static Pane get(String name) {
		Pane pane = new Pane(new Text(UNKNOWN));
		if (name == null || name.length() == 0) return pane;
		if (name.equalsIgnoreCase(LOG_Y)) return LogY();
		if (name.equalsIgnoreCase(XML)) return Xml();
		return pane;
	}

	private static Pane Xml() {
		Pane pane = new Pane();

		DropShadow ds = new DropShadow();
		ds.setOffsetY(5.0f);
		ds.setColor(Color.color(0.2f, 0.2f, 0.2f));

		Font defaultFont = Font.getDefault();
		Font font = Font.font(defaultFont.getName(), FontPosture.ITALIC, 12);
		Text text = new Text("Spock");
		text.setFont(font);
		text.setEffect(ds);
		text.setFill(Color.BLUE);
		text.setTranslateY(12);
		pane.getChildren().add(text);

		return pane;
	}

	private static Pane LogY() {
		Rectangle rectangle = new Rectangle(2.0, 5.0, 2.0, 6.0);
		Polygon triangleUp = new Polygon(new double[] { 0.0, 5.0, 6.0, 5.0, 3.0, 0.0 });
		Polygon triangleDown = new Polygon(new double[] { 0.0, 11.0, 6.0, 11.0, 3.0, 16.0 });
		Shape shape = Shape.union(rectangle, triangleUp);
		shape = Shape.union(shape, triangleDown);
		shape.setFill(Color.web("#0074D9"));

		Font defaultFont = Font.getDefault();
		Font font = Font.font(defaultFont.getName(), FontPosture.ITALIC, 10);
		Text text = new Text("Log");
		text.setFont(font);
		text.setTranslateX(5);

		StackPane pane = new StackPane();
		StackPane.setAlignment(shape, Pos.CENTER_LEFT);
		StackPane.setAlignment(text, Pos.CENTER);
		pane.getChildren().addAll(shape, text);

		return pane;
	}

}
