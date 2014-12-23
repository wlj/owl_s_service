package cn.edu.pku.ss.matchmaker.query;

import java.util.Vector;

import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;

public class Query {
	private InputList inputList;
	private OutputList outputList;
	private PreConditionList preConditionList;
	private ResultList resultList;
	
	private ServiceCategoriesList serviceCategoriesList; 

	private ActorsList actorsList;
	private Vector<Object> serviceProducts;
	
	private String context;
	
	
	public String getContext() {
		return context;
	}


	public void setContext(String context) {
		this.context = context;
	}


	public ResultList getResultList() {
		return resultList;
	}


	public void setResultList(ResultList resultList) {
		this.resultList = resultList;
	}


	
	
	public Query(InputList inputList, OutputList outputList,
			PreConditionList preConditionList, ResultList resultList,
			ServiceCategoriesList serviceCategoriesList, ActorsList actorsList,
			Vector<Object> serviceProducts) {
		super();
		this.inputList = inputList;
		this.outputList = outputList;
		this.preConditionList = preConditionList;
		this.resultList = resultList;
		this.serviceCategoriesList = serviceCategoriesList;
		this.actorsList = actorsList;
		this.serviceProducts = serviceProducts;
	}


	public InputList getInputList() {
		return inputList;
	}


	public void setInputList(InputList inputList) {
		this.inputList = inputList;
	}


	public OutputList getOutputList() {
		return outputList;
	}


	public void setOutputList(OutputList outputList) {
		this.outputList = outputList;
	}


	public PreConditionList getPreConditionList() {
		return preConditionList;
	}


	public void setPreConditionList(PreConditionList preConditionList) {
		this.preConditionList = preConditionList;
	}

	public ServiceCategoriesList getServiceCategoriesList() {
		return serviceCategoriesList;
	}


	public void setServiceCategoriesList(ServiceCategoriesList serviceCategoriesList) {
		this.serviceCategoriesList = serviceCategoriesList;
	}


	public ActorsList getActorsList() {
		return actorsList;
	}


	public void setActorsList(ActorsList actorsList) {
		this.actorsList = actorsList;
	}


	public Vector<Object> getServiceProducts() {
		return serviceProducts;
	}


	public void setServiceProducts(Vector<Object> serviceProducts) {
		this.serviceProducts = serviceProducts;
	}
}
