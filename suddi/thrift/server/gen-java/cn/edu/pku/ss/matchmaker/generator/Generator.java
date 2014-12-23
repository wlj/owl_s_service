package cn.edu.pku.ss.matchmaker.generator;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Generator {

	/**
	 * @param args
	 */
	private static final String baseDir = "H:\\juddi\\suddi\\suddi\\thrift\\server\\";
	private static final String thriftFile = baseDir + "ServiceDiscovery.thrift";
	private static final String thriftCmd = baseDir + "thrift.exe";
	private static final String outputdir = baseDir;
	private static final String cmd = thriftCmd + " --gen java " + " -o " + outputdir + " " + thriftFile;
	
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.generator.Generator.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Runtime run = Runtime.getRuntime();
		
		logger.info("path: " + cmd);
		try {
			Process process = run.exec(cmd);
			if (process.waitFor() != 0) {
				if (process.exitValue() == 1)
					logger.error("wrongly exit");
				else if (process.exitValue() == 0)
					logger.error("correctly exit");	
			}
			logger.info("------------finished-------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
