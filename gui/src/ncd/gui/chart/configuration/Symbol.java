package ncd.gui.chart.configuration;

import javax.xml.bind.annotation.XmlType;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import ncd.gui.chart.renderer.RendererUtility;
import ncd.utils.colour.Palette1D;
import ncd.utils.configuration.AConfiguration;

@XmlType(propOrder = { "name", "color", "length", "visible", "strokeColor", "strokeWidth" })
public class Symbol extends AConfiguration {

	private String	name;
	private boolean	visible;
	private String	color;
	private double	length;
	private String	strokeColor;
	private double	strokeWidth;

	public Symbol() {
	}

	public Symbol(Color color) {
		this.color = Palette1D.toText(color);
		this.strokeColor = this.color;
	}

	// public Symbol(Symbol symbol) {
	// this.update(symbol);
	// }
	//
	// public void update(Symbol symbol) {
	// this.color = symbol.getColor();
	// this.strokeColor = symbol.getStrokeColor();
	// this.visible = symbol.isVisible();
	// this.length = symbol.getLength();
	// this.name = symbol.getName();
	// this.strokeWidth = symbol.getStrokeWidth();
	// }

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public double getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void defaultSettings() {
		if (this.color == null) {
			this.color = Palette1D.toText(Color.BLACK);
			this.strokeColor = this.color;
		}
		if (this.strokeColor == null) {
			this.strokeColor = this.color;
		}
		this.visible = true;
		this.name = RendererUtility.CIRCLE;
		this.length = 8.0;
		this.strokeWidth = 1.0;
	}

	public SVGPath getGraphic() {
		Color c = Palette1D.toColor(color);
		SVGPath path = new SVGPath();
		path.setContent(RendererUtility.getSVG(this.name, this.length));
		path.setFill(c);
		path.setStroke(c);
		path.setStrokeWidth(1.0);
		path.setVisible(this.visible);
		path.prefHeight(this.length);
		path.maxHeight(this.length);
		path.setStyle(this.getCssColor());
		return path;
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
			Color color = Palette1D.toColor(this.color);
			value = "-fx-background-color: rgba(" + Palette1D.TO_RGB(color) + ") , rgba(" + Palette1D.TO_RGB(color) + ") ;";
		}
		return value;
	}

	private String getCssSize() {
		return "-fx-background-radius: " + this.length + "px;";
	}

	private String getCssShape() {
		return "-fx-shape: \"" + RendererUtility.getSVG(name, length) + "\";";
	}

	@Override
	public String toString() {
		return "SymbolConfiguration [name=" + name + ", visible=" + visible + ", color=" + color + ", length=" + length + ", strokeColor=" + strokeColor + ", strokeWidth="
				+ strokeWidth + "]";
	}
}
