package ncd.gui.chart.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncd.utils.colour.Palette1D;
import ncd.utils.configuration.AConfiguration;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filters;

@XmlRootElement(name = "renderer-list")
public class Renderers extends AConfiguration {

	public final static FileConfiguration	DEFAULT_FILE		= new FileConfiguration("./xml/", "Renderer", Filters.toFilters(Filters.Xml())[0]);

	private static final int				NUMBER_OF_RENDERERS	= 16;

	private List<Renderer>					renderers			= new ArrayList<>();

	public Renderers() {
		this.defaultSettings();
	}

	@XmlElement(name = "renderer")
	public List<Renderer> getRenderers() {
		return renderers;
	}

	public static FileConfiguration getDefaultFile(String name) {
		return new FileConfiguration("./xml/", name + "Renderer", Filters.toFilters(Filters.Xml())[0]);
	}

	public void setRenderers(List<Renderer> renderers) {
		this.renderers = renderers;
	}

	private void defaultConfiguration(int startIndex) {
		this.renderers = new ArrayList<>();
		if (startIndex > NUMBER_OF_RENDERERS) return;

		Palette1D.getInstance().reset();
		for (int i = startIndex; i < NUMBER_OF_RENDERERS; i++) {
			this.renderers.add(new Renderer());
		}
		Palette1D.getInstance().reset();
	}

	public void defaultSettings() {
		this.defaultConfiguration(0);
	}

	public void updateRenderer(List<Renderer> renderers) {
		int size = this.renderers.size();
		int i = 0;
		for (Renderer renderer : renderers) {
			if (i >= size) {
				this.renderers.add(renderer);
			} else {
				this.renderers.set(i, renderer);
			}
			i++;
		}
	}

	public Renderer getRenderer(int index) {
		int nRenderers = this.renderers.size();
		if (index >= nRenderers) {
			Renderer renderer = new Renderer();
			this.renderers.add(renderer);
			return renderer;
		}
		return this.renderers.get(index);
	}

	public void setListOfRenderers(List<Renderer> renderers) {
		this.renderers.clear();
		this.renderers = renderers;
	}

}
