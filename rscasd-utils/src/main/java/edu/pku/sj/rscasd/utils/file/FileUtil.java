package edu.pku.sj.rscasd.utils.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;
import edu.pku.sj.rscasd.utils.random.RandomUtil;

public class FileUtil {

	private final static Log logger = LogFactory.getLog(FileUtil.class);

	public static String endUpPath(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "/";
		}

		filePath = filePath.trim();

		if (!filePath.endsWith(File.separator)) {
			filePath += File.separator;
		}

		return filePath;
	}
	
	public static String replaceSeparator(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return filePath;
		}
		
		return filePath.replaceAll("\\\\", "/");
	}

	public static String complementUriProtocol(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "file:/";
		}

		filePath = filePath.trim();
		String lowcaseFilePath = filePath.toLowerCase();
		if (lowcaseFilePath.startsWith("http:") || lowcaseFilePath.startsWith("file:")) {
			return filePath;
		}

		return "file:" + filePath;
	}

	public static String getRemoteFilePath(HttpServletRequest request, String remotePathKey) {
		if (request == null || StringUtils.isEmpty(remotePathKey)) {
			return null;
		}

		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		remotePathKey = remotePathKey.replaceAll("\\<pre\\>", "");
		remotePathKey = remotePathKey.replaceAll("\\</pre\\>", "");
		return (String) session.getAttribute(remotePathKey);
	}

	public static String getBase64EncodedRemoteFile(HttpServletRequest request, String remotePathKey) {

		String remotePathValue = FileUtil.getRemoteFilePath(request, remotePathKey);

		if (StringUtils.isEmpty(remotePathValue)) {
			return "";
		}

		File remoteFile = new File(remotePathValue.trim());
		if (!remoteFile.exists()) {
			return "";
		}

		try {
			InputStream ins = new FileInputStream(remoteFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// encode
			new BASE64Encoder().encode(ins, baos);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return null;
	}

	public static String save(String directory, String filePrefix, String fileSuffix, String content) {
		if (StringUtils.isEmpty(directory)) {
			String msg = "No target repository directory given. ";
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		filePrefix = StringUtils.trimToEmpty(filePrefix);
		fileSuffix = StringUtils.trimToEmpty(fileSuffix);
		String fileNameStuff = String.valueOf(System.currentTimeMillis()) + "_" + RandomUtil.getRandomInt();
		directory = endUpPath(directory);
		String fullFileName = directory + (StringUtils.isEmpty(filePrefix) ? "" : filePrefix + "_") + fileNameStuff
				+ (StringUtils.isEmpty(fileSuffix) ? "" : "." + fileSuffix);

		File targetFile = new File(fullFileName);
		File containerDir = targetFile.getParentFile();
		if (!containerDir.exists()) {
			logger.info("Try to create not-existing directory: " + containerDir.getAbsolutePath());
			containerDir.mkdirs();
		}

		try {
			FileUtils.writeStringToFile(targetFile, (content == null ? "" : content),"");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}

		return targetFile.getAbsolutePath();
	}

	public static String save(ClassLoader loader, String relativePath, String filePrefix, String fileSuffix,
			String content) {
		if (loader == null) {
			String msg = "Invalid class loader given. ";
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}

		if (StringUtils.isEmpty(relativePath)) {
			String msg = "No target repository path given. ";
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}

		try {
			URL targetPathUrl = loader.getResource(relativePath);
			if (targetPathUrl == null) {
				String msg = "No target repository path given. ";
				logger.error(msg);
				throw new IllegalArgumentException(msg);
			}
			return save(targetPathUrl.getPath(), filePrefix, fileSuffix, content);
		} catch (Exception e) {
			String msg = "Can not get target directory from given relative path: " + relativePath;
			logger.error(msg + ". With message: " + e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}
	}

	public static String relocatePath(String directory, String path) {
		if (StringUtils.isEmpty(directory)) {
			return null;
		}

		if (StringUtils.isEmpty(path)) {
			return null;
		}

		int lastFileSepInd = path.indexOf(File.separator);
		String fileName = path;
		if (lastFileSepInd >= 0) {
			fileName = path.substring(lastFileSepInd + 1);
		}

		return (endUpPath(directory) + fileName);
	}
	
	public static String GetNamespaceByURL(String url)
	{
		if(!url.contains("#"))
		{
			return "";
		}
		
		return url.substring(0, url.lastIndexOf("#"));
	}
	
	public static String GetNameByURL(String url)
	{
		if(!url.contains("#"))
		{
			return "";
		}
		
		return url.substring(url.lastIndexOf("#"));
	}
}
