package cn.edu.pku.ss.matchmaker.query.impl;

import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Set;


import org.apache.log4j.Logger;

import cn.edu.pku.ss.matchmaker.query.QueryResults;


public class Ranker {
	Logger logger;
	public Ranker() {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(cn.edu.pku.ss.matchmaker.query.impl.Ranker.class);
	}
	
	public Entry[] rank(Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("Parameter is null!");
			return null;
		}
		
		Set<Entry<String, QueryResults>> set = resultTable.entrySet();
		Entry[] entries = set.toArray(new Entry[set.size()]);
		
		Comparator comparator = new Comparator() {
			public int compare(Object object1, Object object2) {
				// TODO Auto-generated method stub
				Entry<String, QueryResults> entry1 = (Entry) object1;
				Entry<String, QueryResults> entry2 = (Entry) object2;
				
				double key1 = ((QueryResults)entry1.getValue()).getTotalScore();
				double key2 = ((QueryResults)entry2.getValue()).getTotalScore();
				
				return ((Comparable) key2).compareTo(key1);
			}
		};
		
		Arrays.sort(entries, comparator);
		logger.info("rank finished!");
		
		return entries;
		
	}
	
	
	
	public Entry[] rankProfile(Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("Parameter is null!");
			return null;
		}
		
		Set<Entry<String, QueryResults>> set = resultTable.entrySet();
		Entry<String, QueryResults>[] entries = set.toArray(new Entry[set.size()]);
		
		Comparator comparator = new Comparator() {
			public int compare(Object object1, Object object2) {
				// TODO Auto-generated method stub
				Entry<String, QueryResults> entry1 = (Entry) object1;
				Entry<String, QueryResults> entry2 = (Entry) object2;
				
				double key1 = ((QueryResults)entry1.getValue()).getProfileScore();
				double key2 = ((QueryResults)entry2.getValue()).getProfileScore();
				
				return ((Comparable) key2).compareTo(key1);
			}
		};
		
		Arrays.sort(entries, comparator);
		logger.info("profile rank finished!");
		
		return entries;
		
	}
	
	
	public Entry[] rankProcess(Hashtable<String, QueryResults> resultTable) {
		if (resultTable == null) {
			logger.error("Parameter is null!");
			return null;
		}
		
		Set<Entry<String, QueryResults>> set = resultTable.entrySet();
		Entry<String, QueryResults>[] entries = set.toArray(new Entry[set.size()]);
		
		Comparator comparator = new Comparator() {
			public int compare(Object object1, Object object2) {
				// TODO Auto-generated method stub
				Entry<String, QueryResults> entry1 = (Entry) object1;
				Entry<String, QueryResults> entry2 = (Entry) object2;
				
				double key1 = ((QueryResults)entry1.getValue()).getProcessScore();
				double key2 = ((QueryResults)entry2.getValue()).getProcessScore();
				
				return ((Comparable) key2).compareTo(key1);
			}
		};
		
		Arrays.sort(entries, comparator);
		logger.info("process rank finished!");
		
		return entries;
		
	}
}

