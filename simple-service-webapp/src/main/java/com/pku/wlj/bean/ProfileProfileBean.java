package com.pku.wlj.bean;

import java.io.Serializable;

public class ProfileProfileBean implements Serializable {
	private int id;
	private String uri;
	private String serviceName;
	private String description;
	private int processId;
	private int serviceId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getProcessId() {
		return processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setService_id(int serviceId) {
		this.serviceId = serviceId;
	}
}
