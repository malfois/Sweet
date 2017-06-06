package ncd.gui.icon;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.filechooser.FileSystemView;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileSystem implements Runnable {

	private File	file;
	private Label	label;

	public FileSystem(File file, Label label) {
		if (file == null || !file.exists()) { return; }

		this.file = file;
		this.label = label;
	}

	@Override
	public void run() {
		// commented code always returns the same icon on OS X...
		FileSystemView view = FileSystemView.getFileSystemView();
		javax.swing.Icon icon = view.getSystemIcon(file);

		if (icon == null) { return; }
		// following code returns different icons for different types on OS X...
		// final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		// javax.swing.Icon icon = fc.getUI().getFileView(fc).getIcon(this.file);

		BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

		Platform.runLater(() -> {
			Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
			ImageView imageView = new ImageView(fxImage);
			this.label.setGraphic(imageView);
		});
	}

}
