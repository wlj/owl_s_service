package cn.edu.pku.ss.matchmaker.query.filters;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;

import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;


public class ClassificationsFilter extends AbstractQueryFilter {

	private Indexer indexer;
	
	private Logger logger;
	
	public ClassificationsFilter(Indexer indexer) {
		super();
		this.indexer = indexer;
		
		// add log record
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.ClassificationsFilter.class);
	}
	
	
	
	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		ServiceCategoriesList categoriesList = query.getServiceCategoriesList();
		
		if (getAllServices(categoriesList, resultTable) == false) {
			logger.error("failed to search according to classification!");
		}
		
		return sendToNextFilter(query, resultTable);
	}

	private boolean getAllServices(ServiceCategoriesList categoriesList, Hashtable<String, QueryResults> resultTable) {
		if (categoriesList == null || resultTable == null) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		int classificationNum = categoriesList.size();
		for (int i = 0; i < categoriesList.size(); ++i) {
			ServiceCategory serviceCategory = categoriesList.getServiceCategoryAt(i);
			if (serviceCategory == null)
				continue;
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(serviceCategory.getValue());
			if (similarConcepts.isEmpty() == false) {
				if (getSimilarServicesSpecifiedConcept(serviceCategory.getValue(), similarConcepts, classificationNum, resultTable) == false)
					logger.error("failed to get services according to classification term: " + serviceCategory.getValue());
			} else {
				if (getSimilarServicesSpecifiedConcept(serviceCategory.getValue(), Collections.singleton(serviceCategory.getValue()), classificationNum, resultTable) == false)
					logger.error("failed to get services according to classification term: " + serviceCategory.getValue());
			}
				
		}
		
		return true;
	}
	
	
	private boolean getSimilarServicesSpecifiedConcept(String concept,
			Set<String> similarConcepts,
			int classificationNum,
			Hashtable<String, QueryResults> resultTable) {
		if (similarConcepts == null || resultTable == null || classificationNum <= 0) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		for (String conceptString : similarConcepts) {
			Vector<Object> servicesVector = indexer.getClassificationServices(conceptString);
			
			if (servicesVector == null)
				continue;
			
			for (int i = 0; i < servicesVector.size(); ++i) {
				ServiceBody serviceBody = (ServiceBody)servicesVector.get(i);
				if (serviceBody == null)
					continue;
				
				if (serviceBody.getProfile() == null || serviceBody.getProfile().getServiceCategory() == null)
					continue;
				
				int candClassificationNum = serviceBody.getProfile().getServiceCategory().size();
				int maxClassificationNum =  candClassificationNum > classificationNum ? candClassificationNum : classificationNum;
				double classificationScore = indexer.getSemanticMatchDegree(concept, conceptString) / maxClassificationNum;
				
				if (resultTable.containsKey(serviceBody.getServiceKey()) == false) {
					QueryResults queryResults = new QueryResults(serviceBody);
					queryResults.setClassificationScore(classificationScore);
					resultTable.put(serviceBody.getServiceKey(), queryResults);
					continue;
				}
				
				QueryResults queryResults = resultTable.get(serviceBody.getServiceKey());
				if (queryResults == null)
					continue;
				
				queryResults.setClassificationScore(queryResults.getClassificationScore() + classificationScore);
			}
		}
		
		return true;
	}
}
