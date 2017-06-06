package ncd.data;

import org.eclipse.january.dataset.DoubleDataset;

import javafx.scene.chart.XYChart;

public interface IData {

	public DoubleDataset getData();

	public void setData(DoubleDataset data);

	public DoubleDataset getAxis();

	public void setAxis(DoubleDataset axis);

	public IData copy();

	public XYChart.Series<Number, Number> getXySeries();

	public String toText(String separator);
}
