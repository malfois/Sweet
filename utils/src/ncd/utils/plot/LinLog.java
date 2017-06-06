package ncd.utils.plot;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.eclipse.january.dataset.AbstractDataset;
import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.IndexIterator;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class LinLog extends GraphicScale {

	public LinLog() {
	}

	@Override
	public Point2D to(double x, double y) {
		Double ret = new Double(0);
		if (Double.isNaN(y)) {
			ret = Double.NaN;
		}
		y = Math.log(y);
		ret = new Double(y);

		if (Double.isInfinite(y) || Double.isNaN(y)) {
			ret = Double.NaN;
		}
		return new Point2D(x, ret);
	}

	public DoubleDataset convert(AbstractDataset data) {
		int[] shape = data.getShape();

		int nSize = data.getSize();
		double[] d = new double[nSize];
		IndexIterator iter = data.getIterator();
		while (iter.hasNext()) {
			final double value = data.getElementDoubleAbs(iter.index);
			if (value <= 0) {
				d[iter.index] = Double.NaN;
			} else {
				d[iter.index] = Math.log(value);
			}
		}
		DoubleDataset logvalue = (DoubleDataset) DatasetFactory.createFromObject(d, shape);
		return logvalue;
	}

	@Override
	public String toString() {
		return "x - log y";
	}

	@Override
	public Series<Number, Number> convert(Series<Number, Number> series) {
		XYChart.Series<Number, Number> logSeries = new XYChart.Series<Number, Number>();

		ObservableList<Data<Number, Number>> dataList = series.getData();
		for (Data<Number, Number> data : dataList) {
			Number x = data.getXValue();
			Number y = data.getYValue();
			Point2D p = this.to(x.doubleValue(), y.doubleValue());
			if (!Double.isNaN(p.getY())) {
				XYChart.Data<Number, Number> logData = new XYChart.Data<Number, Number>(p.getX(), p.getY());
				logSeries.getData().add(logData);
			}
		}
		return logSeries;
	}

	@Override
	public String getTitleX(Unit<? extends Quantity<?>> unit) {
		return unit.toString();
	}

	@Override
	public String getTitleY(Unit<? extends Quantity<?>> xUnit, Unit<? extends Quantity<?>> yUnit) {
		return "Log " + yUnit.toString();
	}

}
