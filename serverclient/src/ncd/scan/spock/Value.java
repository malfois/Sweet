package ncd.scan.spock;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "position", "value" })
public class Value {

	private double	position;
	private double	value;

	public Value(double position, double value) {
		this.position = position;
		this.value = value;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Value [position=" + position + ", value=" + value + "]";
	}

}
