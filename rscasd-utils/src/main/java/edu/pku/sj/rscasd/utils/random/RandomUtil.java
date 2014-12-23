package edu.pku.sj.rscasd.utils.random;

import java.util.Random;

public class RandomUtil {

	private final static Random random = new Random(System.currentTimeMillis());

	public static long getRandomLong() {
		return random.nextLong();
	}

	public static long getRandomInt() {
		return random.nextLong();
	}
}
