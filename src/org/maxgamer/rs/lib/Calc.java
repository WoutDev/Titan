package org.maxgamer.rs.lib;

/**
 * @author netherfoam
 */
public class Calc {
	public static boolean isBetween(int number, int min, int max) {
		return number >= min && number <= max;
	}
	
	public static double between(double min, double max, double val) {
		if (val > max) return max;
		if (val < min) return min;
		return val;
	}
	
	public static long between(long min, long max, long val) {
		if (val > max) return max;
		if (val < min) return min;
		return val;
	}
	
	public static int between(int min, int max, int val) {
		if (val > max) return max;
		if (val < min) return min;
		return val;
	}
	
	public static long min(long... longs) {
		long min = longs[0];
		for (int i = 1; i < longs.length; i++) {
			if (longs[i] < min) min = longs[i];
		}
		return min;
	}
	
	public static int min(int... ints) {
		int min = ints[0];
		for (int i = 1; i < ints.length; i++) {
			if (ints[i] < min) min = ints[i];
		}
		return min;
	}
	
	public static double min(double... doubles) {
		double min = doubles[0];
		for (int i = 1; i < doubles.length; i++) {
			if (doubles[i] < min) min = doubles[i];
		}
		return min;
	}
	
	public static int max(int... ints) {
		int max = ints[0];
		for (int i = 1; i < ints.length; i++) {
			if (ints[i] > max) max = ints[i];
		}
		return max;
	}
	
	public static long max(long... longs) {
		long max = longs[0];
		for (int i = 1; i < longs.length; i++) {
			if (longs[i] > max) max = longs[i];
		}
		return max;
	}
	
	public static double max(double... doubles) {
		double max = doubles[0];
		for (int i = 1; i < doubles.length; i++) {
			if (doubles[i] > max) max = doubles[i];
		}
		return max;
	}
}