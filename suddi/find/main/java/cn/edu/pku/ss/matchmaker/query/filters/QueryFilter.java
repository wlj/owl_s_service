package cn.edu.pku.ss.matchmaker.query.filters;

import java.util.Hashtable;

import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;

public interface QueryFilter {
	public abstract Hashtable<String, QueryResults> process(Query query, Hashtable<String, QueryResults> resultTable);

	public abstract void addNextFilter(QueryFilter queryFilter);

	public abstract Hashtable<String, QueryResults> sendToNextFilter(Query query, Hashtable<String, QueryResults> resultTable);
}
