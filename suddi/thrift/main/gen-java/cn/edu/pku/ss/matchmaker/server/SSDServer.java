package cn.edu.pku.ss.matchmaker.server;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import cn.edu.pku.ss.matchmaker.thrift.Test;

public class SSDServer {
	private static final int port = 8989;
	private Logger logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.server.SSDServer.class);
	public void startServer() {
		try {
			TNonblockingServerSocket socket = new TNonblockingServerSocket(port);
			Test.Processor processor = new Test.Processor(new TestImpl());
			
			TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
			//THsHaServer.Args args = new THsHaServer.Args(socket);
			args.processorFactory(new TProcessorFactory(processor));
			args.transportFactory(new TFramedTransport.Factory());
			args.protocolFactory(new TCompactProtocol.Factory());
			
			TServer server = new TNonblockingServer(args);
			logger.info("----------server is started-------------");
			server.serve();
			
			
		} catch (TTransportException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SSDServer server = new SSDServer();
		server.startServer();
	}
}
