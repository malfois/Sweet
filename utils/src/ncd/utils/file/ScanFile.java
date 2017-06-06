package ncd.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncd.data.XyData;
import ncd.utils.system.ListUtils;

public class ScanFile extends AsciiFile {

	private static final long	serialVersionUID	= -8235158491416904108L;

	public final static String	SCAN_COMMNAND		= "Scan command";
	public final static String	USER				= "User";
	public final static String	DATE				= "Date";
	public final static String	STARTING_DATE		= "Scan started";
	public final static String	ENDING_DATE			= "Scan ended";
	public final static String	SCAN_ID				= "Scan ID";
	public final static String	MOTOR				= "Motor";
	public final static String	MOTOR_INDEX			= "Motor index";
	public final static String	NUMBER_OF_SCANS		= "Number of scans";

	private String				line				= "";

	public ScanFile(String path) {
		super(path, "\t");
		Map<String, String> headers = new HashMap<>();
		headers.put(TITLE, this.getName());
		headers.put(DIRECTORY, this.getParent());
		headers.put(TYPE, MetadataType.FILE);
		this.metadata = new Metadata(headers);
	}

	@Override
	public void readData() {
		BufferedReader input = null;
		File file = this.getAbsoluteFile();
		try {
			input = new BufferedReader(new FileReader(file), 1);
			while (line != null) {
				Metadata scanMetadata = this.readScanHeader(input);
				if (scanMetadata == null) {
					break;
				}
				this.metadata.getChildren().add(scanMetadata) ;
				List<Metadata> metadata = scanMetadata.getChildren() ;
				this.readScanData(input, metadata);
			}
			this.metadata.getHeaders().put(NUMBER_OF_SCANS, String.valueOf(this.metadata.getChildren().size()));
			
			input.close();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readScanData(BufferedReader input, List<Metadata> metadata ) {
		try {
			List<List<Double>> val = new ArrayList<>() ; 
			while ((line = input.readLine()) != null) {
				if (line.startsWith("#C") && line.contains("ended")) {
					this.updateData(metadata, val);
					return ;
				}
				if (line.length() == 0) {
					this.updateData(metadata, val);
					return ;
				}
				String[] values = line.split(" ");
				int numberOfChannels = values.length;
				List<Double> dVal = new ArrayList<>() ;
				for (int iChannel = 0; iChannel < numberOfChannels; iChannel++) {
					String sValue = values[iChannel];
					Double dValue = Double.NaN;
					if (!sValue.equalsIgnoreCase("nan")) {
						dValue = Double.parseDouble(sValue);
					}
					dVal.add(dValue) ;										
				}
				val.add(dVal) ;
			}
			this.updateData(metadata, val);
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
	}

	private void updateData(List<Metadata> metadata, List<List<Double>> values) {
		values = ListUtils.transpose(values) ;

		int numberOfChannels = values.size() ;
		for (int i = 0; i < numberOfChannels; i++) {
			int motorIndex = Integer.parseInt(metadata.get(i).getHeaders().get(MOTOR_INDEX));
			List<Double> x = values.get(motorIndex);
			List<Double> y = values.get(i);
			this.data.add(new XyData(x, y));
		}	
		this.metadata.getHeaders().put(NUMBER_OF_CURVES, String.valueOf(numberOfChannels));
	}

	public Metadata readScanHeader(BufferedReader input) {
		try {
			List<Metadata> metadata;
			Map<String, String> textMetadata = new HashMap<>();
			textMetadata.put(TYPE, MetadataType.SCAN);
			while ((line = input.readLine()) != null) {
				if (line.startsWith("#S")) {
					this.getTitleInformation(line.substring(3), textMetadata);
				}
				if (line.startsWith("#U")) {
					textMetadata.put(USER, line.substring(3));
				}
				if (line.startsWith("#D")) {
					textMetadata.put(DATE, line.substring(3));
				}
				if (line.startsWith("#N")) {
					textMetadata.put(NUMBER_OF_CURVES, line.substring(3));
				}
				if (line.startsWith("#C") && line.contains("started")) {
					textMetadata.put(STARTING_DATE, line.substring(3));
				}
				if (line.startsWith("#L")) {
					String motorName = (String) textMetadata.get(MOTOR);
					metadata = this.readCurveHeader(motorName);
					return new Metadata(textMetadata, metadata);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Metadata> readCurveHeader(String motorName) {
		List<Metadata> metadata = new ArrayList<>();
		String[] names = line.substring(3).split("  ");
		int index = 0;
		int iName = 0;
		for (String name : names) {
			Map<String, String> textMetadata = new HashMap<String, String>();
			Boolean plot = true;
			if (name.contains("timer") || name.equalsIgnoreCase("Pt_No") || name.equalsIgnoreCase("dt") || name.equalsIgnoreCase("machine_current")
					|| name.equalsIgnoreCase(motorName)) {
				plot = false;
			}
			textMetadata.put(PLOT, plot.toString());
			textMetadata.put(X_NAME, motorName);
			textMetadata.put(TITLE, name);
			textMetadata.put(Y_NAME, name);
			textMetadata.put(TYPE, MetadataType.CURVE);
			textMetadata.put(MetadataType.DATA_TYPE, MetadataType.DATA);

			if (name.equalsIgnoreCase(motorName)) {
				index = iName;
			}
			iName++;
			metadata.add(new Metadata(textMetadata));
		}
		for (Metadata mdata : metadata) {
			mdata.getHeaders().put(MOTOR_INDEX, String.valueOf(index));
		}
		return metadata;
	}

	private void getTitleInformation(String title, Map<String, String> textMetadata) {
		String[] s = title.split(" ");
		textMetadata.put(SCAN_ID, s[0]);
		textMetadata.put(MOTOR, s[2]);
		String scanCommand = title.substring(s[0].length()).trim();
		textMetadata.put(SCAN_COMMNAND, scanCommand);
		textMetadata.put(TITLE, title);
	}

}
