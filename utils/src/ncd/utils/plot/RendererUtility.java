package ncd.utils.plot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 * @author mmalfois
 *
 */
public class RendererUtility {

	public static final String	CIRCLE			= "Circle";
	public static final String	DIAGONAL_CROSS	= "Diagonal Cross";
	public static final String	DIAMOND			= "Diamond";
	public static final String	REGULAR_CROSS	= "Regular Cross";
	public static final String	SQUARE			= "Square";
	public static final String	TRIANGLE_DOWN	= "Triangle Down";
	public static final String	TRIANGLE_LEFT	= "Triangle Left";
	public static final String	TRIANGLE_RIGHT	= "Triangle Right";
	public static final String	TRIANGLE_UP		= "Triangle Up";

	public static String getSVG(String name, double length) {
		String svg = getCircle(length);
		if (name == null || name.length() == 0) {
			return svg;
		}
		if (name.equalsIgnoreCase(DIAGONAL_CROSS)) {
			svg = getDiagonalCross(length);
		}
		if (name.equalsIgnoreCase(DIAMOND)) {
			svg = getDiamond(length);
		}
		if (name.equalsIgnoreCase(REGULAR_CROSS)) {
			svg = getRegularCross(length);
		}
		if (name.equalsIgnoreCase(SQUARE)) {
			svg = getSquare(length);
		}
		if (name.equalsIgnoreCase(TRIANGLE_DOWN)) {
			svg = getTriangleDown(length);
		}
		if (name.equalsIgnoreCase(TRIANGLE_LEFT)) {
			svg = getTriangleLeft(length);
		}
		if (name.equalsIgnoreCase(TRIANGLE_RIGHT)) {
			svg = getTriangleRight(length);
		}
		if (name.equalsIgnoreCase(TRIANGLE_UP)) {
			svg = getTriangleUp(length);
		}
		return svg;
	}

	public static ObservableList<String> ListOfSymbolNames() {
		ObservableList<String> names = FXCollections.observableArrayList();
		names.add(CIRCLE);
		names.add(DIAGONAL_CROSS);
		names.add(DIAMOND);
		names.add(REGULAR_CROSS);
		names.add(SQUARE);
		names.add(TRIANGLE_DOWN);
		names.add(TRIANGLE_LEFT);
		names.add(TRIANGLE_RIGHT);
		names.add(TRIANGLE_UP);
		return names;
	}

	private static String getCircle(double diameter) {
		String r = String.valueOf(diameter / 2.0);
		return "M 0 0 A " + r + " " + r + "  0 1,0 1 0 Z";
	}

	private static String getDiagonalCross(double length) {
		String zero = String.valueOf(0.0);
		String quarter = String.valueOf(length / 4.0);
		String half = String.valueOf(length / 2.0);
		String threeQuarter = String.valueOf(3.0 * length / 4.0);
		String l = String.valueOf(length);

		String value = "M" + quarter + "," + zero;
		value = value + " L" + half + "," + quarter;
		value = value + " L" + threeQuarter + "," + zero;
		value = value + " L" + l + "," + quarter;
		value = value + " L" + threeQuarter + "," + half;
		value = value + " L" + l + "," + threeQuarter;
		value = value + " L" + threeQuarter + "," + l;
		value = value + " L" + half + "," + threeQuarter;
		value = value + " L" + quarter + "," + l;
		value = value + " L" + zero + "," + threeQuarter;
		value = value + " L" + quarter + "," + half;
		value = value + " L" + zero + "," + quarter;
		value = value + " Z";
		return value;
	}

	private static String getRegularCross(double length) {
		String zero = String.valueOf(0.0);
		String third = String.valueOf(length / 3.0);
		String twoThird = String.valueOf(2.0 * length / 3.0);
		String l = String.valueOf(length);

		String value = "M" + third + "," + zero;
		value = value + " L" + twoThird + "," + zero;
		value = value + " L" + twoThird + "," + third;
		value = value + " L" + l + "," + third;
		value = value + " L" + l + "," + twoThird;
		value = value + " L" + twoThird + "," + twoThird;
		value = value + " L" + twoThird + "," + l;
		value = value + " L" + third + "," + l;
		value = value + " L" + third + "," + twoThird;
		value = value + " L" + zero + "," + twoThird;
		value = value + " L" + zero + "," + third;
		value = value + " L" + third + "," + third;
		value = value + " Z";
		return value;
	}

	private static String getDiamond(double length) {
		String zero = String.valueOf(0.0);
		String half = String.valueOf(length / 2.0);
		String quarter = String.valueOf(length / 4.0);
		String l = String.valueOf(length);

		String value = "M" + quarter + ",0 ";
		value = value + " L" + half + "," + half;
		value = value + " L" + quarter + "," + l;
		value = value + " L" + zero + "," + half;
		value = value + " Z";
		return value;
	}

	private static String getSquare(double length) {
		String zero = String.valueOf(0.0);
		String l = String.valueOf(length);

		String value = "M" + zero + "," + zero;
		value = value + " L" + l + "," + zero;
		value = value + " L" + l + "," + l;
		value = value + " L" + zero + "," + l;
		value = value + " Z";
		return value;
	}

	private static String getTriangleDown(double length) {
		String zero = String.valueOf(0.0);
		String half = String.valueOf(length / 2.0);
		String l = String.valueOf(length);

		String value = "M" + zero + "," + zero;
		value = value + " L" + half + "," + l;
		value = value + " L" + l + "," + zero;
		value = value + " Z";
		return value;
	}

	private static String getTriangleLeft(double length) {
		String zero = String.valueOf(0.0);
		String half = String.valueOf(length / 2.0);
		String l = String.valueOf(length);

		String value = "M" + zero + "," + half;
		value = value + " L" + l + "," + zero;
		value = value + " L" + l + "," + l;
		value = value + " Z";
		return value;
	}

	private static String getTriangleRight(double length) {
		String zero = String.valueOf(0.0);
		String half = String.valueOf(length / 2.0);
		String l = String.valueOf(length);

		String value = "M" + zero + "," + zero;
		value = value + " L" + l + "," + half;
		value = value + " L" + zero + "," + l;
		value = value + " Z";
		return value;
	}

	private static String getTriangleUp(double length) {
		String zero = String.valueOf(0.0);
		String half = String.valueOf(length / 2.0);
		String l = String.valueOf(length);

		String value = "M" + zero + "," + l;
		value = value + " L" + l + "," + l;
		value = value + " L" + half + "," + zero;
		value = value + " Z";
		return value;
	}

	public static ObservableList<ObservableList<Double>> listOfDashLines() {
		ObservableList<ObservableList<Double>> lines = FXCollections.observableArrayList();

		Stroke s1 = new Stroke(Color.BLACK, Stroke.DEFAULT_WIDTH, true);
		lines.add(s1.getStrokeDashArray());

		ObservableList<Double> l2 = FXCollections.observableArrayList();
		l2.addAll(2.0, 8.0);
		lines.add(l2);

		ObservableList<Double> l3 = FXCollections.observableArrayList();
		l3.add(5.0);
		lines.add(l3);

		ObservableList<Double> l4 = FXCollections.observableArrayList();
		l4.add(10.0);
		lines.add(l4);

		ObservableList<Double> l5 = FXCollections.observableArrayList();
		l5.add(14.0);
		lines.add(l5);

		ObservableList<Double> l6 = FXCollections.observableArrayList();
		l6.addAll(20.0, 8.0);
		lines.add(l6);

		return lines;
	}

}
