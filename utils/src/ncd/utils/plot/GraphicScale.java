package ncd.utils.plot;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.eclipse.january.dataset.AbstractDataset;
import org.eclipse.january.dataset.DoubleDataset;

import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;

public abstract class GraphicScale {

	/**
	 * return the menu list defined as x-x, log x - y , log x - log y , x - log y
	 *
	 * @return list of graphic scale names
	 */
	public static GraphicScale[]	LIST1D	= { new LinLin(), new LinLog() };

	public static GraphicScale[]	LIST2D	= { new LinLin(), new LinLog() };

	public GraphicScale() {
	}

	public abstract Point2D to(double x, double y);

	public abstract XYChart.Series<Number, Number> convert(XYChart.Series<Number, Number> series);

	public abstract String getTitleX(Unit<? extends Quantity<?>> unit);

	public abstract String getTitleY(Unit<? extends Quantity<?>> xUnit, Unit<? extends Quantity<?>> yUnit);

	public abstract DoubleDataset convert(AbstractDataset data);

	public static GraphicScale Factory(String text) {
		GraphicScale scale = new LinLin();
		if (text == null) {
			return scale;
		}
		if (text.equalsIgnoreCase(new LinLog().toString())) {
			scale = new LinLog();
		}
		return scale;
	}
}
