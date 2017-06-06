package ncd.scan.client;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ncd.gui.chart.SimpleInformationPanel;
import ncd.gui.component.number.RangeLabel;
import ncd.parameter.DoubleRange;
import ncd.parameter.Format;
import ncd.parameter.IParameter;
import ncd.scan.spock.Fit;
import ncd.utils.maths.fitting.Gaussian;
import ncd.utils.maths.fitting.Peak;

public class GaussianInformationPane extends SimpleInformationPanel {

	private Header	header	= new Header();
	private Value	value	= new Value();

	public GaussianInformationPane(String xText, String yText) {
		super(xText, yText);
		super.add(this.header, 0, 1, 6, 1);
		super.add(this.value, 0, 2, 6, 1);
	}

	public void clear() {
		super.clear();
	}

	public void setValue(Gaussian gaussian) {
		this.value.setParameter(gaussian);
	}

	public Fit getFitParameter() {
		return new Fit(this.value.getGaussian());
	}

	private class Header extends HBox {

		public Header() {
			String style = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;-fx-alignment: center; -fx-size: 2em;-fx-background-insets: 0, 0 1 1 0, 1 2 2 1;-fx-text-fill: -fx-selection-bar-text;-fx-padding: 0.166667em;";

			String[] names = Gaussian.NAMES;
			int nNames = names.length;
			Label[] labelName = new Label[nNames];

			for (int i = 0; i < nNames; i++) {
				labelName[i] = new Label(names[i]);
				labelName[i].setStyle(style);
				labelName[i].setPrefWidth(120);
			}

			this.getChildren().addAll(labelName);
		}

		public int getNumberOfRows() {
			return this.getChildren().size();
		}
	}

	private class Value extends HBox {

		private Gaussian gaussian;

		public Value() {
			int nNames = header.getNumberOfRows();
			for (int i = 0; i < nNames; i++) {
				RangeLabel<Double> labelValue = new RangeLabel<>(new DoubleRange(null), new Format());
				labelValue.setPrefWidth(120);
				this.getChildren().add(labelValue);
			}
		}

		public void setParameter(Gaussian gaussian) {
			this.gaussian = gaussian;
			this.setPosition(gaussian.getParameter(Peak.POSITION));
			this.setFwhm(gaussian.getParameter(Peak.FWHM));
			this.setArea(gaussian.getParameter(Peak.AREA));
			this.setOffset(gaussian.getParameter(Peak.OFFSET));
		}

		@SuppressWarnings("unchecked")
		private void setPosition(IParameter<Double> parameter) {
			RangeLabel<Double> value = (RangeLabel<Double>) this.getChildren().get(Peak.POSITION);
			value.setRange(parameter.getValueRange());
		}

		@SuppressWarnings("unchecked")
		private void setFwhm(IParameter<Double> parameter) {
			RangeLabel<Double> value = (RangeLabel<Double>) this.getChildren().get(Peak.FWHM);
			value.setRange(parameter.getValueRange());
		}

		@SuppressWarnings("unchecked")
		private void setArea(IParameter<Double> parameter) {
			RangeLabel<Double> value = (RangeLabel<Double>) this.getChildren().get(Peak.AREA);
			value.setRange(parameter.getValueRange());
		}

		@SuppressWarnings("unchecked")
		private void setOffset(IParameter<Double> parameter) {
			RangeLabel<Double> value = (RangeLabel<Double>) this.getChildren().get(Peak.OFFSET);
			value.setRange(parameter.getValueRange());
		}

		public Gaussian getGaussian() {
			return this.gaussian;
		}
	}

}
