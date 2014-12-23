package cn.edu.pku.ss.matchmaker.query.filters;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;


import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;

import cn.edu.pku.ss.matchmaker.query.Query;
import cn.edu.pku.ss.matchmaker.query.QueryResults;
import edu.pku.func.CXT_RULE;

public class PreconditionsFilter extends AbstractQueryFilter {
	private CXT_RULE cxt_rule;
	private Logger logger;
	
	public PreconditionsFilter(CXT_RULE cxt_rule) {
		// TODO Auto-generated constructor stub
		this.cxt_rule = cxt_rule;
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.filters.impl.PreconditionsFilter.class);
	}

	public Hashtable<String, QueryResults> process(Query query,
			Hashtable<String, QueryResults> resultTable) {
		// TODO Auto-generated method stub
		PreConditionList preConditionList = query.getPreConditionList();
		
		return null;
	}
	
	public boolean profilePreconditionFilter(PreConditionList preConditionList,
			Hashtable<String, QueryResults> resultTable) {
		if (preConditionList == null || preConditionList.size() == 0 || resultTable == null)
			return false;
		
		String expressionBody = getPreconditionString(preConditionList);
		if (expressionBody == null)
			return false;
		
		Enumeration enumeration = resultTable.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			if (key == null)
				continue;
			
			QueryResults queryResults = resultTable.get(key);
			if (queryResults == null)
				continue;
			
			Profile profile = queryResults.getProfile();
			if (profile == null)
				continue;
			
			String precondition = getPreconditionString(profile.getPreconditionList());
			if (precondition == null)
				continue;
			
			double matchDegree = cxt_rule.exec(expressionBody, precondition);
			queryResults.setPreconditionScore(matchDegree);
		}
		
		
		return true;
	}
	
	private String getPreconditionString(PreConditionList preConditionList) {
		if (preConditionList == null || preConditionList.size() == 0)
			return null;
		
		Condition condition = preConditionList.getNthPreCondition(0);
		if (condition == null)
			return null;
		
		String expressionBody = condition.getExpressionBody();
		if (expressionBody != null)
			return expressionBody;
		
		return null;
	}
}
