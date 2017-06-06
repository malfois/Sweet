package ncd.scan.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ncd.scan.spock.Result;
import ncd.utils.configuration.AConfiguration;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "scan", "results" })
public class SpockFile extends AConfiguration {

	@XmlTransient
	public final static FileConfiguration	DEFAULT_FILE	= new FileConfiguration("./spock/", "spock", Filter.XML);

	private FileConfiguration				scan;
	private FileConfiguration				results;

	public SpockFile() {
		this.defaultSettings();
	}

	public FileConfiguration getScan() {
		return scan;
	}

	public void setScan(FileConfiguration scan) {
		this.scan = scan;
	}

	public FileConfiguration getResults() {
		return results;
	}

	public void setResults(FileConfiguration results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "ScanConfiguration [scan=" + scan + ", results=" + results + "]";
	}

	// public List<String> printConfiguration() {
	// File file = new File(this.fileConfiguration.getCanonicalPath());
	// List<String> texts = new ArrayList<>();
	// try {
	// texts.add("Scan Configuration file: " + file.getCanonicalPath());
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// texts.add("Configuration file: " + this.scan.getCanonicalPath());
	// texts.add("Configuration File exists: " + OSUtils.pathExists(this.scan.getCanonicalPath()));
	// texts.add("Results file: " + this.results.getCanonicalPath());
	// texts.add("Results File exists: " + OSUtils.pathExists(this.results.getCanonicalPath()));
	// return texts;
	// }

	@Override
	public void defaultSettings() {
		scan = new FileConfiguration("./spock", "scanFile", Filter.TXT);
		results = Result.DEFAULT_FILE;
	}
}
