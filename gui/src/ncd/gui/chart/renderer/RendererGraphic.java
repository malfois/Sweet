package ncd.gui.chart.renderer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import ncd.gui.chart.configuration.Renderer;

public class RendererGraphic extends StackPane {

	private final static int			LINE_INDEX		= 0;
	private final static int			SYMBOL_INDEX	= 1;

	private ObjectProperty<Renderer>	renderer		= new SimpleObjectProperty<>(new Renderer());

	public RendererGraphic(Renderer renderer) {
		this.getChildren().addAll(renderer.getLine().getGraphic(), renderer.getSymbol().getGraphic());
		this.renderer.set(renderer);
		this.renderer.addListener((observable, oldValue, newValue) -> update());
	}

	public RendererGraphic() {
		this.renderer.addListener((observable, oldValue, newValue) -> update());
	}

	private void update() {
		this.getChildren().clear();
		Renderer r = this.renderer.getValue();
		this.getChildren().add(r.getLine().getGraphic());
		if (r.getSymbol().isVisible()) {
			this.getChildren().add(r.getSymbol().getGraphic());
		}
	}

	public Line getLine() {
		return (Line) this.getChildren().get(LINE_INDEX);
	}

	public SVGPath getSymbol() {
		return (SVGPath) this.getChildren().get(SYMBOL_INDEX);
	}

	public ObjectProperty<Renderer> rendererProperty() {
		return this.renderer;
	}

	public Renderer getRenderer() {
		return this.renderer.get();
	}

	public void setRenderer(Renderer renderer) {
		this.renderer.set(renderer);
	}

}
