package ncd.utils.system;

public class Timer {

	private static long startTime = 0;

	public static void start() {
		startTime = System.currentTimeMillis();
	}

	public static long totalTime() {
		return System.currentTimeMillis() - startTime;
	}
}
