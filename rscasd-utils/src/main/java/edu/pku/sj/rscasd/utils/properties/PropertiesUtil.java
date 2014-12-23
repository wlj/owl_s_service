package edu.pku.sj.rscasd.utils.properties;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PropertiesUtil {

	private static final Properties props = new Properties(System.getProperties());
	
	private static final Log logger = LogFactory.getLog(PropertiesUtil.class);

	static {

		String mainPropertyFilePath = System.getProperty("edu.pku.sj.rscasd.main_property_file");
		boolean toLoadDefault = false;
		String defaultPropertyFilePath = "/rscasd.properties";

		if (mainPropertyFilePath != null && !mainPropertyFilePath.trim().equals("")) {
			try {
				props.load(PropertiesUtil.class.getResourceAsStream(mainPropertyFilePath));
			} catch (IOException e) {
				toLoadDefault = true;
			}
		} else {
			toLoadDefault = true;
		}

		if (toLoadDefault) {
			try {
				props.load(PropertiesUtil.class.getResourceAsStream(defaultPropertyFilePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static Properties load(URL url) {
		Properties tempProps = new Properties();

		try {
			tempProps.load(url.openStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return tempProps;
	}

	public synchronized static String getProperty(String name) {
		return StringUtils.isEmpty(name) ? "" : StringUtils.defaultString(props.getProperty(name));
	}

	public synchronized static boolean getBoolean(String name) {
		return BooleanUtils.toBoolean(getProperty(name));
	}

	public synchronized static int getInteger(String name) {
		return NumberUtils.toInt(getProperty(name));
	}

	public synchronized static float getFloat(String name) {
		return NumberUtils.toFloat(getProperty(name));
	}

	public synchronized static long getLong(String name) {
		return NumberUtils.toLong(getProperty(name));
	}
}
