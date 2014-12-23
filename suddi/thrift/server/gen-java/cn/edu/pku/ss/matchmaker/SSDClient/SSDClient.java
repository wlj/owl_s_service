package cn.edu.pku.ss.matchmaker.SSDClient;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import cn.edu.pku.ss.matchmaker.thrift.RequestInfo;
import cn.edu.pku.ss.matchmaker.thrift.ResponseInfo;
import cn.edu.pku.ss.matchmaker.thrift.ServiceDiscoverer;

public class SSDClient {

	/**
	 * @param args
	 */
	private static final String ip = "127.0.0.1";
	private static final int port = 8989;
	private static final int clientTimeout = 30000;
	private static Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.SSDClient.SSDClient.class);
	
    ServiceDiscoverer.AsyncClient initAsyncClient() {
    	ServiceDiscoverer.AsyncClient asyncClient = null;
    	try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			TNonblockingTransport transport = new TNonblockingSocket(ip, port, clientTimeout);
		
			
			TProtocolFactory protocol = new TCompactProtocol.Factory();
			asyncClient = new ServiceDiscoverer.AsyncClient(protocol, clientManager, transport);
			
			
    	} catch (IOException e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	
    	return asyncClient;
    }
    
	public static void main(String[] args) throws TException {
		// TODO Auto-generated method stub
		SSDClient ssdClient = new SSDClient();
		ServiceDiscoverer.AsyncClient asyncClient = null;
		if ((asyncClient = ssdClient.initAsyncClient()) == null) {
			logger.error("failed to init client");
			return ;
		}
		
		RequestInfo requestinfo = new RequestInfo();
	
	}
	
	
	
//	ServiceDiscoverer.Client initSyncClient() {
//    	transport = new TFramedTransport(new TSocket(ip, port, clientTimeout));
//		TProtocol protocol = new TCompactProtocol(transport);
//		ServiceDiscoverer.Client client = new ServiceDiscoverer.Client(protocol);
//		
//    	return client;
//    }
//    
//    public void close() {
//    	transport.close();
//    }
//   
//    
//    public void open() {
//    	try {
//			transport.open();
//		} catch (TTransportException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

}
