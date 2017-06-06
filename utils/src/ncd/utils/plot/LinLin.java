package ncd.utils.plot;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.eclipse.january.dataset.AbstractDataset;
import org.eclipse.january.dataset.DoubleDataset;

import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart.Series;

public class LinLin extends GraphicScale {

	public LinLin() {
	}

	@Override
	public Point2D to(double x, double y) {
		return new Point2D(new Double(x), new Double(y));
	}

	public DoubleDataset convert(AbstractDataset data) {
		return (DoubleDataset) data.clone();
	}

	@Override
	public String toString() {
		return " x - y ";
	}

	@Override
	public Series<Number, Number> convert(Series<Number, Number> series) {
		return series;
	}

	@Override
	public String getTitleX(Unit<? extends Quantity<?>> unit) {
		return unit.toString();
	}

	@Override
	public String getTitleY(Unit<? extends Quantity<?>> xUnit, Unit<? extends Quantity<?>> yUnit) {
		return yUnit.toString();
	}

}
