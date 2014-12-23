package cn.edu.pku.ss.matchmaker.index.impl;

public class ServiceDataModel {
	private DataModel profileDataModel;
	private DataModel processDataModel;
	private DataModel groundingDataModel;
	
    public ServiceDataModel() {
    	profileDataModel = new DataModel();
    	processDataModel = new DataModel();
    	groundingDataModel = new DataModel();
    }
	
	public ServiceDataModel(DataModel profileDataModel,
			DataModel processDataModel, DataModel groundingDataModel) {
		
		this.profileDataModel = profileDataModel;
		this.processDataModel = processDataModel;
		this.groundingDataModel = groundingDataModel;
		
	}


	public DataModel getProfileDataModel() {
		return profileDataModel;
	}
	
	public void setProfileDataModel(DataModel profileDataModel) {
		this.profileDataModel = profileDataModel;
	}
	
	public DataModel getProcessDataModel() {
		return processDataModel;
	}
	
	public void setProcessDataModel(DataModel processDataModel) {
		this.processDataModel = processDataModel;
	}
	
	public DataModel getGroundingDataModel() {
		return groundingDataModel;
	}
	
	public void setGroundingDataModel(DataModel groundingDataModel) {
		this.groundingDataModel = groundingDataModel;
	}
}
