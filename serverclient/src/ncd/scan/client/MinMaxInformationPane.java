package ncd.scan.client;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ncd.gui.chart.SimpleInformationPanel;
import ncd.gui.component.number.RangeLabel;
import ncd.parameter.DoubleRange;
import ncd.parameter.Format;
import ncd.scan.spock.Value;

public class MinMaxInformationPane extends SimpleInformationPanel {

	private HBox	header		= new HBox();
	private HBox	minValue	= new HBox();
	private HBox	maxValue	= new HBox();

	public MinMaxInformationPane(String xText, String yText) {
		super(xText, yText);

		String style = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;-fx-alignment: center; -fx-size: 2em;-fx-background-insets: 0, 0 1 1 0, 1 2 2 1;-fx-text-fill: -fx-selection-bar-text;-fx-padding: 0.166667em;";
		String[] names = new String[] { "Position", "Value" };
		int nNames = names.length;
		Label[] labelName = new Label[nNames];

		Label emptyLabel = new Label("");
		emptyLabel.setStyle(style);
		emptyLabel.setPrefWidth(120);

		Label minLabel = new Label("Y Minimum");
		minLabel.setStyle(style);
		minLabel.setPrefWidth(120);
		minValue.getChildren().add(minLabel);

		Label maxLabel = new Label("Y Maximum");
		maxLabel.setStyle(style);
		maxLabel.setPrefWidth(120);
		maxValue.getChildren().add(maxLabel);

		for (int i = 0; i < nNames; i++) {
			labelName[i] = new Label(names[i]);
			labelName[i].setStyle(style);
			labelName[i].setPrefWidth(120);

			RangeLabel<Double> labelValue = new RangeLabel<>(DoubleRange.INFINITE, new Format());
			labelValue.setPrefWidth(120);
			minValue.getChildren().add(labelValue);

			RangeLabel<Double> maxVal = new RangeLabel<>(DoubleRange.INFINITE, new Format());
			maxVal.setPrefWidth(120);
			maxValue.getChildren().add(maxVal);

		}

		header.getChildren().add(emptyLabel);
		header.getChildren().addAll(labelName);
		super.add(this.header, 0, 1, 6, 1);
		super.add(this.minValue, 0, 2, 6, 1);
		super.add(this.maxValue, 0, 3, 6, 1);

	}

	@SuppressWarnings("unchecked")
	public void setValue(Value min, Value max) {
		RangeLabel<Double> minPosition = (RangeLabel<Double>) this.minValue.getChildren().get(1);
		minPosition.setValue(min.getPosition());
		RangeLabel<Double> minValue = (RangeLabel<Double>) this.minValue.getChildren().get(2);
		minValue.setValue(min.getValue());

		RangeLabel<Double> maxPosition = (RangeLabel<Double>) this.maxValue.getChildren().get(1);
		maxPosition.setValue(max.getPosition());
		RangeLabel<Double> maxValue = (RangeLabel<Double>) this.maxValue.getChildren().get(2);
		maxValue.setValue(max.getValue());
	}

	@SuppressWarnings("unchecked")
	public Value getMinInformation() {
		RangeLabel<Double> minPosition = (RangeLabel<Double>) this.minValue.getChildren().get(1);
		double position = minPosition.getValue();
		RangeLabel<Double> minValue = (RangeLabel<Double>) this.minValue.getChildren().get(2);
		double value = minValue.getValue();
		return new Value(position, value);
	}

	@SuppressWarnings("unchecked")
	public Value getMaxInformation() {
		RangeLabel<Double> maxPosition = (RangeLabel<Double>) this.maxValue.getChildren().get(1);
		double position = maxPosition.getValue();
		RangeLabel<Double> maxValue = (RangeLabel<Double>) this.maxValue.getChildren().get(2);
		double value = maxValue.getValue();
		return new Value(position, value);
	}

}
