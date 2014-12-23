package cn.edu.pku.ss.matchmaker.adapter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OWLFileParser {
	
	private String filePath = null;
	private Document document = null;
	
	private List<String> serviceKeys = null;
	private List<String> textDescriptions = null;	
	private List<String> qosValues = null;	
	private List<String> contextValues = null;
	private List<String> ruleValues = null;	
	private List<String> preconditionLists = null;
	private List<String> effectLists = null;
	private List<ActorInfo> actorLists = null;
	private List<CategoryInfo> categoryLists = null;
	
	private List<String> serNames = null;
	private List<String> proNames = null;
	private List<String> procNames = null;
	private List<String> grouNames = null;
	private List<IOInfo> inputValues = null;
	private List<IOInfo> outputValues =null;
	private List<String> atoProcGouValues = null;
	private String processContents = null;

	public OWLFileParser() {
	}
	
	
	
	public boolean parse(String owlsContent) {
		if (owlsContent == null)
			return false;
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();  
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream inputStream = new ByteArrayInputStream(owlsContent.getBytes());
			document = documentBuilder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (document == null)
			return false;
		
		serNames = getServiceName();
		proNames = getProfileName();
		procNames = getProcessName();
		grouNames =getGroundingName();
		inputValues = getInput();
		outputValues = getOutput();
		atoProcGouValues = getAtomicProcessGrounding();
		
		serviceKeys = getServiceKey();
		textDescriptions = getTextDescription();
		preconditionLists = getPreconditionList();
		effectLists = getEffectList();
		actorLists = getActorList();
		
		categoryLists = getCategoryList();
		processContents = getProcessContent();
		
		return true;
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
	public void parse()
	{
		serNames = getServiceName();
		proNames = getProfileName();
		procNames = getProcessName();
		grouNames =getGroundingName();
		inputValues = getInput();
		outputValues = getOutput();
		atoProcGouValues = getAtomicProcessGrounding();
		
		serviceKeys = getServiceKey();
		textDescriptions = getTextDescription();
		preconditionLists = getPreconditionList();
		effectLists = getEffectList();
		actorLists = getActorList();
		
		categoryLists = getCategoryList();
		qosValues = getQosValue();
		contextValues = getContextValue();
		ruleValues = getRuleValue();
		processContents = getProcessContent();
		
	}
	
	public String getProcessContents() {
		return processContents;
	}
	public void setProcessContents(String processContent) {
		this.processContents = processContent;
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
	public List<IOInfo> getInputValues() {
		return inputValues;
	}
	public void setInputValues(List<IOInfo> inputValues) {
		this.inputValues = inputValues;
	}
	public List<IOInfo> getOutputValues() {
		return outputValues;
	}
	public void setOutputValues(List<IOInfo> outputValues) {
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
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public List<String> getServiceKeys() {
		return serviceKeys;
	}
	public void setServiceKeys(List<String> serviceKeys) {
		this.serviceKeys = serviceKeys;
	}
	public List<String> getTextDescriptions() {
		return textDescriptions;
	}
	public void setTextDescriptions(List<String> textDescriptions) {
		this.textDescriptions = textDescriptions;
	}
	public List<String> getPreconditionLists() {
		return preconditionLists;
	}
	public void setPreconditionLists(List<String> preconditionLists) {
		this.preconditionLists = preconditionLists;
	}
	public List<String> getEffectLists() {
		return effectLists;
	}
	public void setEffectLists(List<String> effectLists) {
		this.effectLists = effectLists;
	}
	public List<ActorInfo> getActorLists() {
		return actorLists;
	}
	public void setActorLists(List<ActorInfo> actorLists) {
		this.actorLists = actorLists;
	}
	public List<CategoryInfo> getCategoryLists() {
		return categoryLists;
	}
	public void setCategoryLists(List<CategoryInfo> categoryLists) {
		this.categoryLists = categoryLists;
	}
	
	public List<String> getQosValues() {
		return qosValues;
	}
	public void setQosValues(List<String> qosValues) {
		this.qosValues = qosValues;
	}
	public List<String> getContextValues() {
		return contextValues;
	}
	public void setContextValues(List<String> contextValues) {
		this.contextValues = contextValues;
	}
	public List<String> getRuleValues() {
		return ruleValues;
	}
	public void setRuleValues(List<String> ruleValues) {
		this.ruleValues = ruleValues;
	}
	
	
	private String getProcessContent() {
		
		   List<String> describe = new LinkedList<String>();
			
			NodeList serList = this.document.getElementsByTagName("profile:processContent");
			
			if (serList.getLength() < 1)
				return null;
			Node node = serList.item(0);
			if (node == null)
				return null;
			
			String  processContent = node.getTextContent();
			
			return  processContent;
		}
	private List<String> getServiceKey()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("service:Service");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			NamedNodeMap map = node.getAttributes();
			String name = map.getNamedItem("rdf:ID").getNodeValue();
			serNames.add(name);
		}
		
		return  serNames;
	}private List<String> getQosValue()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("profile:Qos");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			String nodeValue = node.getFirstChild().getNodeValue();
			serNames.add(nodeValue);
		}
		
		return  serNames;
	}
	private List<String> getContextValue()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("profile:Context");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			String nodeValue = node.getFirstChild().getNodeValue();
			serNames.add(nodeValue);
		}
		
		return  serNames;
	}
	private List<String> getRuleValue()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("profile:Rule");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			String nodeValue = node.getFirstChild().getNodeValue();
			serNames.add(nodeValue);
		}
		
		return  serNames;
	}
	private List<String> getTextDescription()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("profile:textDescription");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node node = serList.item(i);
			String nodeValue = node.getFirstChild().getNodeValue();
			serNames.add(nodeValue);
		}
		
		return  serNames;
	}
	private List<String> getPreconditionList()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("profile:preconditionValue");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node pre = serList.item(i);
			String preValue = pre.getFirstChild().getNodeValue();
			serNames.add(preValue);
		}
		
		return  serNames;
	}
	private List<String> getEffectList()
	{
		List<String> serNames = new LinkedList<String>();
		
		NodeList serList = this.document.getElementsByTagName("profile:effectValue");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			Node pre = serList.item(i);
			String preValue = pre.getFirstChild().getNodeValue();
			serNames.add(preValue);
		}
		
		return  serNames;
	}
	
	
	private List<CategoryInfo> getCategoryList(){
		List<CategoryInfo> cats = new LinkedList<CategoryInfo>();
		NodeList catList = this.document.getElementsByTagName("profile:serviceCategory");
		
		for(int i = 0;i<catList.getLength();i++)
		{
			
			CategoryInfo catInfo = new CategoryInfo();
			Node cat = catList.item(i);
			Node first = null;
			NodeList l = cat.getChildNodes();
			for(int k = 0 ;k<l.getLength();k++)
			{
				String name = l.item(k).getNodeName();
				if(name != null && name.indexOf("addParam") != -1)
					first = l.item(k);
			}
			if(first == null)
				return cats;
			catInfo.setCategoryName(first.getAttributes().getNamedItem("rdf:ID").getNodeValue());		
			NodeList list = first.getChildNodes();
			for(int j = 0;j<list.getLength();j++)
			{
				Node node = list.item(j);
				String nodeName = node.getNodeName();
				if("#text".equals(nodeName))
					continue;
				System.out.println(nodeName);
				if("profile:value".equals(nodeName))
					catInfo.setValue(node.getFirstChild().getNodeValue());
				else if("profile:code".equals(nodeName))
					catInfo.setCode(node.getFirstChild().getNodeValue());
			}
			cats.add(catInfo);
		}
		return cats;
	}
	
	
	
	private List<ActorInfo> getActorList(){
		List<ActorInfo> acts = new LinkedList<ActorInfo>();
		NodeList serList = this.document.getElementsByTagName("actor:Actor");
		
		for(int i = 0;i<serList.getLength();i++)
		{
			ActorInfo actInfo = new ActorInfo();
			Node actor = serList.item(i);
			NodeList list = actor.getChildNodes();
			for(int j = 0;j<list.getLength();j++)
			{
				Node node = list.item(j);
				String nodeName = node.getNodeName();
				if("#text".equals(nodeName))
					continue;
				System.out.println(nodeName);
				if("actor:name".equals(nodeName))
					actInfo.setName(node.getFirstChild().getNodeValue());
				else if("actor:title".equals(nodeName))
					actInfo.setTitle(node.getFirstChild().getNodeValue());
				else if("actor:phone".equals(nodeName))
					actInfo.setPhone(node.getFirstChild().getNodeValue());
				else if("actor:fax".equals(nodeName))
					actInfo.setFax(node.getFirstChild().getNodeValue());
				
				else if("actor:email".equals(nodeName))
					actInfo.setEmail(node.getFirstChild().getNodeValue());
				else if("actor:physicalAddress".equals(nodeName))
					actInfo.setPhysicalAddress(node.getFirstChild().getNodeValue());
				else if("actor:webURL".equals(nodeName))
					actInfo.setWebURI(node.getFirstChild().getNodeValue());
			}
			acts.add(actInfo);
		}
		return acts;
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
			if (name == null)
				continue;
			
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
			if(name == null)
				continue;
			
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
	private List<IOInfo> getInput()
	{
		List<IOInfo> inputs = new LinkedList<IOInfo>();
		NodeList inputList = this.document.getElementsByTagName("profile:Input");
		
		for(int i = 0;i<inputList.getLength();i++)
		{
			
			IOInfo inputInfo = new IOInfo();
			Node input = inputList.item(i);
			inputInfo.setParameterName(input.getAttributes().getNamedItem("rdf:ID").getNodeValue());
			Node first = null;
			NodeList l = input.getChildNodes();
			for(int k = 0 ;k<l.getLength();k++)
			{
				String name = l.item(k).getNodeName();
				if(name != null && name.equals("profile:parameterType"))
					first = l.item(k);
			}
			if(first == null)
				return inputs;
			inputInfo.setParameterType(first.getFirstChild().getNodeValue());
			
			inputs.add(inputInfo);
		}
		return inputs;
		
	}
	private List<IOInfo> getOutput()
	{
		List<IOInfo> outputs = new LinkedList<IOInfo>();
		NodeList outputList = this.document.getElementsByTagName("profile:Output");
		
		for(int i = 0;i<outputList.getLength();i++)
		{
			
			IOInfo outputInfo = new IOInfo();
			Node output = outputList.item(i);
			outputInfo.setParameterName(output.getAttributes().getNamedItem("rdf:ID").getNodeValue());
			Node first = null;
			NodeList l = output.getChildNodes();
			for(int k = 0 ;k<l.getLength();k++)
			{
				String name = l.item(k).getNodeName();
				if(name != null && name.equals("profile:parameterType"))
					first = l.item(k);
			}
			if(first == null)
				return outputs;
			outputInfo.setParameterType(first.getFirstChild().getNodeValue());
			
			outputs.add(outputInfo);
		}
		return outputs;
		
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
	
}
