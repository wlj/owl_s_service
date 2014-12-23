package cn.edu.pku.ss.matchmaker.query.filters.impl;

import java.util.Hashtable;

import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import cn.edu.pku.ss.matchmaker.query.filters.QueryFilter;

public abstract class AbstractQueryFilter implements QueryFilter {

private QueryFilter nextFilter;
	
	public AbstractQueryFilter() {
		nextFilter = null;
	}
	
	
	public void addNextFilter(QueryFilter queryFilter) {
		// TODO Auto-generated method stub

		this.nextFilter = queryFilter;
	}


	
	public Hashtable<String, QueryResults> sendToNextFilter(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		
		if (nextFilter != null)
			return nextFilter.process(query, resultTable);
		
		return resultTable;
	}

	public QueryFilter getNextFilter() {
		return nextFilter;
	}

	public void setNextFilter(QueryFilter nextFilter) {
		this.nextFilter = nextFilter;
	}

}
