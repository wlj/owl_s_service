package cn.edu.pku.ss.matchmaker.client;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import cn.edu.pku.ss.matchmaker.thrift.Test.AsyncClient.printHello_call;

public class MyCallback implements AsyncMethodCallback<printHello_call> {

	@Override
	public void onComplete(printHello_call response) {
		// TODO Auto-generated method stub
		System.out.println("onComplete");
		try {
			response.getResult();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onError(Exception exception) {
		// TODO Auto-generated method stub
		System.out.println("onError");
		
	}

}
