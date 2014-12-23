package edu.pku.ly.XmlOpe;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DomOpe {

	/*public static void main(String[] args)
	{
		String filepath = "C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\Application.owl";
	}*/
	
	public static String GetDefaultNS(String fileName)
	{
		String default_ns = "";
		
		Element root = GetRootElement(fileName);
		
		default_ns = root.attribute("base").getValue();
		
		return default_ns;
	}
	
	public static String GetVersionNum(String fileName)
	{
		String version_info = "";
		
		Element root = GetRootElement(fileName);
		
		for (Iterator i = root.elementIterator(); i.hasNext();) 
		{
			Element item = (Element) i.next();
			if(item.getName() == "Ontology")
			{
				version_info = item.elementTextTrim("versionInfo");
				
				Pattern pattern = Pattern.compile("\\d{1,}\\.\\d{1,}");
				Matcher matcher = pattern.matcher(version_info);
				if (matcher.find())
				{
					version_info = matcher.group();
					return version_info;
				}
			}
		}
		
		return version_info;
	}
	
	public static Element GetRootElement(String fileName) 
	{
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		Element root = null;
		try 
		{
			Document document = saxReader.read(inputXml);
			root = document.getRootElement();
			
			/*for(int k = 0; k < root.attributes().size(); k++)
			{
				Attribute aa = (Attribute)root.attributes().get(k);
				System.out.println(aa.getName() + ":" + aa.getValue());
			}*/
			
			/*for (Iterator i = root.elementIterator(); i.hasNext();) 
			{
				Element item = (Element) i.next();
				
			}*/
			
			return root;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return root;
	} 

	/*public static void parserXml(String fileName) 
	{
		NodeList employees = document.getChildNodes();
		for (int i = 0; i < employees.getLength(); i++) 
		{
			Node employee = employees.item(i);
			NodeList employeeInfo = employee.getChildNodes();
			for (int j = 0; j < employeeInfo.getLength(); j++) 
			{
				Node node = employeeInfo.item(j);
				NodeList employeeMeta = node.getChildNodes();
				for (int k = 0; k < employeeMeta.getLength(); k++) 
				{
					System.out.println(employeeMeta.item(k).getNodeName() + ":"
							+ employeeMeta.item(k).getTextContent());
				}
			}
		}
	}*/
}
