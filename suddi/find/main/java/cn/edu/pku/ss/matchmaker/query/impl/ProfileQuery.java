package cn.edu.pku.ss.matchmaker.query.impl;


import java.util.Hashtable;

import org.apache.log4j.Logger;

import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ComponentType;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import cn.edu.pku.ss.matchmaker.query.filters.QueryFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.ActorsFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.ClassificationsFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.InputsFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.OutputsFilter;
import cn.edu.pku.ss.matchmaker.query.filters.impl.ProductsFilter;

import EDU.cmu.Atlas.owls1_1.profile.Profile;


public class ProfileQuery {
	private Indexer indexer;
	private Scorer scorer;
	QueryFilter filter;
	Logger logger;
	
	public ProfileQuery(Indexer indexer, Scorer scorer) {
		// TODO Auto-generated constructor stub
		this.indexer = indexer;
		this.scorer = scorer;
		
		init();
		
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.impl.ProfileQuery.class);
	}
	
	private void init() {
		filter = new InputsFilter(indexer, ComponentType.PROFILE);
		
		QueryFilter outputFilter = new OutputsFilter(indexer, ComponentType.PROFILE);
		filter.addNextFilter(outputFilter);
		
		QueryFilter classificationFilter = new ClassificationsFilter(indexer);
		outputFilter.addNextFilter(classificationFilter);
//		
//		QueryFilter productsFilter = new ProductsFilter(indexer);
//		classificationFilter.addNextFilter(productsFilter);
		
		QueryFilter actorsFilter = new ActorsFilter(indexer);
		classificationFilter.addNextFilter(actorsFilter);
	}
	
	public boolean process(Profile profile, Hashtable<String, QueryResults> resultTable) {
		if (profile == null || resultTable == null) {
			logger.error("parameter is empty or parameter is illegal!");
			return false;
		}
		
		Query query = new Query(profile.getInputList(), profile.getOutputList(),
				profile.getPreconditionList(), profile.getResultList(), profile.getServiceCategory(), 
				profile.getContactInformation(), profile.getServiceProducts());
				
		filter.process(query, resultTable);
		
		if (scorer.computeProfileScore(resultTable) == false) {
			logger.error("failed to compute process score!");
			return false;
		}
		
		return true;
	}
	

}
