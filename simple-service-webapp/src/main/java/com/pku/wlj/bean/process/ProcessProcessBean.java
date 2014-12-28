package com.pku.wlj.bean.process;

public class ProcessProcessBean {
	private int id;
	private String name;
	private String uri;
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private int ccType;
	private int serviceId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getCcType() {
		return ccType;
	}
	public void setCcType(int ccType) {
		this.ccType = ccType;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	
}
