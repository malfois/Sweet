package ncd.utils.plot;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import ncd.utils.colour.Palette1D;

/**
 * define the renderer of the curve displayed in JFreeChart
 *
 * @author mm54
 *
 */
public class Renderer extends StackPane {

	private Symbol	symbol			= new Symbol(RendererUtility.CIRCLE, Color.BLACK, 8.0);
	private boolean	symbolVisible	= true;

	private Stroke	line;

	public Renderer() {
		Color color = Palette1D.getInstance().getNextColor();
		this.symbol.setFill(color);
		this.symbol.setStroke(color);
		this.symbol.setVisible(this.symbolVisible);
		line = new Stroke(color, Stroke.DEFAULT_WIDTH, true);

		this.getChildren().addAll(this.line, this.symbol);
	}

	public Renderer(Stroke line, Symbol symbol, boolean symbolVisible) {
		this.line = line;
		this.symbol = symbol;
		this.symbolVisible = symbolVisible;
		this.getChildren().addAll(this.line, this.symbol);
	}

	public boolean isSymbolVisible() {
		return symbolVisible;
	}

	public Stroke getLine() {
		return this.line;
	}

	public void setLine(Stroke line) {
		this.line = line;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return this.symbol;
	}

	protected static String toRGB(Color color) {
		return String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255), (int) (color.getOpacity() * 255));
	}

	public Renderer clone() {
		Stroke line = this.line.clone();
		Symbol symbol = this.symbol.clone();
		return new Renderer(line, symbol, this.symbolVisible);
	}

	@Override
	public String toString() {
		return "Renderer [symbol=" + symbol + ", symbolVisible=" + symbolVisible + ", line=" + line + "]";
	}

}
