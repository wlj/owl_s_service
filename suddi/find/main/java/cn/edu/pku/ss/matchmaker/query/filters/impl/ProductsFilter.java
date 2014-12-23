package cn.edu.pku.ss.matchmaker.query.filters.impl;


import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.edu.pku.ss.matchmaker.index.Indexer;
import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;



public class ProductsFilter extends AbstractQueryFilter {

	Indexer indexer;
	
	Logger logger;
	
	public ProductsFilter(Indexer indexer) {
		super();
		this.indexer = indexer;
		
		// add log record
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.ProductsFilter.class);
	}
	
	
	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		Vector<Object> productVector = query.getServiceProducts();
		
		if (getAllServices(productVector, resultTable) == false) {
			logger.error("failed to search according to product!");
			
		}
		
		return sendToNextFilter(query, resultTable);
	}

	private boolean getAllServices(Vector<Object> productVector, Hashtable<String, QueryResults> resultTable) {
		if (productVector == null || resultTable == null) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		int productsNum = productVector.size();
		for (int i = 0; i < productVector.size(); ++i) {
			String product = (String) productVector.get(i);
			// TODO
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(product);
			if (getSimilarServicesSpecifiedConcept(product, similarConcepts, productsNum, resultTable) == false)
				logger.error("failed to get services according to product term: " + product);
		}
		
		return true;
	}
	
	
	private boolean getSimilarServicesSpecifiedConcept(String concept,
			Set<String> similarConcepts,
			int productsNum,
			Hashtable<String, QueryResults> resultTable) {
		if (similarConcepts == null || resultTable == null || productsNum <= 0) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		for (String conceptString : similarConcepts) {
			Vector<Object> servicesVector = indexer.getServiceProductServices(conceptString);
			
			if (servicesVector == null)
				continue;
			
			for (int i = 0; i < servicesVector.size(); ++i) {
				ServiceBody serviceBody = (ServiceBody)servicesVector.get(i);
				if (serviceBody == null)
					continue;
				
				double productScore = indexer.getSemanticMatchDegree(concept, conceptString) / productsNum;
				
				if (resultTable.containsKey(serviceBody.getServiceKey()) == false) {
					QueryResults queryResults = new QueryResults(serviceBody);
					queryResults.setProductScore(productScore);
					resultTable.put(serviceBody.getServiceKey(), queryResults);
					continue;
				}
				
				QueryResults queryResults = resultTable.get(serviceBody.getServiceKey());
				if (queryResults == null)
					continue;
				
				queryResults.setProductScore(queryResults.getProductScore() + productScore);
			}
		}
		
		return true;
	}
	
}
