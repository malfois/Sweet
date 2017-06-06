package ncd.scan.utils;

public class Message {

	public enum Type {
		NAME, SCAN_FILE, RESULTS, UNKNOWN, STOP, DIAGNOSTIC
	}

	public static boolean isSubstring(String message) {
		for (Type type : Type.values()) {
			if (message.contains(type.name())) { return true; }
		}
		return false;
	}

	public static Type getType(String message) {
		for (Type type : Type.values()) {
			if (message.contains(type.name())) { return type; }
		}
		return Type.UNKNOWN;
	}

	public static String get(String message) {
		int index = message.lastIndexOf(":");
		return message.substring(index + 1).trim();
	}

}