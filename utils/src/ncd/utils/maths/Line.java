package ncd.utils.maths;

import javafx.geometry.Point2D;

public class Line {

	private double	slope;
	private double	intercept;

	public Line(Point2D point1, Point2D point2) {
		this.slope = (point1.getY() - point2.getY()) / (point1.getX() - point2.getX());
		this.intercept = point1.getY() - this.slope * point1.getX();
	}

	public double calculate(double x) {
		return this.slope * x + this.intercept;
	}

	public double getSlope() {
		return slope;
	}

	public double getIntercept() {
		return intercept;
	}

}
