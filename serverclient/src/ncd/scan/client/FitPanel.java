package ncd.scan.client;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ncd.data.IData;
import ncd.data.XyData;
import ncd.gui.chart.ChartPanel;
import ncd.gui.chart.table.MetadataItem;
import ncd.gui.event.ToggleButtonEvent;
import ncd.gui.icon.FileUtility;
import ncd.gui.icon.RadioButtonUtility;
import ncd.scan.client.event.FitMouseEvent;
import ncd.scan.spock.Fit;
import ncd.utils.file.AFile;
import ncd.utils.file.AsciiFile;
import ncd.utils.file.Metadata;
import ncd.utils.file.MetadataType;
import ncd.utils.maths.fitting.Function;
import ncd.utils.maths.fitting.Gaussian;
import ncd.utils.maths.nr.LinearInterpolation;
import ncd.utils.maths.optimiser.Fitter;

public class FitPanel extends ChartPanel {

	private final static String		NAME					= "Data";

	private GaussianInformationPane	informationPane			= new GaussianInformationPane("x", "y");

	private FitSelection			fitSelection			= new FitSelection();

	final EventHandler<MouseEvent>	startFitEventHandler	= mouseEvent -> startFit(mouseEvent);
	final EventHandler<MouseEvent>	fittingEventHandler		= mouseEvent -> fitting(mouseEvent);
	final EventHandler<MouseEvent>	fitEventHandler			= mouseEvent -> performFit();

	public FitPanel(String name) {
		super(name);
		this.initialise();
	}

	public FitPanel() {
		super(NAME);
		this.initialise();
	}

	private void initialise() {

		ObservableList<Node> toolBar = FXCollections.observableArrayList();
		RadioButton fitButton = RadioButtonUtility.getButton(FileUtility.FIT);
		RadioButton zoomButton = RadioButtonUtility.getButton(FileUtility.ZOOM);
		toolBar.addAll(fitButton, zoomButton);
		this.getToolBar().getItems().add(1, fitButton);
		this.getToolBar().getItems().add(2, new Separator());
		this.getToolBar().getItems().add(3, zoomButton);

		ToggleGroup group = new ToggleGroup();
		fitButton.setToggleGroup(group);
		zoomButton.setToggleGroup(group);
		zoomButton.setSelected(true);

		super.setInformationPanel(this.informationPane);

		fitButton.addEventHandler(ToggleButtonEvent.FIT, event -> fit());
		zoomButton.addEventHandler(ToggleButtonEvent.ZOOM, event -> zoom());

	}

	public void setCurveNameInformation(String name) {
		this.informationPane.setName(name);
	}

	private void removeFitEventHandlers() {
		this.getChart().setOnMousePressed(null);
		this.getChart().setOnMouseDragged(null);
		this.getChart().setOnMouseReleased(null);
	}

	private void fit() {
		this.getChart().removeZoomEventHandler();
		this.getChart().setOnMousePressed(this.startFitEventHandler);
		this.getChart().setOnMouseDragged(this.fittingEventHandler);
		this.getChart().setOnMouseReleased(this.fitEventHandler);
	}

	private void startFit(MouseEvent event) {
		this.fitSelection.setYShift(this.getChart().getInsets().getTop());
		this.fitSelection.setAxis((NumberAxis) this.getChart().getXAxis(), (NumberAxis) this.getChart().getYAxis());
		this.fitSelection.setXMinimum(event.getX());
		Rectangle rectangle = this.fitSelection.getRectangle();
		this.getChart().addNode(rectangle);
	}

	private void fitting(MouseEvent event) {
		this.getChart().removeNode(this.fitSelection.getRectangle());
		this.fitSelection.setXMaximum(event.getX());
		Rectangle rectangle = this.fitSelection.getRectangle();
		this.getChart().addNode(rectangle);
	}

	private void performFit() {
		this.getChart().removeNode(this.fitSelection.getRectangle());

		MetadataItem item = (MetadataItem) this.getLegend().getSelectedItem().getParentItem();
		Metadata itemMetadata = item.getMetadata();
		Metadata dataMetadata = itemMetadata.getMetadataWithEntry(MetadataType.DATA_TYPE, MetadataType.DATA);

		int index = Integer.parseInt(dataMetadata.get(MetadataType.CURVE_INDEX));
		XyData data = this.getChart().getDataset().get(index);
		XyData modifiedData = this.fitSelection.getData(data);
		XyData fitData = this.fitSelection.fit(modifiedData);
		Metadata fitMetadata = this.fitSelection.getMetadata();
		List<XyData> d = new ArrayList<>();
		d.add(data);
		d.add(fitData);

		Metadata metadata = this.getLegend().getSelectedItem().getMetadata();
		Metadata oldMetadata = metadata.getMetadataWithEntry(AFile.TITLE, item.getTitle().trim());
		oldMetadata.getChildren().clear();
		oldMetadata.getChildren().add(dataMetadata);
		oldMetadata.getChildren().add(fitMetadata);

		List<Metadata> metadataList = new ArrayList<>();
		metadataList.add(metadata);

		super.plot(metadataList, d);
		this.informationPane.setValue((Gaussian) this.fitSelection.getFunction());

		FitMouseEvent event = new FitMouseEvent();
		this.fireEvent(event);
	}

	private void zoom() {
		this.removeFitEventHandlers();
		this.getChart().installZoomEventHandler();
	}

