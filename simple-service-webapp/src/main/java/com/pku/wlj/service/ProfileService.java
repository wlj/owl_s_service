package com.pku.wlj.service;

import javax.ws.rs.PathParam;

import EDU.cmu.Atlas.owls1_1.profile.ProfileList;

import com.pku.wlj.bean.profile.ProfileProfileBean;

public interface ProfileService {
	public ProfileList getProfile(int id);
	
}
