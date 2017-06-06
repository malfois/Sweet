package ncd.gui.beamline;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import ncd.beamline.Diagnostic;

public class DiagnosticPane extends VBox {

	List<CheckBox> diagnostics = new ArrayList<>();

	public DiagnosticPane() {
		super(3);
		// setText("Diagnostics");
		// setExpanded(true);

		setPadding(new Insets(5, 0, 5, 0));

		Label text = new Label("Diagnostics");
		text.setAlignment(Pos.CENTER);
		Font defaultFont = Font.getDefault();
		Font font = Font.font(defaultFont.getName(), FontWeight.MEDIUM, FontPosture.ITALIC, 14);
		text.setFont(font);

		text.prefWidthProperty().bind(this.widthProperty());

		getChildren().add(text);
		for (String diagnostic : Diagnostic.DIAGNOSTICS) {
			CheckBox checkBox = new CheckBox(diagnostic);
			checkBox.setMnemonicParsing(false);
			getChildren().add(checkBox);
		}

	}
}
