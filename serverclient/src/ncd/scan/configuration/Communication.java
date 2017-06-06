package ncd.scan.configuration;

import javax.xml.bind.annotation.XmlRootElement;

import ncd.utils.configuration.AConfiguration;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filter;

@XmlRootElement(name = "Connection")
public class Communication extends AConfiguration {

	public final static FileConfiguration	DEFAULT_FILE	= new FileConfiguration("./xml", "communication", Filter.XML);

	private int								port;
	private String							hostName;

	public Communication() {
	}

	public int getPort() {
		return port;
	}

	public String getPortText() {
		return (port < 0) ? "N/A" : String.valueOf(port);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	// public Boolean fileExists() {
	// return fileConfiguration.exists();
	// }
	//
	// public FileConfiguration getXmlFile() {
	// return this.fileConfiguration;
	// }

	@Override
	public void defaultSettings() {
		this.port = -1;
		this.hostName = "N/A";
	}

	@Override
	public String toString() {
		return "Communication [port=" + port + ", hostName=" + hostName + "]";
	}

}
