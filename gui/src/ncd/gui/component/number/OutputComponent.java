package ncd.gui.component.number;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import ncd.gui.component.ToolTipText;
import ncd.gui.component.initialvalue.CLabel;
import ncd.gui.component.initialvalue.RowPane;
import ncd.parameter.Format;

public class OutputComponent<T extends Number> extends RowPane {

	protected OutputComponent(String title, T number, Format format, String toolTipText) {
		super(new CLabel(title), new NumberLabel<T>(number, format));

		Label label = (Label) this.getComponent(0).getControl();
		label.setTooltip(new ToolTipText(toolTipText));

		this.setWidths(130, 130);

		this.setAlignment(Pos.CENTER_LEFT);

	}

	@SuppressWarnings("unchecked")
	protected void setFormat(Format format) {
		NumberLabel<T> nf = (NumberLabel<T>) this.getComponent(1);
		nf.setFormat(format);
	}

	@SuppressWarnings("unchecked")
	protected Format getFormat() {
		NumberLabel<T> nf = (NumberLabel<T>) this.getComponent(1);
		return nf.getFormat();
	}

}
