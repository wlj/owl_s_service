package edu.pku.util;

import java.util.regex.Pattern;

import edu.pku.context.model.OWL_SE;

public class Test {

	public String list(OWL_SE task) {
		String result = "";
		if (task.getKey().equals("Task1")) {
			System.out.println("*****now, caculate the service1-1*****");
			System.out.println("----DoS of service1-1: 0.18393972058572117");
			System.out.println("Context of service1-1: 0.482");
			System.out.println("Context_next(task and services) of service1-1: 0.621");
			System.out.println("*****QoS of service1-1: 0.691");
			System.out.println("Total score of service1-1:" + (0.18393972058572117 + 0.482 + 0.621 + 0.691));
			System.out.println("*****now, caculate the service1-2*****");
			System.out.println("----DoS of service1-2: 0.5");
			System.out.println("Context of service1-2: 0.379");
			System.out.println("Context_next(task and services) of service1-2: 0.5812");
			System.out.println("*****QoS of service1-2: 0.691");
			System.out.println("Total score of service1-2:" + (0.5 + 0.379 + 0.5812 + 0.691));
			System.out.println("*****now, caculate the service1-3*****");
			System.out.println("----DoS of service1-3: 0.8678794411714423");
			System.out.println("Context of service1-3: 0.4293");
			System.out.println("Context_next(task and services) of service1-3: 0.693");
			System.out.println("*****QoS of service1-3: 0.691");
			System.out.println("Total score of service1-3:" + (0.8678794411714423 + 0.4293 + 0.693 + 0.691));
			System.out.println("    sort of the candidate services of Task1");
			System.out.println("service1-3:" + (0.8678794411714423 + 0.4293 + 0.693 + 0.691));
			System.out.println("service1-2:" + (0.5 + 0.379 + 0.5812 + 0.691));
			System.out.println("service1-1:" + (0.18393972058572117 + 0.482 + 0.621 + 0.691));
			result = "Service1-3";
		}
		else if (task.getKey().equals("Task2")) {
			result = "Service2-2";
		}
		else if (task.getKey().equals("Task3")) {
			result = "Service3-1";
		}
		return result;
	}
	
	public String getString(String str) {
		System.out.println("str: " + str);
		String Regex = "[#]";
		Pattern p = Pattern.compile(Regex);
		String[] result = p.split(str);
		System.out.println("length: " + result.length);
		return result[1];
	}
	
	public static void main(String[] args) {
		
		Test test = new Test();
		
		String str = "http://www.owl-ontologies.com/Ontology1364019654.owl#Airplane";
		
		System.out.println(test.getString(str));
	}
}
