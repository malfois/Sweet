package ncd.utils.plot;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class Symbol extends SVGPath {

	public final static double	DEFAULT_LENGTH	= 8.0;

	private String				name;
	private double				length;

	public Symbol(String name, Color color, double length) {
		this.name = name;
		this.length = length;
		this.setFill(color);
		this.setStroke(color);
		this.setStrokeWidth(1.0);
		this.setVisible(true);
		this.setContent(RendererUtility.getSVG(this.name, this.length));
		this.prefHeight(this.length);
		this.maxHeight(this.length);
	}

	public Symbol(String name, Color color, double length, Color strokeColor, double strokeWidth, boolean visible) {
		this.name = name;
		this.length = length;
		this.setFill(color);
		this.setStroke(strokeColor);
		this.setStrokeWidth(strokeWidth);
		this.setVisible(visible);
		this.setContent(RendererUtility.getSVG(this.name, this.length));
	}

	public String getName() {
		return name;
	}

	public double getLength() {
		return length;
	}

	public void setName(String name) {
		this.name = name;
		this.setContent(RendererUtility.getSVG(this.name, this.length));
	}

	public void setLength(double length) {
		this.length = length;
		this.setContent(RendererUtility.getSVG(this.name, this.length));
	}

	public Symbol clone() {
		Symbol symbol = new Symbol(this.name, (Color) this.getFill(), this.length, (Color) this.getStroke(), this.getStrokeWidth(), this.isVisible());
		return symbol;
	}

	public String getCSSName(int index) {
		return ".chart-line-symbol.series" + String.valueOf(index);
	}

	public String getCSS() {
		return this.getCssColor() + this.getCssShape() + this.getCssSize();
	}

	private String getCssColor() {
		String value = "-fx-background-color: transparent, transparent ;";
		if (this.isVisible()) {
			Color color = (Color) this.getFill();
			value = "-fx-background-color: rgba(" + Renderer.toRGB(color) + ") , rgba(" + Renderer.toRGB(color) + ") ;";
		}
		return value;
	}

	private String getCssSize() {
		return "-fx-background-radius: " + this.length + "px;";
	}

	private String getCssShape() {
		return "-fx-shape: \"" + this.getContent() + "\";";
	}

}
