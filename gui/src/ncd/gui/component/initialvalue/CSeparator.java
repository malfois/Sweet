package ncd.gui.component.initialvalue;

import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import ncd.logger.NcdLogger;

public class CSeparator extends HBox {

	private Separator	l1		= new Separator();
	private CLabel		label	= new CLabel("");
	private Separator	l2		= new Separator();

	public CSeparator(String text) {
		this.label.setText(text);
		this.getChildren().addAll(this.l1, this.label, this.l2);

		this.setAlignment(Pos.CENTER);
		this.label.setAlignment(Pos.CENTER);

	}

	public void setWidths(final double... widths) {
		int nChildren = this.getChildren().size();
		if (widths.length != nChildren) {
			NcdLogger.Error(this.getClass().getSimpleName(), "setWidths", "Number of components and number of widths different");
			return;
		}

		this.l1.setPrefWidth(widths[0]);
		this.label.setPrefWidth(widths[1]);
		this.l2.setPrefWidth(widths[2]);

	}

}
