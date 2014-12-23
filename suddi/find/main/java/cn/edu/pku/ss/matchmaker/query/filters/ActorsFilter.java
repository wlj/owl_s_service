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

import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;


public class ActorsFilter extends AbstractQueryFilter {

	private Indexer indexer;
	
	private Logger logger;
	
	public ActorsFilter(Indexer indexer) {
		super();
		this.indexer = indexer;
		
		// add log record
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.ActorsFilter.class);
	}
	
	
	
	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		ActorsList actorsList = query.getActorsList();
		
		if (getAllServices(actorsList, resultTable) == false) {
			logger.error("failed to search according to actors!");
		}
		
		return sendToNextFilter(query, resultTable);
	}

	private boolean getAllServices(ActorsList actorsList, Hashtable<String, QueryResults> resultTable) {
		if (actorsList == null || resultTable == null) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		int actorsNum = actorsList.size();
		for (int i = 0; i < actorsList.size(); ++i) {
			Actor actor = actorsList.getNthActor(i);
			if (actor == null)
				continue;
			// TODO
			Set<String> similarConcepts = indexer.getAllSimilarConcepts(actor.getName());
			if (similarConcepts.isEmpty() == false) {
				if (getSimilarServicesSpecifiedConcept(actor.getName(), similarConcepts, actorsNum, resultTable) == false)
					logger.error("failed to get services according to actor term: " + actor.getName());
			} else {
				if (getSimilarServicesSpecifiedConcept(actor.getName(), Collections.singleton(actor.getName()), actorsNum, resultTable) == false)
					logger.error("failed to get services according to actor term: " + actor.getName());
			}
		}
		
		return true;
	}
	
	
	private boolean getSimilarServicesSpecifiedConcept(String concept,
			Set<String> similarConcepts,
			int actorsNum,
			Hashtable<String, QueryResults> resultTable) {
		if (similarConcepts == null || resultTable == null || actorsNum <= 0) {
			logger.error("parameter is null or parameter is illegal!");
			return false;
		}
		
		for (String conceptString : similarConcepts) {
			Vector<Object> servicesVector = indexer.getActorServices(conceptString);
			
			if (servicesVector == null)
				continue;
			
			for (int i = 0; i < servicesVector.size(); ++i) {
				ServiceBody serviceBody = (ServiceBody)servicesVector.get(i);
				if (serviceBody == null)
					continue;
				

				if (serviceBody.getProfile() == null || serviceBody.getProfile().getContactInformation() == null)
					continue;
				int candActorsNum = serviceBody.getProfile().getContactInformation().size();
				int maxActorsNum = candActorsNum > actorsNum ? candActorsNum : actorsNum;
				
				double actorsScore = indexer.getSemanticMatchDegree(concept, conceptString) / maxActorsNum;
				
				if (resultTable.containsKey(serviceBody.getServiceKey()) == false) {
					QueryResults queryResults = new QueryResults(serviceBody);
					queryResults.setActorScore(actorsScore);
					resultTable.put(serviceBody.getServiceKey(), queryResults);
					continue;
				}
				
				QueryResults queryResults = resultTable.get(serviceBody.getServiceKey());
				if (queryResults == null)
					continue;
				
				queryResults.setActorScore(queryResults.getActorScore() + actorsScore);
			}
		}
		
		return true;
	}

}