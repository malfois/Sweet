package ncd.beamline;

import java.util.Arrays;
import java.util.List;

public class Diagnostic {

	public final static List<String> DIAGNOSTICS = Arrays.asList(new String[] { "xbpmi_up", "xbpmi_down", "xbpmi_inb", "xbpmi_outb", "ic1", "ic2" });

	public static String getDefault() {
		return DIAGNOSTICS.get(0);
	}

}
