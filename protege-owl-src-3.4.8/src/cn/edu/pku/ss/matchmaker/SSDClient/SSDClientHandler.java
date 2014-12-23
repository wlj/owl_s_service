package cn.edu.pku.ss.matchmaker.SSDClient;


import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import cn.edu.pku.ss.matchmaker.thrift.ResponseInfo;
import cn.edu.pku.ss.matchmaker.thrift.ServiceDiscoverer.AsyncClient.getServices_call;

public class SSDClientHandler implements AsyncMethodCallback<getServices_call> {
	private ResponseInfo responseInfo;
	
	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}
	
	@Override
	public void onError(Exception exception) {
		// TODO Auto-generated method stub
		exception.printStackTrace();
	}

	@Override
	public void onComplete(getServices_call response) {
		// TODO Auto-generated method stub
		try {
			responseInfo = response.getResult();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
