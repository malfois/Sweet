package ncd.gui.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.january.dataset.DoubleDataset;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import ncd.data.XyData;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.event.MousePositionEvent;
import ncd.logger.NcdLogger;
import ncd.parameter.Format;
import ncd.utils.maths.Line;
import ncd.utils.maths.nr.LinearInterpolation;
import ncd.utils.plot.GraphicScale;
import ncd.utils.plot.LinLin;

public class Chart extends LineChart<Number, Number> {

	private GraphicScale			graphicScale			= new LinLin();
	private Zoom					zoom					= new Zoom();

	private List<XyData>			dataset					= new ArrayList<>();
	private ObjectProperty<XyData>	selectedCurve			= new SimpleObjectProperty<>();

	private Marker					marker					= new Marker();

	final EventHandler<MouseEvent>	startZoomEventHandler	= new EventHandler<MouseEvent>() {

																public void handle(final MouseEvent mouseEvent) {
																	startZoom(mouseEvent);
																}
															};
	final EventHandler<MouseEvent>	zoomingEventHandler		= new EventHandler<MouseEvent>() {

																public void handle(final MouseEvent mouseEvent) {
																	zooming(mouseEvent);
																}
															};
	final EventHandler<MouseEvent>	zoomEventHandler		= new EventHandler<MouseEvent>() {

																public void handle(final MouseEvent mouseEvent) {
																	zoom();
																}
															};

	public Chart() {
		super(new NumberAxis(), new NumberAxis());

		this.setLegendVisible(false);
		this.setAnimated(false);

		this.setPadding(new Insets(3, 30, 3, 3));

		NumberAxis xAxis = (NumberAxis) this.getXAxis();
		xAxis.setTickLabelFormatter(new LabelFormat());
		xAxis.setForceZeroInRange(false);

		NumberAxis yAxis = (NumberAxis) this.getYAxis();
		yAxis.setTickLabelFormatter(new LabelFormat());
		yAxis.setForceZeroInRange(false);

		this.installZoomEventHandler();
		this.setOnMouseMoved(event -> mouseMotion(event));
	}

	public void installZoomEventHandler() {
		this.setOnMousePressed(this.startZoomEventHandler);
		this.setOnMouseDragged(this.zoomingEventHandler);
		this.setOnMouseReleased(this.zoomEventHandler);
	}

	public void removeZoomEventHandler() {
		this.setOnMousePressed(null);
		this.setOnMouseDragged(null);
		this.setOnMouseReleased(null);
	}

	public List<XyData> getDataset() {
		return this.dataset;
	}

	public void setDataset(List<XyData> dataset) {
		this.dataset.clear();
		this.addDataset(dataset);
	}

	public void addDataset(List<XyData> dataset) {
		for (XyData data : dataset) {
			this.dataset.add((XyData) data);
		}
	}

	public void addData(XyData data) {
		this.dataset.add((XyData) data);
	}

	public void setGraphicScale(GraphicScale graphicScale) {
		this.graphicScale = graphicScale;
	}

	public void clear() {
		this.getData().clear();
	}

	public ObjectProperty<XyData> selectedCurveProperty() {
		return selectedCurve;
	}

	public void setSelectedCurve(XyData curve) {
		this.selectedCurve.set(curve);
	}

	public void setSelectedCurve(Integer index) {
		XyData curve = null;
		if (index >= 0) {
			curve = this.dataset.get(index);
		}
		this.selectedCurve.set(curve);
	}

	public int getNumberOfCurves() {
		if (this.dataset == null) { return 0; }
		return this.dataset.size();
	}

	public void updateRenderer(List<Renderer> renderers) {
		int nRows = this.getData().size();
		if (nRows != renderers.size()) {
			NcdLogger.Warning(this.getClass().getSimpleName(), "updateRenderer", "Number of data and number of renderers different. Adjusting size");
			nRows = (nRows < renderers.size()) ? nRows : renderers.size();
		}
		for (int iRow = 0; iRow < nRows; iRow++) {
			XYChart.Series<Number, Number> series = this.getData().get(iRow);
			Renderer renderer = renderers.get(iRow);
			String lineStyle = renderer.getLine().getCSS();
			Node node = series.getNode().lookup(renderer.getLine().getCSSName(iRow));
			node.setStyle(lineStyle);
			Set<Node> nodes = this.lookupAll(renderer.getSymbol().getCSSName(iRow));
			String symbolStyle = renderer.getSymbol().getCSS();
			for (Node n : nodes) {
				n.setStyle(symbolStyle);
			}
		}

		// this.applyCss();
		// this.layout();
	}

