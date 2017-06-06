package ncd.scan.spock;

import javax.xml.bind.annotation.XmlType;

import ncd.utils.maths.fitting.Gaussian;

@XmlType(propOrder = { "position", "fwhm", "area", "offset" })
public class Fit {

	@Override
	public String toString() {
		return "Fit [position=" + position + ", fwhm=" + fwhm + ", area=" + area + ", offset=" + offset + "]";
	}

	private double	position;
	private double	fwhm;
	private double	area;
	private double	offset;

	public Fit() {
	}

	public Fit(Gaussian gaussian) {
		this.position = gaussian.getPosition();
		this.fwhm = gaussian.getFWHM();
		this.area = gaussian.getArea();
		this.offset = gaussian.getOffset();
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getFwhm() {
		return fwhm;
	}

	public void setFwhm(double fwhm) {
		this.fwhm = fwhm;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

}
