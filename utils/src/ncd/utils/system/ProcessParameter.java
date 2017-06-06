package ncd.utils.system;

public class ProcessParameter {

	public static void start() {
		Timer.start();
	}

	public static void end(String message) {
		long endTime = Timer.totalTime();
		long freeMB = Memory.end();
		System.out.println(message + " took " + endTime + "ms, free memory: " + freeMB);
	}

}
