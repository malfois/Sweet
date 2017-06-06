package ncd.parameter;

public class Format {

	private IntegerRange	precision;
	private IntegerRange	scientificNotation;

	public Format() {
		this.precision = new IntegerRange(3, 0, 10);
		this.scientificNotation = new IntegerRange(3, 0, 10);
	}

	public IRange<Integer> getPrecisionRange() {
		return this.precision;
	}

	public IRange<Integer> getScientificNotationRange() {
		return this.scientificNotation;
	}

	public int getPrecision() {
		return precision.getValue();
	}

	public int getScientificNotation() {
		return scientificNotation.getValue();
	}

	public void setFormat(int precision, int scientificNotation) {
		this.setPrecision(precision);
		this.setScientificNotation(scientificNotation);
	}

	public Format getFormat() {
		Format format = new Format();
		format.setFormat(0, 2);
		return format;
	}

	public void setPrecision(int precision) {
		this.precision.setValue(precision);
	}

	public void setScientificNotation(int scientificNotation) {
		this.scientificNotation.setValue(scientificNotation);
	}

	@Override
	public String toString() {
		return "Format [precision=" + precision + ", scientificNotation=" + scientificNotation + "]";
	}

	public String toText(Number value) {
		if (value == null) {
			return "";
		}
		if (value.getClass().getSimpleName().equalsIgnoreCase(Double.class.getSimpleName())) {
			return this.toText(value.doubleValue());
		}
		if (value.getClass().getSimpleName().equalsIgnoreCase(Integer.class.getSimpleName())) {
			return this.toText(value.intValue());
		}
		return null;
	}

	public String toText(Integer value) {
		if (value == null) {
			return "";
		}

		String s = "0";
		double limit = Math.pow(10, this.scientificNotation.getValue());
		if (Math.abs(value) <= limit && Math.abs(value) >= 1.0 / limit) {
			s = String.format("%d", value);
		} else {
			if (value == 0) {
				s = String.format("%s", s);
			} else {
				s = String.format("%10." + this.precision.getValue() + "e", value.doubleValue());
			}
		}
		return s.trim();
	}

	public String toText(Double value) {
		if (value == null) {
			return "";
		}

		if (value.isNaN() || value.isInfinite()) {
			return value.toString();
		}

		String s = "0";
		double limit = Math.pow(10, this.scientificNotation.getValue());
		int precision = this.precision.getValue();
		if (Math.abs(value) <= limit && Math.abs(value) >= 1.0 / limit) {
			s = String.format("%10." + precision + "f", value);
		} else {
			if (value == 0.0) {
				s = String.format("%10." + precision + "f", value);
			} else {
				s = String.format("%10." + precision + "e", value);
			}
		}
		return s.trim();
	}

	public Format copy() {
		Format format = new Format();
		format.setFormat(this.precision.getValue(), this.scientificNotation.getValue());
		return format;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((precision == null) ? 0 : precision.hashCode());
		result = prime * result + ((scientificNotation == null) ? 0 : scientificNotation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Format other = (Format) obj;
		if (precision == null) {
			if (other.precision != null) return false;
		} else if (!precision.equals(other.precision)) return false;
		if (scientificNotation == null) {
			if (other.scientificNotation != null) return false;
		} else if (!scientificNotation.equals(other.scientificNotation)) return false;
		return true;
	}

	public String helpScientificNotation() {
		StringBuilder builder = new StringBuilder();
		builder.append("Scientific notation if ");
		builder.append("number < 10^-" + this.scientificNotation);
		builder.append(" or number > 10^" + this.scientificNotation + "\n");
		return builder.toString();
	}

	public String helpPrecision() {
		return "Number of digit after the dot.";
	}

}
