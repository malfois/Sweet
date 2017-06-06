package ncd.gui.chart.configuration;

import javax.xml.bind.annotation.XmlType;

import javafx.scene.paint.Color;
import ncd.gui.chart.renderer.RendererGraphic;
import ncd.utils.colour.Palette1D;
import ncd.utils.configuration.AConfiguration;

@XmlType(propOrder = { "line", "symbol" })
public class Renderer extends AConfiguration {

	private Stroke	line;
	private Symbol	symbol;

	public Renderer() {
		super();
	}

	public Renderer(Stroke line, Symbol symbol) {
		this.line = line;
		this.symbol = symbol;
	}

	public Stroke getLine() {
		return line;
	}

	public void setLine(Stroke line) {
		this.line = line;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public void defaultSettings() {
		Color c = Palette1D.getInstance().getNextColor();
		this.line = new Stroke(c);
		this.symbol = new Symbol(c);
	}

	public RendererGraphic getGraphic() {
		return new RendererGraphic(this);
	}

	// public void update(Renderer renderer) {
	// this.line = renderer.getLine();
	// this.symbol = renderer.getSymbol();
	// }

	@Override
	public String toString() {
		return "RendererConfiguration [line=" + line + ", symbol=" + symbol + "]";
	}
}
