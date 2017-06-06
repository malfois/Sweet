package ncd.gui.chart.renderer;

import java.util.ArrayList;
import java.util.List;

import ncd.gui.chart.configuration.Renderer;
import ncd.utils.configuration.FileConfiguration;

public class DialogParameter {

	private FileConfiguration		fileConfiguration;
	private List<MetadataRenderer>	metadataRenderers;

	public DialogParameter(FileConfiguration fileConfiguration, List<MetadataRenderer> metadataRenderers) {
		super();
		this.fileConfiguration = fileConfiguration;
		this.metadataRenderers = metadataRenderers;
	}

	public FileConfiguration getFileConfiguration() {
		return fileConfiguration;
	}

	public List<MetadataRenderer> getMetadataRenderers() {
		return metadataRenderers;
	}

	public void setFileConfiguration(FileConfiguration fileConfiguration) {
		this.fileConfiguration = fileConfiguration;
	}

	public List<Renderer> getRenderers() {
		List<Renderer> renderers = new ArrayList<>();
		for (MetadataRenderer metadata : metadataRenderers) {
			metadata.getRenderers(renderers);
		}
		return renderers;
	}

	public Renderer getRenderer(int index) {
		for (MetadataRenderer metadata : metadataRenderers) {
			Renderer renderer = metadata.getRenderer(index);
			if (renderer != null) { return renderer; }
		}
		return null;
	}
}
