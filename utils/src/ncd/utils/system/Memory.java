package ncd.utils.system;

public class Memory {

	public static void usage(String className) {
		Runtime rt = Runtime.getRuntime();
		long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
		long freeMB = (rt.freeMemory()) / 1024 / 1024;
		long maxMB = (rt.maxMemory()) / 1024 / 1024;
		long totalMB = (rt.totalMemory()) / 1024 / 1024;
		System.out.println(className + " memory usage: " + usedMB + "MB, Free memory = " + freeMB
				+ "MB, Total memory = " + totalMB + ", Max memory = " + maxMB);
	}

	public static long end() {
		Runtime rt = Runtime.getRuntime();
		return (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
	}

}
