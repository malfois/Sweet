package ncd.gui.icon;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class ShapeUtility {

	public static final String	UNKNOWN			= "Unknown";
	public static final String	TRIANGLE_DOWN	= "Triangle Down";
	public static final String	TRIANGLE_RIGHT	= "Triangle Right";
	public static final String	RELOAD			= "Reload";
	public static final String	ARROW_UP		= "Arrow Up";
	public static final String	LOG_Y			= "log y";
	public static final String	GREEN_BALL		= "Green ball";
	public static final String	RED_BALL		= "Red ball";
	public static final String	ORANGE_BALL		= "Orange ball";

	public static Shape get(String name) {
		Shape shape = new Text(UNKNOWN);
		if (name == null || name.length() == 0) return shape;
		if (name.equalsIgnoreCase(TRIANGLE_DOWN)) return new Polygon(new double[] { 0.0, 0.0, 4.0, 10.0, 8.0, 0.0 });
		if (name.equalsIgnoreCase(TRIANGLE_RIGHT)) return new Polygon(new double[] { 0.0, 0.0, 10.0, 4.0, 0.0, 8.0 });
		if (name.equalsIgnoreCase(ARROW_UP)) return ArrowUp();
		if (name.equalsIgnoreCase(RELOAD)) return Reload();
		if (name.equalsIgnoreCase(GREEN_BALL)) return Ball(Color.FORESTGREEN);
		if (name.equalsIgnoreCase(RED_BALL)) return Ball(Color.RED);
		if (name.equalsIgnoreCase(ORANGE_BALL)) return Ball(Color.ORANGE);
		return shape;
	}

	private static Polygon ArrowUp() {
		double[] points = new double[] { 8.0, 0.0, 16.0, 8.0, 12.0, 8.0, 12.0, 16.0, 4.0, 16.0, 4.0, 8.0, 0.0, 8.0 };
		return new Polygon(points);
	}

	private static Shape Reload() {
		Circle outer = new Circle(8.0, 8.0, 6.0);
		Circle inner = new Circle(8.0, 8.0, 2.0);
		Shape shape = Shape.subtract(outer, inner);
		Rectangle rectangle = new Rectangle(0.0, 0.0, 8.0, 8.0);
		shape = Shape.subtract(shape, rectangle);
		Polygon triangle = new Polygon(new double[] { 0.0, 8.0, 8.0, 8.0, 4.0, 0.0 });
		shape = Shape.union(shape, triangle);
		return shape;
	}

	private static Shape Ball(Color color) {
		Circle ball = new Circle(12.0, 12.0, 11.0);
		ball.setFill(new RadialGradient(0.0, 0.0, 0.25, 0.75, .5, true, CycleMethod.NO_CYCLE, new Stop(0., Color.WHITE), new Stop(1.0, color)));
		return ball;
	}

}
