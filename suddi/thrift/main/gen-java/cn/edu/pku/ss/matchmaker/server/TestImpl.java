package cn.edu.pku.ss.matchmaker.server;

import org.apache.thrift.TException;

import cn.edu.pku.ss.matchmaker.thrift.Test.Iface;

public class TestImpl implements Iface {

	@Override
	public void printHello(String str) throws TException {
		// TODO Auto-generated method stub
		
		System.out.println("Hello, " + str);
	}

}
