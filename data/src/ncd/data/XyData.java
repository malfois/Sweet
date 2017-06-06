package ncd.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.january.dataset.DoubleDataset;

import javafx.scene.chart.XYChart;
import ncd.parameter.Format;

public class XyData extends AData {

	public XyData(List<Double> x, List<Double> y) {
		super(x, y);
	}

	public XyData(DoubleDataset axis, DoubleDataset data) {
		super(axis, data);
	}

	@Override
	public XYChart.Series<Number, Number> getXySeries() {
		XYChart.Series<Number, Number> series = new XYChart.Series<>();

		double[] x = this.getAxis().getData();
		double[] y = this.getData().getData();
		int nPointsX = x.length;
		int nPointsY = y.length;
		int nPoints = (nPointsX > nPointsY) ? nPointsY : nPointsX;
		for (int i = 0; i < nPoints; i++) {
			XYChart.Data<Number, Number> xyData = new XYChart.Data<>(x[i], y[i]);
			series.getData().add(xyData);
		}
		if (nPointsX > nPointsY) {
			for (int i = nPoints; i < nPointsX; i++) {
				XYChart.Data<Number, Number> xyData = new XYChart.Data<>(x[i], Double.NaN);
				series.getData().add(xyData);
			}
		}
		return series;
	}

	public XyData getRange(int iMin, int iMax) {
		double[] x = this.getAxis().getData();
		double[] y = this.getData().getData();
		int nPointsX = x.length;
		int nPointsY = y.length;
		int nPoints = (nPointsX > nPointsY) ? nPointsY : nPointsX;
		if (iMin < 0)
			iMin = 0;
		if (iMax > nPoints)
			iMax = nPoints;

		List<Double> xShort = new ArrayList<>();
		List<Double> yShort = new ArrayList<>();
		for (int i = iMin; i < iMax; i++) {
			xShort.add(x[i]);
			yShort.add(y[i]);
		}
		return new XyData(xShort, yShort);
	}

	@Override
	public IData copy() {
		List<Double> x = this.getAxisAsList();
		List<Double> cloneX = new ArrayList<Double>();
		for (Double xItem : x) {
			cloneX.add(new Double(xItem));
		}
		List<Double> y = this.getDataAsList();
		List<Double> cloneY = new ArrayList<Double>();
		for (Double yItem : y) {
			cloneY.add(new Double(yItem));
		}
		return new XyData(cloneX, cloneY);
	}

	@Override
	public String toText(String separator) {
		String text = "";
		Format format = new Format();

		double[] x = this.getAxis().getData();
		double[] y = this.getData().getData();
		int nPointsX = x.length;
		int nPointsY = y.length;
		int nPoints = (nPointsX > nPointsY) ? nPointsY : nPointsX;
		for (int i = 0; i < nPoints; i++) {
			text = format.toText(x[i]) + separator + format.toText(y[i]) + "\n";
		}
		if (nPointsX > nPointsY) {
			for (int i = nPoints; i < nPointsX; i++) {
				text = format.toText(x[i]) + separator + Double.NaN + "\n";
			}
		}

		return text;
	}

	public List<Double> getAxisAsList() {
		return Arrays.stream(this.getAxis().getData()).boxed().collect(Collectors.toList());
	}

	public List<Double> getDataAsList() {
		return Arrays.stream(this.getData().getData()).boxed().collect(Collectors.toList());
	}

}
