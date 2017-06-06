package ncd.gui.chart.renderer;

import java.util.List;
import java.util.Map;

import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.configuration.Renderers;
import ncd.utils.file.Metadata;
import ncd.utils.file.MetadataType;

public class MetadataRenderer extends Metadata {

	private Renderer renderer;

	public MetadataRenderer(Map<String, String> headers, Renderer renderer) {
		super(headers);
		this.renderer = renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void areAllVisible(Integer[] values) {
		if (this.getChildren().isEmpty()) {
			values[0]++;
			if (this.renderer.getLine().isVisible()) {
				values[1]++;
			}
			if (this.renderer.getSymbol().isVisible()) {
				values[2]++;
			}
		} else {
			for (Metadata mdata : this.getChildren()) {
				MetadataRenderer metadata = (MetadataRenderer) mdata;
				metadata.areAllVisible(values);
			}
		}
	}

	public void initialiseRenderer(Renderers configuration) {
		if (this.getChildren().isEmpty()) {
			Integer index = Integer.parseInt(this.getHeaders().get(MetadataType.CURVE_INDEX));
			this.renderer = configuration.getRenderer(index);
			return;
		}

		List<Metadata> metadataList = this.getChildren();
		for (Metadata metadata : metadataList) {
			MetadataRenderer item = (MetadataRenderer) metadata;
			item.initialiseRenderer(configuration);
		}
	}

	public void getRenderers(List<Renderer> renderers) {
		if (this.getChildren().isEmpty()) {
			renderers.add(this.renderer);
			return;
		}

		List<Metadata> metadataList = this.getChildren();
		for (Metadata metadata : metadataList) {
			MetadataRenderer item = (MetadataRenderer) metadata;
			item.getRenderers(renderers);
		}
	}

	public Renderer getRenderer(int index) {
		if (this.getChildren().isEmpty()) {
			int iRenderer = Integer.parseInt(this.getHeaders().get(MetadataType.CURVE_INDEX));
			if (iRenderer == index) { return this.renderer; }
			return null;
		}

		List<Metadata> metadataList = this.getChildren();
		for (Metadata metadata : metadataList) {
			MetadataRenderer item = (MetadataRenderer) metadata;
			Renderer r = item.getRenderer(index);
			if (r != null) { return r; }
		}
		return null;
	}
}
