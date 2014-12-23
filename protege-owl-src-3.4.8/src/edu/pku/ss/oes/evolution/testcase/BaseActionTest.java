package edu.pku.ss.oes.evolution.testcase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.pku.ss.oes.evolution.IBaseAction;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

public class BaseActionTest {
	IBaseAction baseActionServiceTest;

	public void setBaseActionServiceTest(IBaseAction baseActionServiceTest) {
		this.baseActionServiceTest = baseActionServiceTest;
	}

	public void testAddClass(String name) {
		baseActionServiceTest.addOwlClass(name + "Class");
	}

	public void testRemoveClass(String name) {
		baseActionServiceTest.removeOwlClass(name + "Class");
	}

	public void testAddSubClass(String name) {
		baseActionServiceTest.addSuberClass(name + "GuideCard", "Card");
	}

	public void testRemoveSubClass(String name) {
		baseActionServiceTest.removeSuberClass(name + "AddSubJavaMethod",
				"JavaMethod");
	}

	public void testAddObjectProperty(String name) {
		baseActionServiceTest.addObjectProperty(name + "ObjectProperty");
	}

	public void testRemoveObjectProperty(String name) {
		baseActionServiceTest.removeObjectProperty(name + "ObjectProperty");
	}

	public void testAddSubObjectProperty(String name) {
		baseActionServiceTest.addSuberObjectProperty(name
				+ "JavaStatement_hasIfStatement", "JavaMethod_hasStatement");
	}

	public void testRemoveSubObjectProperty(String name) {
		baseActionServiceTest.removeSuberObjectProperty(name
				+ "JavaStatement_hasIfStatement", "JavaMethod_hasStatement");
	}

	public void testAddDataProperty(String name) {
		baseActionServiceTest.addDataProperty(name + "DataProperty");
	}

	public void testRemoveDataProperty(String name) {
		baseActionServiceTest.removeDataProperty(name + "DataProperty");
	}

	public void testAddSubDataProperty(String name) {
		baseActionServiceTest.addSuberDataProperty(name
				+ "JavaParameter_hasName", "JavaElement_hasName");
	}

	public void testRemoveSubDataProperty(String name) {
		baseActionServiceTest
				.removeDataProperty(name + "JavaParameter_hasName");
	}

	public void testAddIndividual(String owlClassName, String owlIndividualName) {
		baseActionServiceTest.addOWLNamedIndividual(owlClassName,
				owlIndividualName);
	}

	public void testRemoveIndividual(String name) {
		baseActionServiceTest.removeOWLNamedIndividual("JavaMethod", name
				+ "MethodIndividual");
	}

	public void addRelation(String individualA, String individualB,
			String objectProperty, int expireSeconds) {
		baseActionServiceTest.addObjectPropetyRelation(individualA,
				individualB, objectProperty, expireSeconds);
	}

	public File testGetOwlFile() throws Exception {
		return null;
	}
	
	/**http下载*/  
	public static boolean httpDownload(String httpUrl,String saveFile){  
	       // 下载网络文件   
	       int bytesum = 0;  
	       int byteread = 0;  
	  
	       URL url = null;  
	    try {  
	        url = new URL(httpUrl);  
	    } catch (MalformedURLException e1) {  
	        // TODO Auto-generated catch block   
	        e1.printStackTrace();  
	        return false;  
	    }  
	  
	       try {  
	           URLConnection conn = url.openConnection();  
	           InputStream inStream = conn.getInputStream();  
	           FileOutputStream fs = new FileOutputStream(saveFile);  
	  
	           byte[] buffer = new byte[1204];  
	           while ((byteread = inStream.read(buffer)) != -1) {  
	               bytesum += byteread;  
	               System.out.println(bytesum);  
	               fs.write(buffer, 0, byteread);  
	           }  
	           return true;  
	       } catch (FileNotFoundException e) {  
	           e.printStackTrace();  
	           return false;  
	       } catch (IOException e) {  
	           e.printStackTrace();  
	           return false;  
	       }  
	   }  

	public static void main(String[] args) throws Exception {
		final BaseActionTest baseActionTest = (BaseActionTest) (new FileSystemXmlApplicationContext(
				System.getProperty("user.dir") + "/webservice/"
						+ "applicationContext.xml").getBean("baseActionTest"));
		baseActionTest.testAddIndividual("Person", "p1");
		baseActionTest.testAddIndividual("Ticket", "t1");
		baseActionTest.addRelation("p1", "t1", "PersonHasTicket", 10);
		
		baseActionTest.httpDownload("http://192.168.1.101:8080/OntologyEvolutionSystem_new/tourwuxt.owl", "G:/a.owl");
	}
}
