package com.pku.wlj;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.pku.wlj.service.ProcessService;
import com.pku.wlj.service.impl.ProcessServiceImpl;

import EDU.pku.ly.Process.ProcessMain;
import EDU.pku.ly.Process.Implementation.ProcessMainImpl;
//@Component
@Path("/process")
public class Process {
	
	@Autowired
	private  ProcessService processService;
	
	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}

	@GET
	@Path("/{process_id}")
	@Produces({MediaType.APPLICATION_JSON})
	public String get(@PathParam("process_id") int process_id) {
		EDU.cmu.Atlas.owls1_1.process.Process process=processService.ProcessInquiryEntry(process_id,"");
		Gson gson = new Gson();
		String str = gson.toJson(process);
		return str;
	}
	
	
	
}
