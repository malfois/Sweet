package ncd.fileviewer;

import javafx.scene.layout.Region;

public interface IFileViewer {

	public void update(String path);

	public Region getRegion();

	public String getPath();

	public void reset();
}
