package ncd.gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import ncd.logger.NcdLogger;
import ncd.logger.TextAreaHandler;

public class ScenePane extends Scene {

	private static volatile ScenePane INSTANCE;

	public static ScenePane getInstance(String title, double arg0, double arg1) {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		ScenePane s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance(title, arg0, arg1);
		}
		return s;
	}

	private static synchronized ScenePane tryCreateInstance(String title, double arg0, double arg1) {
		if (INSTANCE == null) {
			INSTANCE = new ScenePane(title, arg0, arg1);
		}
		return INSTANCE;
	}

	private ScenePane(String title, double arg1, double arg2) {
		super(new BorderPane(), arg1, arg2);

		TextAreaHandler textAreaHandler = NcdLogger.getTextAreaHandler();

		BorderPane borderPane = (BorderPane) this.getRoot();

		borderPane.setBottom(textAreaHandler);

		ScrollPane sp = new ScrollPane();
		sp.setContent(borderPane);

		textAreaHandler.prefWidthProperty().bind(this.widthProperty());

	}

	public void setBottom(Node node) {
		BorderPane pane = (BorderPane) this.getRoot();
		pane.setBottom(node);
	}

	public void setCentre(Node node) {
		BorderPane pane = (BorderPane) this.getRoot();
		pane.setCenter(node);
	}

	public void setLeft(Node node) {
		BorderPane pane = (BorderPane) this.getRoot();
		pane.setLeft(node);
	}

	public void setRight(Node node) {
		BorderPane pane = (BorderPane) this.getRoot();
		pane.setRight(node);
	}

	public void setTop(Node node) {
		BorderPane pane = (BorderPane) this.getRoot();
		pane.setTop(node);
	}

}
