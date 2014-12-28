package com.pku.wlj.dao;

import java.util.List;

import com.pku.wlj.bean.process.ProcessProcessBean;

public interface ProcessDao {
	public ProcessProcessBean getProcessBean(int process_id);
	
	public List<ProcessProcessBean> getProcessBeanList(int service_id);
}
