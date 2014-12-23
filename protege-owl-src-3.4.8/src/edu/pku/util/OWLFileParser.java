package edu.pku.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OWLFileParser {
	
//1: required string serviceKey;
//2: optional string serviceName;
//3: optional string description;
//4: optional list<IOModel> inputList;
//5: optional list<IOModel> outputList;
//6: optional list<PECRModel> preconditionList;
//7: optional list<PECRModel> effectList;
//8: optional list<PECRModel> context;
//9: optional list<PECRModel> rule;
//10: optional list<QoS> qosList;
//11: optional list<Actor> actorList;
//12: optional list<Category> categoryList;
//13: optional string profileName;
//14: optional string processName;
//15: optional string groundingName;
//16: optional string wsdlURI;
	
	private String filePath = null;
	private Document document = null;
	
	private String serviceKey = null;
	private String textDescription = null;	
	
	private List<String> preconditionList = null;
	private List<String> effectList = null;
	private List<String> actorList = null;
	private List<String> categoryList = null;
	
	private List<String> serNames = null;
	private List<String> proNames = null;
	private List<String> procNames = null;
	private List<String> grouNames = null;
	private List<String> inputValues = null;
	private List<String> outputValues =null;
	private List<String> atoProcGouValues = null;
	
	public OWLFileParser(){
		init();
	}
	public OWLFileParser(String filePath)
	{
		this.filePath = filePath;
		init();
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	private void init()
	{
		if(filePath == null)
			return;
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
		if(document == null)
			return;
	}
	public void print(List<String> lists) {
		System.out.println(lists + " is be printed");
		if (lists == null) return ;
		for (int i = 0; i < lists.size(); i++) {
			System.out.println("--" + lists.get(i).toString());
		}
	}
	public void print(String str) {
		System.out.println(str+" only...");
	}
	public void parse()
	{
		serNames = getServiceName();
		print(serNames);
		proNames = getProfileName();
		print(proNames);
		procNames = getProcessName();
		print(procNames);
		grouNames =getGroundingName();
		print(grouNames);
		inputValues = getInput();
		print(inputValues);
		outputValues = getOutput();
		print(outputValues);
		atoProcGouValues = getAtomicProcessGrounding();
		print(atoProcGouValues);
		
		serviceKey = getServiceKey();
		print(serviceKey);
		textDescription = getTextDescription();
		print(textDescription);
		
		preconditionList = getPreconditionList();
		print(preconditionList);
		effectList = getEffectList();
		print(effectList);
		actorList = getActorList();
		print(actorList);
		categoryList = getCategoryList();
		print(categoryList);
		
	}
	
	public List<String> getSerNames() {
		return serNames;
	}
	public void setSerNames(List<String> serNames) {
		this.serNames = serNames;
	}
	public List<String> getProNames() {
		return proNames;
	}
	public void setProNames(List<String> proNames) {
		this.proNames = proNames;
	}
	public List<String> getProcNames() {
		return procNames;
	}
	public void setProcNames(List<String> procNames) {
		this.procNames = procNames;
	}
	public List<String> getGrouNames() {
		return grouNames;
	}
	public void setGrouNames(List<String> grouNames) {
		this.grouNames = grouNames;
	}
	public List<String> getInputValues() {
		return inputValues;
	}
	public void setInputValues(List<String> inputValues) {
		this.inputValues = inputValues;
	}
	public List<String> getOutputValues() {
		return outputValues;
	}
	public void setOutputValues(List<String> outputValues) {
		this.outputValues = outputValues;
	}
	public List<String> getAtoProcGouValues() {
		return atoProcGouValues;
	}
	public void setAtoProcGouValues(List<String> atoProcGouValues) {
		this.atoProcGouValues = atoProcGouValues;
	}
	public String getFilePath() {
		return filePath;
	}
	
	public String getServiceKey() {
		return serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	public String getTextDescription() {
		return textDescription;
	}
	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}
	public List<String> getPreconditionList() {
		return preconditionList;
	}
	public void setPreconditionList(List<String> preconditionList) {
		this.preconditionList = preconditionList;
	}
	public List<String> getEffectList() {
		return effectList;
	}
	public void setEffectList(List<String> effectList) {
		this.effectList = effectList;
	}
	public List<String> getActorList() {
		return actorList;
	}
	public void setActorList(List<String> actorList) {
		this.actorList = actorList;
	}
	public List<String> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}
	
	private List<String> getServiceName()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("service:Service");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			System.out.println("*****  " + name + "  *****");
			serNames.add(name);
		}
		
		return  serNames;
	}
	private List<String> getProfileName()
	{
		List<String> proNames = new LinkedList<String>();
		
		NodeList proList = this.document.getElementsByTagName("profile:Profile");
		
		for(int i = 0;i<proList.getLength();i++)
		{
			Node node = proList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			proNames.add(name);
		}
		
		return  proNames;
	}
	private List<String> getProcessName()
	{
		List<String> procNames = new LinkedList<String>();
		
		NodeList procList = this.document.getElementsByTagName("process:AtomicProcess");
		
		for(int i = 0;i<procList.getLength();i++)
		{
			Node node = procList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			procNames.add(name);
		}
		return  procNames;
	}
	private List<String> getGroundingName()
	{
		List<String> grouNames = new LinkedList<String>();
		
		NodeList grouList = this.document.getElementsByTagName("grounding:WsdlGrounding");
		
		for(int i = 0;i<grouList.getLength();i++)
		{
			Node node =grouList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			grouNames.add(name);
		}
		return  grouNames;
	}
	private List<String> getInput()
	{
		List<String> inputValues = new LinkedList<String>();
		
		NodeList inputList = this.document.getElementsByTagName("process:Input");
		
		for(int i = 0;i<inputList.getLength();i++)
		{
			Node node =inputList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			inputValues.add(name);
			System.out.println(name);
		}
		return  inputValues;
		
	}
	private List<String> getOutput()
	{
		List<String> outputValues = new LinkedList<String>();
		
		NodeList outputList = this.document.getElementsByTagName("process:Output");
		
		for(int i = 0;i<outputList.getLength();i++)
		{
			Node node =outputList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			outputValues.add(name);
			System.out.println(name);
		}
		return  outputValues;
		
	}
	private List<String> getAtomicProcessGrounding()
	{
		List<String> atoProcGouValues = new LinkedList<String>();
		
		NodeList atoProcGouList = this.document.getElementsByTagName("grounding:WsdlAtomicProcessGrounding");
		
		for(int i = 0;i<atoProcGouList.getLength();i++)
		{
			Node node =atoProcGouList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			atoProcGouValues.add(name);
			System.out.println(name);
		}
		return  atoProcGouValues;
		
	}
	
	public static void main(String[] args) {
		
		String filePath = "G:/thesis/test2.owl";
		
		OWLFileParser owlFileParser = new OWLFileParser(filePath);
		
		owlFileParser.parse();
		
	}
}
