package ncd.fileviewer;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Shark extends Application {

	private TreeFile		treeFile		= new TreeFile();
	private DirectoryPane	directoryPane	= new DirectoryPane();
	private FileViewer		fileViewer		= new FileViewer();

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane borderPane = new BorderPane();
		Scene scene = new Scene(borderPane, 850, 650);

		File f = new File("//beamlines/bl11/controls/scans/icon/shark.png");
		Image icon = new Image(f.toURI().toString());
		primaryStage.getIcons().add(icon);

		borderPane.setTop(this.directoryPane);
		borderPane.setLeft(treeFile);
		borderPane.setCenter(this.fileViewer);

		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setTitle("Shark - version 1.0");
		primaryStage.setOnCloseRequest(event -> closeApplication(event));

		this.fileViewer.getScrollPane().prefHeightProperty().bind(this.treeFile.heightProperty());

		fileViewer.addEventHandler(FileEvent.UPDATE_DIRECTORY, event -> updateDirectory((FileEvent<File>) event));
		treeFile.addEventHandler(FileEvent.UPDATE_DIRECTORY, event -> updateDirectory((FileEvent<File>) event));
		directoryPane.addEventHandler(FileEvent.UPDATE_DIRECTORY, event -> updateDirectory((FileEvent<File>) event));
	}

	private void updateDirectory(String path) {
		directoryPane.add(path);
		fileViewer.update(path);
	}

	private void updateDirectory(FileEvent<File> event) {
		File file = event.getResult();
		if (file == null) return;
		this.updateDirectory(file.getAbsolutePath());
	}

	private void closeApplication(WindowEvent event) {
		Platform.exit();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
