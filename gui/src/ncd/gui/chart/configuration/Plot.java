package ncd.gui.chart.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncd.utils.configuration.AConfiguration;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filter;
import ncd.utils.file.Filters;
import ncd.utils.plot.GraphicScale;
import ncd.utils.plot.LinLin;

@XmlRootElement(name = "plot-configuration")
public class Plot extends AConfiguration {

	public final static FileConfiguration	DEFAULT_FILE	= new FileConfiguration("./xml", "Plot", Filter.XML);

	private String							name;

	private FileConfiguration				imageFile;
	private FileConfiguration				outputDataFile;
	private String							scale;
	private FileConfiguration				rendererFile;

	public Plot() {
	}

	public Plot(String name) {
		this.name = name;
	}

	@XmlElement(name = "output-data-file")
	public FileConfiguration getOutputDataFile() {
		return outputDataFile;
	}

	public void setOutputDataFile(FileConfiguration outputDataFile) {
		this.outputDataFile = outputDataFile;
	}

	@XmlElement(name = "image-file")
	public FileConfiguration getImageFile() {
		return imageFile;
	}

	public void setImageFile(FileConfiguration imageFile) {
		this.imageFile = imageFile;
	}

	@XmlElement(name = "graphic-scale")
	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	@XmlElement(name = "renderer-file")
	public FileConfiguration getRendererFile() {
		return rendererFile;
	}

	public void setRendererFile(FileConfiguration rendererFile) {
		this.rendererFile = rendererFile;
	}

	public GraphicScale getGraphicScale() {
		return GraphicScale.Factory(this.scale);
	}

	public void defaultSettings() {
		// create configuration, assigning data
		this.imageFile = new FileConfiguration("image", Filters.toFilters(Filters.Image())[0]);
		this.scale = new LinLin().toString();
		this.rendererFile = Renderers.getDefaultFile(name);
		this.outputDataFile = new FileConfiguration("data", Filters.toFilters(Filters.Ascii())[0]);
	}

	@Override
	public String toString() {
		return "PlotConfiguration [imageFile=" + imageFile + ", outputDataFile=" + outputDataFile + ", scale=" + scale + ", rendererFile=" + rendererFile + "]";
	}

}
