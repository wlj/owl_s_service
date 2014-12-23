package edu.pku.sj.rscasd.utils.properties;

import junit.framework.Assert;

import org.junit.Test;

import edu.pku.sj.rscasd.utils.properties.PropertiesUtil;

public class PropertiesUtilTest {

	private final static String VALUE_TESTING_STRING = "123456";
	private final static int VALUE_TESTING_INTEGER = 123456;
	private final static boolean VALUE_TESTING_BOOLEAN = false;

	@Test
	public void checkLoad() {
		String testString = PropertiesUtil.getProperty("default.fortesting");
		int testInt = PropertiesUtil.getInteger("default.fortesting");
		boolean testBoolean = PropertiesUtil.getBoolean("default.forboolean");

		Assert.assertEquals(VALUE_TESTING_BOOLEAN, testBoolean);
		Assert.assertEquals(VALUE_TESTING_INTEGER, testInt);
		Assert.assertEquals(VALUE_TESTING_STRING, testString);
	}
}
