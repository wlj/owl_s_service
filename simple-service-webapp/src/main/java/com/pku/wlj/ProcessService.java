package com.pku.wlj;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import EDU.pku.ly.Process.ProcessMain;
import EDU.pku.ly.Process.Implementation.ProcessMainImpl;

@Path("/process")
public class ProcessService {
	
	private static ProcessMain processMain=new ProcessMainImpl();
	
	@GET
	@Path("/{service_id}")
	@Produces({MediaType.APPLICATION_JSON})
	public String get(int service_id) {
		EDU.cmu.Atlas.owls1_1.process.Process process=processMain.ProcessInquiryEntry(service_id);
		Gson gson = new Gson();
		String str = gson.toJson(process);
		return str;
	}
	
	
	
}
