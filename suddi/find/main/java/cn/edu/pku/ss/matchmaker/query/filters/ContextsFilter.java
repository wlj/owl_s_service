package cn.edu.pku.ss.matchmaker.query.filters;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.sun.xml.internal.bind.CycleRecoverable.Context;

import cn.edu.pku.ss.matchmaker.index.impl.ServiceBody;
import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import edu.pku.func.CXT_RULE;

public class ContextsFilter extends AbstractQueryFilter {
	private CXT_RULE cxt_rule;
	private Logger logger;
	
	public ContextsFilter(CXT_RULE cxt_rule) {
		// TODO Auto-generated constructor stub
		this.cxt_rule = cxt_rule;
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.ContextsFilter.class);
	}

	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		String context = query.getContext();
		if (context == null || context.isEmpty())
			return sendToNextFilter(query, resultTable);
		
		Enumeration enumeration = resultTable.keys();
		while(enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			QueryResults queryResults = resultTable.get(key);
			if (queryResults == null)
				continue;
			
			String rule = queryResults.getContextRule();
			if (rule == null || rule.isEmpty())
				continue;
			
			double matchDegree = cxt_rule.exec(context, rule);
			queryResults.setContextScore(matchDegree);
		}
		
		return sendToNextFilter(query, resultTable);
	}

}
