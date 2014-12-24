package com.pku.wlj.dao;



import java.util.List;

import com.pku.wlj.bean.ProfileProfileBean;

public interface ProfileDao {
	/**
	 * 根据ID获取profile_profile
	 * @param id profile_profile_id
	 * @return ProfileProfileBean
	 */
	public ProfileProfileBean getProfileProfileById(int id);
	
	/**
	 * 根据服务ID获取profile_profileList
	 * @param serviceId
	 * @return
	 */
	public List<ProfileProfileBean> getProfileProfileListByService(int serviceId);
	
	
}
