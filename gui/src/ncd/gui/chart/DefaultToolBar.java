package ncd.gui.chart;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import ncd.gui.icon.ButtonUtility;
import ncd.gui.icon.FileUtility;
import ncd.gui.icon.PaneUtility;
import ncd.gui.icon.ToggleButtonUtility;
import ncd.utils.plot.GraphicScale;
import ncd.utils.plot.LinLin;
import ncd.utils.plot.LinLog;

public class DefaultToolBar extends ToolBar {

	private Button			unzoom	= ButtonUtility.getButton(FileUtility.UNZOOM);
	private ToggleButton	scale	= ToggleButtonUtility.getButton(PaneUtility.LOG_Y);
	private Button			save	= ButtonUtility.getButton(FileUtility.SAVE_IMAGE);
	private Button			print	= ButtonUtility.getButton(FileUtility.PRINT);

	public DefaultToolBar() {
		getItems().addAll(this.scale, this.unzoom, new Separator(), this.save, this.print);
	}

	public GraphicScale getGraphicScale() {
		GraphicScale graphicScale = new LinLin();
		if (this.scale.isSelected()) graphicScale = new LinLog();
		return graphicScale;
	}

}
