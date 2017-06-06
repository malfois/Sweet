package ncd.gui.icon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileUtility {

	public static final String	UNKNOWN			= "Unknown";
	public static final String	SAVE_IMAGE		= "Save image";
	public static final String	UNZOOM			= "Unzoom";
	public static final String	PRINT			= "Print";
	public static final String	CONFIGURATION	= "Configuration";
	public static final String	OPEN_FILE		= "Open file";
	public static final String	LOAD_FILE		= "Load file";
	public static final String	ZOOM			= "Zoom";
	public static final String	FIT				= "Fit";

	public static ImageView get(String name) {
		ImageView imageView = new ImageView();
		if (name == null || name.length() == 0) return imageView;
		if (name.equalsIgnoreCase(SAVE_IMAGE)) return new ImageView(new Image("file:./icon/Save.png"));
		if (name.equalsIgnoreCase(CONFIGURATION)) return new ImageView(new Image("file:./icon/Settings.png"));
		if (name.equalsIgnoreCase(UNZOOM)) return new ImageView(new Image("file:./icon/unzoom.png"));
		if (name.equalsIgnoreCase(PRINT)) return new ImageView(new Image("file:./icon/print.png"));
		if (name.equalsIgnoreCase(OPEN_FILE)) return new ImageView(new Image("file:./icon/openFile.png"));
		if (name.equalsIgnoreCase(LOAD_FILE)) return new ImageView(new Image("file:./icon/upload.png"));
		if (name.equalsIgnoreCase(ZOOM)) return new ImageView(new Image("file:./icon/zoom.png"));
		if (name.equalsIgnoreCase(FIT)) return new ImageView(new Image("file:./icon/fit.png"));
		return imageView;
	}

}
