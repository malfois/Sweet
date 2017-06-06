package ncd.scan.client;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ncd.gui.component.PathPane;
import ncd.gui.component.ToolTipText;
import ncd.gui.icon.PaneUtility;
import ncd.scan.AConfigurationPane;
import ncd.scan.configuration.SpockFile;
import ncd.scan.spock.Spock;
import ncd.utils.file.Xml;

public class SpockPanel extends AConfigurationPane {

	private PathPane	scanPane;
	private PathPane	resultsPane;

	protected SpockPanel() {
		super(PaneUtility.get(PaneUtility.XML));
		this.getMenuButton().setTooltip(new ToolTipText("Configuration of the files related to Spock"));

		TitledPane pane = new TitledPane();
		pane.setText("Spock files NCD");
		pane.setExpanded(true);
		pane.setCollapsible(false);
		pane.setAlignment(Pos.CENTER);
		pane.setFont(Font.font(pane.getFont().getFamily(), FontWeight.BOLD, pane.getFont().getSize()));

		List<ColumnConstraints> columnConstraints = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			columnConstraints.add(new ColumnConstraints(100));
		}

		scanPane = new PathPane(columnConstraints, "Scan file", Spock.getInstance().getFileConfiguration().getScan());
		resultsPane = new PathPane(columnConstraints, "Results file", Spock.getInstance().getFileConfiguration().getResults());

		VBox box = new VBox(3);
		box.setPadding(new Insets(3));
		box.getChildren().addAll(this.scanPane, new Separator(), this.resultsPane);
		pane.setContent(box);

		initialise(pane);

	}

	@Override
	protected void save() {
		Spock.getInstance().getFileConfiguration().getScan().setDirectory(this.scanPane.getDirectory());
		Spock.getInstance().getFileConfiguration().getResults().setDirectory(this.resultsPane.getDirectory());
		Xml<SpockFile> xml = new Xml<>(SpockFile.DEFAULT_FILE, Spock.getInstance().getFileConfiguration());
		xml.write();
	}

}
