package com.pku.wlj;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.pku.wlj.bean.ProfileProfileBean;
import com.pku.wlj.dao.ProfileDao;

@Path("profile")
public class Profile {
	@Autowired
	private ProfileDao profileDao;
	
	public void setProfileDao(ProfileDao profileDao) {
		this.profileDao = profileDao;
	}

	@GET
	@Path("/profileprofile/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public ProfileProfileBean getProfileProfile(@PathParam("id") int id) {
		ProfileProfileBean bean=profileDao.getProfileProfileById(id);
		if(bean==null){
			throw new NotFoundException();
		}
		return bean;
	}
}
