package com.pku.wlj;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

//import EDU.pku.ly.owlsservice.implementation.OwlsServiceMainImpl;

@Path("service")
public class Service {
	/**
	 * 发布服务
	 * @param service_url
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void post(@FormParam("service_uri") String service_uri){
//		OwlsServiceMainImpl impl=new OwlsServiceMainImpl();
//		impl.OwlsServicePublishEntry(service_uri);
	}
}
