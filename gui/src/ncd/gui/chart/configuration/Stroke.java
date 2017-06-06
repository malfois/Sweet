package ncd.gui.chart.configuration;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import ncd.gui.chart.renderer.RendererUtility;
import ncd.utils.colour.Palette1D;
import ncd.utils.configuration.AConfiguration;

@XmlType(propOrder = { "color", "width", "visible", "style" })
public class Stroke extends AConfiguration {

	public final static double		DEFAULT_WIDTH	= 3.0;

	private StringProperty			color			= new SimpleStringProperty();
	private DoubleProperty			width			= new SimpleDoubleProperty(DEFAULT_WIDTH);
	private double					length;
	private BooleanProperty			visible			= new SimpleBooleanProperty(true);
	private ListProperty<Double>	style			= new SimpleListProperty<>(RendererUtility.listOfDashLines().get(0));

	public Stroke() {
	}

	public Stroke(Color color) {
		super();
		this.color.set(Palette1D.toText(color));
	}

	@XmlElement
	public String getColor() {
		return color.get();
	}

	public void setColor(String color) {
		this.color.set(color);
	}

	public StringProperty colorProperty() {
		return this.color;
	}

	@XmlElement
	public double getWidth() {
		return width.doubleValue();
	}

	public void setWidth(double width) {
		this.width.set(width);
	}

	public DoubleProperty widthProperty() {
		return this.width;
	}

	@XmlElement
	public boolean isVisible() {
		return visible.get();
	}

	public void setVisible(boolean visible) {
		this.visible.set(visible);
	}

	public BooleanProperty visibleProperty() {
		return this.visible;
	}

	@XmlElement
	public List<Double> getStyle() {
		return style;
	}

	public ObservableList<Double> getObservableStyle() {
		return FXCollections.observableArrayList(style);
	}

	public void setStyle(List<Double> style) {
		this.style = new SimpleListProperty<>(FXCollections.observableArrayList(style));
	}

	public ListProperty<Double> styleProperty() {
		return this.style;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void defaultSettings() {
		if (this.color == null) this.color = new SimpleStringProperty(Palette1D.toText(Color.BLACK));
		if (this.visible == null) this.visible = new SimpleBooleanProperty(true);
		if (this.width == null) this.width = new SimpleDoubleProperty(DEFAULT_WIDTH);
		if (this.style == null) this.style = new SimpleListProperty<>(RendererUtility.listOfDashLines().get(0));
	}

	public String getCSSName(int index) {
		return ".chart-series-line.series" + String.valueOf(index);
	}

	public String getCSSColor() {
		String value = "-fx-stroke: transparent; ";
		if (this.isVisible()) {
			Color color = (Color) Palette1D.toColor(this.color.get());
			value = "-fx-stroke: rgba(" + Palette1D.TO_RGB(color) + ");";
		}
		return value;
	}

	public String getCSSDashLine() {
		String text = " ";
		if (style.isEmpty()) return text;
		for (Double st : style) {
			int d = st.intValue();
			text += String.valueOf(d);
		}
		String value = "-fx-stroke-dash-array: " + text + "; ";
		return value;
	}

	public String getCSSWidth() {
		String value = "-fx-stroke-width: " + this.width.doubleValue() + "px; ";
		return value;
	}

	public String getCSS() {
		return this.getCSSColor() + this.getCSSWidth() + this.getCSSDashLine();
	}

	public Line getGraphic() {
		Line line = new Line(0, 0, this.length, 0);
		line.setStroke(Palette1D.toColor(color.get()));
		line.setStrokeWidth(this.width.doubleValue());
		line.setVisible(this.visible.get());
		line.getStrokeDashArray().addAll(this.style);
		return line;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "LineConfiguration [color=" + color + ", width=" + width + ", visible=" + visible + ", style="
				+ (style != null ? style.subList(0, Math.min(style.size(), maxLen)) : null) + "]";
	}
}
