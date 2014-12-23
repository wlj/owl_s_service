package edu.pku.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OWL_SE_FileParser {
	
	private String filePath = null;
	private Document document = null;

	private String Key;
	private double grade;
	
	private String Category;
	
	private String Input;
	private String Output;
	private String Precondition;
	private String Effect;
	
	private String Context;
	private String ContextRule;
	
	private String QoS;
	
	public OWL_SE_FileParser(String filePath) {
		this.filePath = filePath;
	}
	
	public String getKey() {
		return Key;
	}
	
	public double getGrade() {
		return grade;
	}
	
	public String getCategory() {
		return Category;
	}
	
	public String getInput() {
		return Input;
	}
	
	public String getOutput() {
		return Output;
	}
	
	public String getPrecondition() {
		return Precondition;
	}
	
	public String getEffect() {
		return Effect;
	}
	
	public String getContext() {
		return Context;
	}
	
	public String getContextRule() {
		return ContextRule;
	}
	
	public String getQoS() {
		return QoS;
	}
	
	public void parseKey() {
		
		NodeList serList = this.document.getElementsByTagName("Key");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Key = name;
		}
	}
	
	public void parseCategory() {
		
		NodeList serList = this.document.getElementsByTagName("Category");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Category = name;
		}
	}
	
	public void parseInput() {
		
		NodeList serList = this.document.getElementsByTagName("Input");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Input = name;
		}
	}
	
	public void parseOutput() {
		
		NodeList serList = this.document.getElementsByTagName("Output");
	
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Output = name;
		}
	}
	
	public void parsePrecondition() {
		
		NodeList serList = this.document.getElementsByTagName("Precondition");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Precondition = name;
		}
	}
	
	public void parseEffect() {
		
		NodeList serList = this.document.getElementsByTagName("Effect");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Effect = name;
		}
	}

	public void parseContext() {
		
		NodeList serList = this.document.getElementsByTagName("Context");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			Context = name;
		}
	}

	public void parseContextRule() {
	
		NodeList serList = this.document.getElementsByTagName("ContextRule");
	
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			ContextRule = name;
		}
	}
	
	public void parseQoS() {
		
		NodeList serList = this.document.getElementsByTagName("QoS");
	
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			QoS = name;
		}
	}
	
	public void parseAll() {
		
		System.out.println("--------------------------");
		
		if(filePath == null) return;
		File file = new File(filePath);  
		if(!file.exists())
		{
			System.out.println("file not exist!");
			return;
		}
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();  
		try {   
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();    
			document = documentBuilder.parse(file);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(document == null) return;
		
		System.out.println("parse begin!!!");
		
		parseKey();
		parseCategory();
		parseInput();
		parseOutput();
		parsePrecondition();
		parseEffect();
		parseContext();
		parseContextRule();
		parseQoS();
		
		System.out.println("-------------------------");
		print();
		
	}
	
	public void print() {
		System.out.println("Key: " + Key);
		System.out.println("Category: " + Category);
		System.out.println("Input: " + Input);
		System.out.println("Output: " + Output);
		System.out.println("Precondition: " + Precondition);
		System.out.println("Effect: " + Effect);
		System.out.println("Context: " + Context);
		System.out.println("ContextRule: " + ContextRule);
		System.out.println("QoS: " + QoS);
		
	}
	
	public static void main(String[] args) {
		
		String filePath = "G:/thesis/Test/Task1.owl";
		
		OWL_SE_FileParser owl_se_FileParser = new OWL_SE_FileParser(filePath);
		
		owl_se_FileParser.parseAll();
		
	}
}
