package ncd.gui.chart;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import ncd.gui.component.ToolTipText;
import ncd.gui.event.ActionOnAllPlotEvent;
import ncd.gui.event.ContextMenuEvent;

public class DefaultActionPane extends FlowPane {

	private Button	clear			= new Button("Clear All");
	private Button	plot			= new Button("Plot All");
	private Button	configuration	= new Button("Configuration");

	public DefaultActionPane() {
		this.setPadding(new Insets(5, 0, 5, 0));

		this.setPadding(new Insets(5, 5, 5, 5));
		this.setVgap(4);
		this.setHgap(4);
		this.getChildren().addAll(this.clear, this.plot, this.configuration);

		this.clear.setTooltip(new ToolTipText("All plots will be cleared."));
		this.plot.setTooltip(new ToolTipText("Plot all curves"));
		this.configuration.setTooltip(new ToolTipText("Renderer will be changed"));

		this.clear.setDisable(true);
		this.plot.setDisable(true);
		this.configuration.setDisable(true);

		this.plot.setOnAction(this::plotAll);
		this.clear.setOnAction(this::clearAll);
		this.configuration.setOnAction(this::rendererChanged);
	}

	public Button getConfiguration() {
		return configuration;
	}

	public Button getClear() {
		return clear;
	}

	public Button getPlot() {
		return plot;
	}

	private void rendererChanged(ActionEvent event) {
		event.consume();
		ActionOnAllPlotEvent e = new ActionOnAllPlotEvent(ContextMenuEvent.RENDERER_ACTION);
		this.fireEvent(e);
	}

	private void clearAll(ActionEvent event) {
		event.consume();
		ActionOnAllPlotEvent e = new ActionOnAllPlotEvent(ActionOnAllPlotEvent.CLEAR_ALL);
		this.fireEvent(e);
	}

	private void plotAll(ActionEvent event) {
		event.consume();
		ActionOnAllPlotEvent e = new ActionOnAllPlotEvent(ActionOnAllPlotEvent.PLOT_ALL);
		this.fireEvent(e);
	}

	public void setEnabled(boolean enabled) {
		this.clear.setDisable(!enabled);
		this.plot.setDisable(!enabled);
	}

}
