package ncd.utils.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "scanConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "scan", "results" })
public class ScanConfiguration implements IConfiguration {

	private static final String XML = "./xml/scanFileConfiguration.xml";

	private static final String SCAN_FILENAME = "scanFile.txt";
	private static final String RESULTS_FILENAME = "results.xml";

	private ScanFileConfiguration scan = new ScanFileConfiguration("scan", SCAN_FILENAME);
	private ScanFileConfiguration results = new ScanFileConfiguration("results", RESULTS_FILENAME);

	public ScanConfiguration() {
		File file = new File(XML);
		if (!file.exists() || file.length() == 0) {
			this.scan.alert();
			this.results.alert();
		}
	}

	public ScanFileConfiguration getScan() {
		return scan;
	}

	public void setScan(ScanFileConfiguration scan) {
		this.scan = scan;
	}

	public ScanFileConfiguration getResults() {
		return results;
	}

	public void setResults(ScanFileConfiguration results) {
		this.results = results;
	}

	@Override
	public void read() {
		File file = new File(XML);
		if (!file.exists() || file.length() == 0) {
			return;
		}

		JAXBContext context;
		try {
			context = JAXBContext.newInstance(ScanConfiguration.class);
			Unmarshaller um = context.createUnmarshaller();
			ScanConfiguration configuration = (ScanConfiguration) um.unmarshal(new FileReader(XML));
			this.scan = configuration.getScan();
			this.results = configuration.getResults();
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void write() {
		File file = new File(XML);
		// create JAXB context and instantiate marshaller
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(ScanConfiguration.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(this, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void defaultConfiguration() {
		this.scan = new ScanFileConfiguration("scan", SCAN_FILENAME);
		this.results = new ScanFileConfiguration("results", RESULTS_FILENAME);
	}

	@Override
	public String toString() {
		return "ScanConfiguration [scan=" + scan + ", results=" + results + "]";
	}

}
