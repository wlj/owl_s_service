package cn.edu.pku.ss.matchmaker.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import cn.edu.pku.ss.matchmaker.thrift.Test;

public class SSDClient {
	private static final String ip = "127.0.0.1";
	private static final int port = 8989;
	private static final int clientTimeout = 30000;
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.client.SSDClient.class);
	
	public static void main(String args[]) {
//		if (args.length < 3) {
//			logger.error("parameter: " + "ip " + "port " + " string");
//			return ;
//		}
//		String ip = args[0];
//		int port = Integer.parseInt(args[1]);
//		String srt = args[2];
		
		String str = "dennis yang";
		try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			TNonblockingTransport transport = new TNonblockingSocket(ip, port, clientTimeout);
			TProtocolFactory protocol = new TCompactProtocol.Factory();
			Test.AsyncClient asyncClient = new Test.AsyncClient(protocol, clientManager, transport);
			
			logger.info("client calls ...");
			
			MyCallback callback = new MyCallback();
			asyncClient.printHello(str, callback);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
