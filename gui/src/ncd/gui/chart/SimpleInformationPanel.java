package ncd.gui.chart;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ncd.gui.component.number.RangeLabel;
import ncd.parameter.DoubleRange;
import ncd.parameter.Format;

public class SimpleInformationPanel extends GridPane {

	protected Label					name	= new Label();
	protected RangeLabel<Double>	x		= new RangeLabel<Double>(new DoubleRange(null), new Format());
	private RangeLabel<Double>		y		= new RangeLabel<Double>(new DoubleRange(null), new Format());

	public SimpleInformationPanel(String xText, String yText) {
		name.setPrefWidth(200);
		x.setPrefWidth(120);
		y.setPrefWidth(120);

		setHgap(10);
		setPadding(new Insets(5, 5, 5, 5));

		add(this.name, 0, 0);
		add(new Label(xText), 1, 0);
		add(x, 2, 0);
		add(new Label(yText), 4, 0);
		add(y, 5, 0);

		// String style = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;-fx-alignment: center; -fx-size:
		// 2.0em;-fx-font-weight: "
		// + "bold;-fx-background-insets: 0, 0 1 1 0, 1 2 2 1;-fx-padding: 7px; -fx-background-radius: 2px; -fx-border-radius: 2px;-fx-text-fill:
		// -fx-selection-bar-text;";

	}

	public void clear() {
		this.clearPosition();
		this.name.setText("");
	}

	public void clearPosition() {
		this.x.setValue(null);
		this.y.setValue(null);
	}

	public void setXValue(Number value) {
		this.x.setValue(value.doubleValue());
	}

	public void setYValue(Number value) {
		this.y.setValue(value.doubleValue());
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	@Override
	public String toString() {
		return "SimpleInformationPanel [name=" + name.getText() + "]";
	}

}
