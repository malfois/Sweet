package ncd.utils.plot;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Stroke extends Line {

	public final static double DEFAULT_WIDTH = 3.0;

	public Stroke(Color color, double strokeWidth, boolean visible) {
		this.setStroke(color);
		this.setStrokeWidth(strokeWidth);
		this.setVisible(visible);
		this.setSize(0.0, 0.0, 40.0, 0.0);
	}

	public Color getColor() {
		return (Color) this.getStroke();
	}

	public void setColor(Color color) {
		this.setStroke(color);
	}

	public void setStrokeDashArray(ObservableList<Double> style) {
		this.getStrokeDashArray().clear();
		this.getStrokeDashArray().addAll(style);
	}

	public String getCSSName(int index) {
		return ".chart-series-line.series" + String.valueOf(index);
	}

	public String getCSSColor() {
		String value = "-fx-stroke: transparent; ";
		if (this.isVisible()) {
			Color color = (Color) this.getStroke();
			value = "-fx-stroke: rgba(" + Renderer.toRGB(color) + ");";
		}
		return value;
	}

	public String getCSSDashLine() {
		ObservableList<Double> styles = this.getStrokeDashArray();
		String text = " ";
		if (styles.isEmpty())
			return text;
		for (Double style : styles) {
			int d = style.intValue();
			text += String.valueOf(d);
		}
		String value = "-fx-stroke-dash-array: " + text + "; ";
		return value;
	}

	public String getCSSWidth() {
		String value = "-fx-stroke-width: " + this.getStrokeWidth() + "px; ";
		return value;
	}

	public String getCSS() {
		return this.getCSSColor() + this.getCSSWidth() + this.getCSSDashLine();
	}

	private void setSize(double startX, double startY, double endX, double endY) {
		this.setStartX(startX);
		this.setStartY(startY);
		this.setEndX(endX);
		this.setEndY(endY);
	}

	public Stroke clone() {
		Stroke line = new Stroke((Color) this.getStroke(), this.getStrokeWidth(), this.isVisible());
		line.getStrokeDashArray().addAll(this.getStrokeDashArray());
		return line;
	}

}
