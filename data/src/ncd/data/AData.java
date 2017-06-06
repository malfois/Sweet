package ncd.data;

import java.util.List;

import org.eclipse.january.dataset.AbstractDataset;
import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;

public abstract class AData implements IData {

	private DoubleDataset	axis;
	private DoubleDataset	data;

	protected AData(DoubleDataset axis, DoubleDataset data) {
		this.axis = axis;
		this.data = data;
	}

	protected AData(List<Double> x, List<Double> y) {
		this.axis = (DoubleDataset) DatasetFactory.createFromObject(AbstractDataset.FLOAT64, x);
		this.data = (DoubleDataset) DatasetFactory.createFromObject(AbstractDataset.FLOAT64, y);
	}

	public DoubleDataset getAxis() {
		return axis;
	}

	public DoubleDataset getData() {
		return data;
	}

	public void setAxis(DoubleDataset axis) {
		this.axis = axis;
	}

	public void setData(DoubleDataset data) {
		this.data = data;
	}

}
