package edu.pku.sj.rscasd.utils.encoding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author Jason
 * @author Susan
 * 
 */
public class BASE64Util {
	public static String encodeFile(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}

		File remoteFile = new File(filePath.trim());
		if (!remoteFile.exists()) {
			return "";
		}

		try {
			InputStream ins = new FileInputStream(remoteFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// encode
			new BASE64Encoder().encode(ins, baos);
			return baos.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String encodePlainText(String text) {
		if (StringUtils.isEmpty(text)) {
			return "";
		}

		try {
			byte[] data = text.getBytes();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// encode
			new BASE64Encoder().encode(data, baos);
			return baos.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String decodePlainText(String text) {
		if (StringUtils.isEmpty(text)) {
			return "";
		}

		try {
			byte[] data = new BASE64Decoder().decodeBuffer(text);
			return new String(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {

		// Plain text
		String sourceText = "Just for testing BASE64";
		System.out.println("Source: " + sourceText);
		String encodedText = encodePlainText(sourceText);
		System.out.println("Encoded: " + encodedText);
		String decodedText = decodePlainText(encodedText);
		System.out.println("Decoded: " + decodedText);
		if(!sourceText.equals(decodedText)) {
			throw new IllegalArgumentException();
		}
		
		// File 
		encodedText = encodeFile("d:\\abc.owls");
		System.out.println("Encoded: " + encodedText);
		decodedText = decodePlainText(encodedText);
		System.out.println("Decoded: " + decodedText);
	}
}