	public void plot(Metadata metadata, IData data) {
		super.clear();

		XyData fitData = this.fitSelection.fit((XyData) data);
		Function function = this.fitSelection.getFunction();

		List<XyData> d = new ArrayList<>();
		d.add((XyData) data);
		d.add(fitData);
		this.updateDataFitMetadata(metadata, this.fitSelection.getMetadata());
		List<Metadata> metadataList = new ArrayList<>();
		metadataList.add(metadata);
		super.plot(metadataList, d);

		this.informationPane.setValue((Gaussian) function);

	}

	public Fit getFitparameter() {
		return this.informationPane.getFitParameter();
	}

	private void updateDataFitMetadata(Metadata metadata, Metadata fitMetadata) {
		Metadata leaf = metadata.getLeaf();
		Metadata curveMetadata = leaf.clone();
		curveMetadata.getHeaders().put(AFile.TITLE, "Data");
		curveMetadata.getHeaders().put(AsciiFile.Y_NAME, "Data");
		curveMetadata.getHeaders().put(MetadataType.SELECTED, String.valueOf(true));
		String title = leaf.get(AFile.TITLE);
		leaf.getHeaders().clear();
		leaf.getHeaders().put(AFile.TITLE, title);
		leaf.getHeaders().put(AFile.TYPE, MetadataType.DATA);
		leaf.getHeaders().put(AsciiFile.NUMBER_OF_CURVES, String.valueOf(2));
		fitMetadata.getHeaders().put(AsciiFile.X_NAME, curveMetadata.get(AsciiFile.X_NAME));
		leaf.getChildren().add(curveMetadata);
		leaf.getChildren().add(fitMetadata);
	}

	private class FitSelection {

		private NumberAxis	xAxis;
		private Rectangle	rectangle	= new Rectangle();
		private double		yShift;

		private Function	function;

		public void setAxis(NumberAxis xAxis, NumberAxis yAxis) {
			this.xAxis = xAxis;

			double lower = yAxis.getLowerBound();
			double yLower = yAxis.getDisplayPosition(lower);
			double upper = yAxis.getUpperBound();
			double yUpper = yAxis.getDisplayPosition(upper);
			this.setYRange(yAxis.localToParent(0, yLower).getY() + yShift, yAxis.localToParent(0, yUpper).getY() + yShift);

		}

		public XyData getData(XyData data) {
			Point2D lower = new Point2D(rectangle.getX(), rectangle.getY());
			double xLower = xAxis.getValueForDisplay(xAxis.parentToLocal(lower).getX()).doubleValue();
			Point2D upper = new Point2D(rectangle.getX() + rectangle.getWidth(), rectangle.getY());
			double xUpper = xAxis.getValueForDisplay(xAxis.parentToLocal(upper).getX()).doubleValue();

			DoubleDataset x = data.getAxis();
			DoubleDataset y = data.getData();
			LinearInterpolation li = new LinearInterpolation(x, y);
			int iLower = li.locate(xLower);
			int iUpper = li.locate(xUpper) + 1;
			return data.getRange(iLower, iUpper);
		}

		public XyData fit(XyData data) {
			DoubleDataset axis = data.getAxis();
			DoubleDataset y = data.getData();

			this.function = Fitter.GaussianFit(y, axis);
			DoubleDataset fitAxis = this.updateAxis(axis);
			DoubleDataset yFit = this.function.calculateValues(fitAxis);
			return new XyData(fitAxis, yFit);
		}

		private DoubleDataset updateAxis(DoubleDataset axis) {
			int numberOfPoints = 51;
			int nPoints = axis.getShape()[0];
			if (nPoints > 50) { return axis; }
			double x1 = 1.0;
			double y1 = axis.get(0);
			double y2 = axis.get(nPoints - 1);
			double slope = -(y1 - y2) / (double) (numberOfPoints - 1);
			double intercept = y1 - slope * x1;
			List<Double> x = new ArrayList<>();
			for (int i = 0; i < numberOfPoints; i++) {
				x.add((i + 1) * slope + intercept);
			}
			return (DoubleDataset) DatasetFactory.createFromList(x);
		}

		public void setYShift(double yShift) {
			this.yShift = yShift;
		}

		public void setYRange(double min, double max) {
			this.rectangle.setY(max);
			this.rectangle.setHeight(min - max);
			Color blue = Color.color(0, 0, 1, 0.3);
			rectangle.setStroke(blue);
			rectangle.setFill(blue);
		}

		public void setXMinimum(double min) {
			this.rectangle.setX(min);
			this.rectangle.setWidth(1);
		}

		public void setXMaximum(double max) {
			this.rectangle.setWidth(max - this.rectangle.getX());
		}

		public Rectangle getRectangle() {
			return this.rectangle;
		}

		public Function getFunction() {
			return this.function;
		}

		public Metadata getMetadata() {
			Metadata fitMetadata = function.getMetadata();
			fitMetadata.getHeaders().put(AFile.TITLE, function.getName() + " " + MetadataType.FIT);
			fitMetadata.getHeaders().put(AFile.TYPE, MetadataType.CURVE);
			fitMetadata.getHeaders().put(MetadataType.DATA_TYPE, MetadataType.FIT);
			fitMetadata.getHeaders().put(AsciiFile.Y_NAME, function.getName() + " " + MetadataType.FIT);
			fitMetadata.getHeaders().put(AsciiFile.PLOT, String.valueOf(Boolean.TRUE));
			return fitMetadata;
		}

	}

}
