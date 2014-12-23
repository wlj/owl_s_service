package edu.pku.ly.XmlOpe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5FileUtil {
	
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	
	static 
	{
		try 
		{
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String GetFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return BufferToHex(messagedigest.digest());
	}

	public static String GetMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return BufferToHex(messagedigest.digest());
	}

	private static String BufferToHex(byte bytes[]) {
		return BufferToHex(bytes, 0, bytes.length);
	}

	private static String BufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			AppendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void AppendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean CheckPassword(String password, String md5PwdStr) {
		String s = GetMD5String(password);
		return s.equals(md5PwdStr);
	}

	/*public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();

		File big = new File("C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\Application.owl");
		String md5 = getFileMD5String(big);

		long end = System.currentTimeMillis();
		System.out.println("md5:" + md5);
		System.out.println("time:" + ((end - begin) / 1000) + "s");
	}*/
}
