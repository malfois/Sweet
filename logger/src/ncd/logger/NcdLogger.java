package ncd.logger;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class NcdLogger {

	public static TextAreaHandler getTextAreaHandler() {
		return TextAreaHandler.getInstance();
	}

	public static void Info(String className, String methodName, String message) {
		LogRecord record = new LogRecord(Level.INFO, message);
		record.setLoggerName(className);
		record.setSourceMethodName(methodName);
		record.setMillis(System.currentTimeMillis());
		TextAreaHandler.getInstance().info(record);
	}

	public static void Warning(String className, String methodName, String message) {
		LogRecord record = new LogRecord(Level.WARNING, message);
		record.setLoggerName(className);
		record.setSourceMethodName(methodName);
		record.setMillis(System.currentTimeMillis());
		TextAreaHandler.getInstance().warning(record);
	}

	public static void Error(String className, String methodName, String message) {
		LogRecord record = new LogRecord(Level.WARNING, message);
		record.setLoggerName(className);
		record.setSourceMethodName(methodName);
		record.setMillis(System.currentTimeMillis());
		TextAreaHandler.getInstance().error(record);
	}

}
