package ncd.utils.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metadata {

	private Map<String, String>	headers;
	private List<Metadata>		nodes;

	public Metadata(Map<String, String> headers) {
		this.headers = headers;
		this.nodes = new ArrayList<>();
	}

	public Metadata(Map<String, String> headers, List<Metadata> nodes) {
		this.headers = headers;
		this.nodes = nodes;
	}

	public List<Metadata> getChildren() {
		return this.nodes;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public int getNumberOfCurves(int nCurve) {
		if (!nodes.isEmpty()) {
			for (Metadata metadata : nodes) {
				nCurve = metadata.getNumberOfCurves(nCurve);
			}
		} else {
			nCurve++;
		}
		return nCurve;
	}

	public Metadata getLeaf() {
		if (!nodes.isEmpty()) {
			for (Metadata metadata : nodes) {
				Metadata mdata = metadata.getLeaf();
				if (mdata != null) {
					return mdata;
				}
			}
		} else {
			return this;
		}
		return null;
	}

	public Metadata clone() {
		Metadata metadata = this.cloneHeaders();
		if (!nodes.isEmpty()) {
			for (Metadata mdata : nodes) {
				metadata.getChildren().add(mdata.clone());
			}
		}
		return metadata;
	}

	private Metadata cloneHeaders() {
		Map<String, String> map = new HashMap<>();
		map.putAll(this.headers);
		return new Metadata(map);
	}

	public Metadata getMetadataWithEntry(String key, String value) {
		if (this.headers.containsKey(key) && this.headers.containsValue(value)) {
			return this;
		}
		if (!this.nodes.isEmpty()) {
			for (Metadata metadata : this.nodes) {
				Metadata m = metadata.getMetadataWithEntry(key, value);
				if (m != null) {
					return m;
				}
			}
		}
		return null;
	}

	public String get(String key) {
		String value = this.headers.get(key);
		if (value != null) {
			return value;
		}
		if (!this.nodes.isEmpty()) {
			for (Metadata metadata : this.nodes) {
				value = metadata.get(key);
				if (value != null) {
					return value;
				}
			}
		}
		return value;
	}

	public Boolean hasKey(String key) {
		String value = this.headers.get(key);
		if (value != null) {
			return true;
		}
		if (!this.nodes.isEmpty()) {
			for (Metadata metadata : this.nodes) {
				value = metadata.get(key);
				if (value != null) {
					return true;
				}
			}
		}
		return false;
	}

	public Boolean hasValue(String value) {
		Boolean hasValue = this.headers.containsValue(value);
		if (hasValue) {
			return true;
		}
		if (!this.nodes.isEmpty()) {
			for (Metadata metadata : this.nodes) {
				hasValue = metadata.hasValue(value);
				if (hasValue) {
					return true;
				}
			}
		}
		return false;
	}

	public String toText() {
		String text = "";
		for (Map.Entry<String, String> entry : this.headers.entrySet()) {
			text += "# " + entry.getKey() + ": " + entry.getValue() + "\n";
		}
		if (this.nodes.size() > 0) {
			for (Metadata node : this.nodes) {
				text += node.toText();
			}
		}
		return text;
	}

	@Override
	public String toString() {
		return this.toString("");
	}

	public String toString(String indent) {
		String text = "";
		for (Map.Entry<String, String> entry : this.headers.entrySet()) {
			text += indent + entry.getKey() + ": " + entry.getValue() + "\n";
		}
		if (this.nodes.size() > 0) {
			for (Metadata node : this.nodes) {
				text += node.toString(indent + "   ");
			}
		}
		return text;
	}

}