	protected void replot(Map<Integer, Renderer> curves) {
		this.getData().clear();

		String xTitle = "x";
		String yTitle = "y";
		this.getXAxis().setLabel(xTitle);
		this.getYAxis().setLabel(yTitle);

		if (dataset == null || this.dataset.size() == 0) { return; }

		List<XYChart.Series<Number, Number>> seriesAll = new ArrayList<>();
		for (Map.Entry<Integer, Renderer> entry : curves.entrySet()) {
			Integer index = entry.getKey();
			XyData xyData = this.dataset.get(index);
			XYChart.Series<Number, Number> series = xyData.getXySeries();
			series = this.graphicScale.convert(series);
			seriesAll.add(series);
		}

		this.getData().addAll(seriesAll);
		List<Renderer> renderers = new ArrayList<>(curves.values());
		this.updateRenderer(renderers);

		NumberAxis xAxis = (NumberAxis) this.getXAxis();
		NumberAxis yAxis = (NumberAxis) this.getYAxis();
		double width = xAxis.getUpperBound() - xAxis.getLowerBound();
		double height = yAxis.getUpperBound() - yAxis.getLowerBound();
		Rectangle rectangle = new Rectangle(xAxis.getLowerBound(), yAxis.getLowerBound(), width, height);
		this.zoom.setNoZoomRectangle(rectangle);

	}

	public void addNode(Node node) {
		this.getChildren().add(node);
	}

	public void removeNode(Node node) {
		this.getChildren().remove(node);
	}

	private void startZoom(MouseEvent event) {

		Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());
		double xM = this.getXAxis().sceneToLocal(mouseSceneCoords).getX();
		double yM = this.getYAxis().sceneToLocal(mouseSceneCoords).getY();
		double xA = this.getXAxis().getValueForDisplay(xM).doubleValue();
		double yA = this.getYAxis().getValueForDisplay(yM).doubleValue();

		Point2D mouseCoords = new Point2D(event.getX(), event.getY());

