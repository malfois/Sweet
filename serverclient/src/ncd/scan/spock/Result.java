package ncd.scan.spock;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ncd.utils.configuration.AConfiguration;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Filter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "scanID", "motor", "minVal", "maxVal", "pos", "edge" })
public class Result extends AConfiguration {

	public final static FileConfiguration	DEFAULT_FILE	= new FileConfiguration("./spock", "results", Filter.XML);

	private String							motor;
	private Value							minVal;
	private Value							maxVal;
	private Fit								pos;
	private Fit								edge;
	private String							scanID;

	public Result() {
	}

	public void update(String scanID, String motor, Value minVal, Value maxVal, Fit position, Fit edge) {
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.pos = position;
		this.edge = edge;
		this.motor = motor;
		this.scanID = scanID;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public Fit getPos() {
		return pos;
	}

	public void setPosition(Fit position) {
		this.pos = position;
	}

	public Fit getEdge() {
		return edge;
	}

	public void setEdge(Fit edge) {
		this.edge = edge;
	}

	public Value getMinVal() {
		return minVal;
	}

	public void setMinVal(Value minVal) {
		this.minVal = minVal;
	}

	public Value getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Value maxVal) {
		this.maxVal = maxVal;
	}

	public String getScanID() {
		return scanID;
	}

	public void setScanID(String scanID) {
		this.scanID = scanID;
	}

	// public void write() {
	// if (this.fileConfiguration.getFileConfiguration().getCanonicalPath() == null || this.fileConfiguration.get.length() == 0) { return; }
	// File file;
	// // create JAXB context and instantiate marshaller
	// JAXBContext context;
	// try {
	// file = new File(OSUtils.getAbsolutePath(this.fileConfiguration.getPath()));
	// context = JAXBContext.newInstance(Result.class);
	// Marshaller m = context.createMarshaller();
	// m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	// m.marshal(this, file);
	// } catch (FileNotFoundException e1) {
	// e1.printStackTrace();
	// } catch (JAXBException e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void defaultSettings() {
	}

}
