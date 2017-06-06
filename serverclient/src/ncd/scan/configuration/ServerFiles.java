package ncd.scan.configuration;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ncd.utils.configuration.AConfiguration;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filter;
import ncd.utils.file.Xml;

@XmlRootElement(name = "Server")
@XmlType(propOrder = { "serverConnection", "jreFile", "jarFile" })
public class ServerFiles extends AConfiguration {

	@XmlTransient
	public final static FileConfiguration	DEFAULT_FILE	= new FileConfiguration("./xml", "serverFiles", Filter.XML);

	private FileConfiguration				serverConnection;
	private FileConfiguration				jarFile;
	private FileConfiguration				jreFile;

	public ServerFiles() {
		this.defaultSettings();
	}

	public void setServerConnection(FileConfiguration serverConnection) {
		this.serverConnection = serverConnection;
	}

	public FileConfiguration getJreFile() {
		return this.jreFile;
	}

	public void setJreFile(FileConfiguration jreFile) {
		this.jreFile = jreFile;
	}

	public FileConfiguration getJarFile() {
		return this.jarFile;
	}

	public void setJarFile(FileConfiguration jarFile) {
		this.jarFile = jarFile;
	}

	public Communication getCommunication() {
		Xml<Communication> xml = new Xml<>(this.serverConnection, new Communication());
		xml.read();
		return xml.getConfiguration();
	}

	public String[] getCommandLine() {
		String[] text = new String[3];
		File jre = new File(this.jreFile.getCanonicalPath());
		File jar = new File(this.jarFile.getCanonicalPath());
		try {
			text[0] = jre.getCanonicalPath();
			text[1] = "-jar";
			text[2] = jar.getCanonicalPath();
			return text;
		} catch (IOException e) {
			return null;
		}
	}

	public FileConfiguration getServerConnection() {
		return this.serverConnection;
	}

	@Override
	public void defaultSettings() {
		serverConnection = Communication.DEFAULT_FILE;
		jarFile = new FileConfiguration(".", "scanFitServer", Filter.JAR);
		jreFile = new FileConfiguration(System.getProperty("java.home"), "java", Filter.JRE);
		// this.readConnectionConfiguration();
	}

	@Override
	public String toString() {
		return "ServerFiles [serverConnection=" + serverConnection + ", jarFile=" + jarFile + ", jreFile=" + jreFile + "]";
	}

	// private void readConnectionConfiguration() {
	// Xml<Communication> fileConfiguration = new Xml<>(this.connectionConfiguration);
	// fileConfiguration.read();
	// this.connectionConfiguration = fileConfiguration.getConfiguration();
	// }

}
