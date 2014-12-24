package com.pku.wlj;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.pku.wlj.bean.ProfileProfileBean;
import com.pku.wlj.dao.ProfileDao;

public class Profile {
	@Autowired
	private ProfileDao profileDao;
	
	@GET
	@Path("/ProfileProfile/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public ProfileProfileBean getProfileProfile(@QueryParam("id") int id) {
		ProfileProfileBean bean=profileDao.getProfileProfileById(id);
		if(bean==null){
			throw new NotFoundException();
		}
		return bean;
	}
}