		this.zoom.setStartPointAxis(new Point2D(xA, yA));
		this.zoom.setStartPointMouse(mouseCoords);
	}

	private void zooming(MouseEvent event) {
		this.removeZoomRectangle();

		Point2D mouseSceneCoords = new Point2D(event.getSceneX(), event.getSceneY());
		double xM = this.getXAxis().sceneToLocal(mouseSceneCoords).getX();
		double yM = this.getYAxis().sceneToLocal(mouseSceneCoords).getY();
		double xA = this.getXAxis().getValueForDisplay(xM).doubleValue();
		double yA = this.getYAxis().getValueForDisplay(yM).doubleValue();

		Point2D mouseCoords = new Point2D(event.getX(), event.getY());

		this.zoom.setEndPointAxis(new Point2D(xA, yA));
		this.zoom.setEndPointMouse(mouseCoords);
		if (zoom.isZooming()) {
			Rectangle rectangle = zoom.getRectangleMouse();
			this.getChildren().add(rectangle);
		}
	}

	private void zoom() {
		this.getChildren().remove(this.marker);
		Rectangle rectangle = this.zoom.getFullRangeRectangle();
		boolean autoRanging = true;
		if (this.zoom.isZooming()) {
			this.removeZoomRectangle();
			rectangle = this.zoom.getRectangleAxis();
			autoRanging = false;
		}
		this.adjustAxis(rectangle, autoRanging);
	}

	public void unzoom() {
		Rectangle rectangle = this.zoom.getFullRangeRectangle();
		boolean autoRanging = true;
		this.adjustAxis(rectangle, autoRanging);
	}

	private void adjustAxis(Rectangle rectangle, boolean autoRanging) {
		double xStart = rectangle.getX();
		double yStart = rectangle.getY();
		double xEnd = rectangle.getWidth() + rectangle.getX();
		double yEnd = rectangle.getHeight() + rectangle.getY();
		NumberAxis xAxis = (NumberAxis) this.getXAxis();
		NumberAxis yAxis = (NumberAxis) this.getYAxis();
		xAxis.setAutoRanging(autoRanging);
		yAxis.setAutoRanging(autoRanging);
		xAxis.setLowerBound(xStart);
		xAxis.setUpperBound(xEnd);
		double lowerBound = (yStart > yEnd) ? yEnd : yStart;
		double upperBound = (yStart > yEnd) ? yStart : yEnd;
		yAxis.setLowerBound(lowerBound);
		yAxis.setUpperBound(upperBound);
	}

	private void removeZoomRectangle() {
		ObservableList<Node> nodes = this.getChildren();
		List<Node> nodesToRemove = new ArrayList<Node>();
		for (Node node : nodes) {
			if (node instanceof Rectangle) {
				nodesToRemove.add(node);
				break;
			}
		}
		this.getChildren().removeAll(nodesToRemove);

	}

	private void mouseMotion(MouseEvent event) {
		if (selectedCurve.getValue() == null) { return; }

		this.getChildren().remove(this.marker);
		Point2D dataCoords = this.pixelToData(new Point2D(event.getX(), event.getY()));
		double xData = dataCoords.getX();

		XyData curve = selectedCurve.getValue();
		DoubleDataset x = curve.getAxis();
		DoubleDataset y = curve.getData();
		int nPoints = x.getShape()[0];

		if (xData < x.get(0) || xData > x.get(nPoints - 1)) {
			MousePositionEvent e = new MousePositionEvent(null);
			this.fireEvent(e);
			return;
		}

		LinearInterpolation li = new LinearInterpolation(x, y);
		int index = li.locate(xData) - 1;

		Point2D value = new Point2D(x.get(nPoints - 1), y.get(nPoints - 1));
		if (index < nPoints - 1) {
			Point2D point1 = new Point2D(x.get(index), y.get(index));
			Point2D point2 = new Point2D(x.get(index + 1), y.get(index + 1));
			Line line = new Line(point1, point2);
			value = new Point2D(xData, line.calculate(xData));
		}

		value = this.graphicScale.to(value.getX(), value.getY());

		NumberAxis xAxis = (NumberAxis) this.getXAxis();
		double xLower = xAxis.getLowerBound();
		double xUpper = xAxis.getUpperBound();
		NumberAxis yAxis = (NumberAxis) this.getYAxis();
		double yLower = yAxis.getLowerBound();
		double yUpper = yAxis.getUpperBound();
		Rectangle rectangle = new Rectangle(xLower, yLower, xUpper - xLower, yUpper - yLower);
		if (rectangle.contains(value)) {
			Point2D pixel = this.dataToPixel(new Point2D(value.getX(), value.getY()));
			this.marker.setLocation(pixel.getX(), pixel.getY());
			this.getChildren().add(this.marker);
		}
		MousePositionEvent e = new MousePositionEvent(new Point2D(value.getX(), value.getY()));
		this.fireEvent(e);
	}

	private Point2D pixelToData(Point2D pixel) {
		NumberAxis xAxis = (NumberAxis) this.getXAxis();
		double x = xAxis.getValueForDisplay(xAxis.parentToLocal(pixel).getX()).doubleValue();
		NumberAxis yAxis = (NumberAxis) this.getYAxis();
		double y = yAxis.getValueForDisplay(yAxis.parentToLocal(pixel).getY()).doubleValue();
		return new Point2D(x, y);
	}

	private Point2D dataToPixel(Point2D data) {
		NumberAxis xAxis = (NumberAxis) this.getXAxis();
		double xPixel = xAxis.getDisplayPosition(data.getX());
		NumberAxis yAxis = (NumberAxis) this.getYAxis();
		double yPixel = yAxis.getDisplayPosition(data.getY());
		Point2D pixel = new Point2D(xPixel, yPixel);
		double x = xAxis.localToParent(pixel).getX() + this.getInsets().getLeft();
		double y = yAxis.localToParent(pixel).getY() + this.getInsets().getTop();
		return new Point2D(x, y);
	}

	private class Marker extends Circle {

		public Marker() {
			setFill(Color.BLACK);
			setRadius(4.0);
		}

		public void setLocation(double x, double y) {
			setCenterX(x);
			setCenterY(y);
		}
	}

	private class LabelFormat extends StringConverter<Number> {

		private Format format = new Format();

		@Override
		public String toString(Number object) {
			if (object == null) { return null; }

			return this.format.toText(object.doubleValue());
		}

		@Override
		public Number fromString(String string) {
			return (string != null ? new Double(string) : new Double(0));
		}

	}

	private class Zoom {

		private Rectangle	noZoom;
		private Rectangle	mouse	= new Rectangle();
		private Rectangle	axis	= new Rectangle();

		public void setNoZoomRectangle(Rectangle noZoom) {
			this.noZoom = noZoom;

		}

		public void setStartPointAxis(Point2D point) {
			this.axis.setX(point.getX());
			this.axis.setY(point.getY());
		}

		public void setEndPointAxis(Point2D point) {
			double width = point.getX() - this.axis.getX();
			double height = point.getY() - this.axis.getY();
			this.axis.setWidth(width);
			this.axis.setHeight(height);
		}

		public void setStartPointMouse(Point2D point) {
			this.mouse.setX(point.getX());
			this.mouse.setY(point.getY());

			Color gray = Color.GRAY;
			Color color = Color.rgb((int) (gray.getRed() / 255.0), (int) (gray.getGreen() / 255.0), (int) (gray.getBlue() / 255.0), 0.3);
			this.mouse.setStroke(color);
			this.mouse.setFill(color);

		}

		public void setEndPointMouse(Point2D point) {
			double width = point.getX() - this.mouse.getX();
			double height = point.getY() - this.mouse.getY();
			this.mouse.setWidth(width);
			this.mouse.setHeight(height);
		}

		public boolean isZooming() {
			double width = this.axis.getWidth();
			if (width < 0) { return false; }
			return true;
		}

		public Rectangle getRectangleMouse() {
			return this.mouse;
		}

		public Rectangle getRectangleAxis() {

			return this.axis;
		}

		public Rectangle getFullRangeRectangle() {
			return this.noZoom;
		}

		// public void reset() {
		// this.mouse = new Rectangle();
		// this.axis = new Rectangle();
		// }

		@Override
		public String toString() {
			return "Zoom [noZoom=" + noZoom + ", mouse=" + mouse + ", axis=" + axis + "]";
		}

	}

}
