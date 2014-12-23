/*
 * OWL-S API provides functionalities to create and to manipulate OWL-S files. Copyright
 * (C) 2005 Katia Sycara, Softagents Lab, Carnegie Mellon University
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the
 * 
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * The Intelligent Software Agents Lab The Robotics Institute Carnegie Mellon University 5000 Forbes
 * Avenue Pittsburgh PA 15213
 * 
 * More information available at http://www.cs.cmu.edu/~softagents/
 */
package EDU.cmu.Atlas.owls1_1.writer;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.hp.hpl.jena.ontology.OntModel;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.Local;
import EDU.cmu.Atlas.owls1_1.process.LocalList;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.writer.OWLSProcessWriter;

/**
 * @author Naveen Srinivasan
 */
public class TestProcessWriter extends TestCase {
	
	private static final Logger logger = Logger.getLogger(TestProcessWriter.class);
	
	public static void _testWriteConstructedProcess() throws Exception {
		
		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
		OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();
		ProcessList list = storeBuilder.createProcessList();
		//Process
		AtomicProcess atomic1 = (AtomicProcess) builder.createProcess("Atomic1", OWLS_Builder_Util.ATOMIC);
		AtomicProcess atomic2 = (AtomicProcess) builder.createProcess("Atomic2", OWLS_Builder_Util.ATOMIC);
		
		Local local = (Local) builder.createParameter(OWLS_Builder_Util.LOCAL);
		local.setParameterType("test");
		local.setParameterValue("value");
		LocalList llist = storeBuilder.createLocalList();
		llist.addLocal(local);
		
		atomic1.setLocalList(llist);
		
		Perform perfAtomic2 = builder.createPerform();
		perfAtomic2.setProcess(atomic2);
		
		ControlConstructList ccList2 = builder.createControlConstructList();
		ccList2.setFirst(perfAtomic2);
		ccList2.setRest(null);
		
		Perform perfAtomic1 = builder.createPerform();
		perfAtomic1.setProcess(atomic1);
		
		ControlConstructList ccList1 = builder.createControlConstructList();
		ccList1.setFirst(perfAtomic1);
		ccList1.setRest(ccList2);
		
		ControlConstruct cc = builder.createControlConstruct(OWLS_Builder_Util.SEQUENCE);
		cc.setComponents(ccList1);
		
		CompositeProcess composite1 = (CompositeProcess) builder.createProcess("Composite1",
				OWLS_Builder_Util.COMPOSITE);
		composite1.setComposedOf(cc);
		
		list.add(composite1);
		
		logger.info("#################################");
		
		String pf = writeProcessList(list, "http://www.cs.cmu.edu/naveen.owls", null, null);
		
		// tests
		
		assertTrue(pf.contains("Atomic1"));
		assertTrue(pf.contains("Atomic2"));
		assertTrue(pf.contains("test"));
		assertTrue(pf.contains("value"));
		assertTrue(pf.contains("Composite1"));
		assertTrue(pf.contains("http://www.cs.cmu.edu/naveen.owls"));
		assertTrue(pf.contains(":ControlConstructList"));
		assertTrue(pf.contains(":CompositeProcess"));
		assertTrue(pf.contains(":Sequence"));
		
	}
	
	public static void _testReadFromURL() throws OWLSWriterException {
		URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
		PropertyConfigurator.configure(configURL);

//		String bravoURI = "http://www.daml.ri.cmu.edu/owls/BravoAirProcess.owl";
		String bravoURI = "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl";
		OWLSProcessParser parser = new OWLSProcessParser();
		DefaultOWLSErrorHandler handler = new DefaultOWLSErrorHandler();
		OWLSProcessModel model = parser.read(bravoURI, handler);
		
		logger.info("#################################");
		
		String pf = writeProcessList(model.getProcessList(), "http://www.cs.cmu.edu/naveen.owls", null, null);
		
		// tests
		
		 assertTrue(pf.contains("BravoAir_Process"));
//		 assertTrue(pf.contains("#BravoAir_ReservationAgent"));	// describes not supported???
		 assertTrue(pf.contains("#DepartureAirport"));
		 assertTrue(pf.contains("#AcctName"));
		 assertTrue(pf.contains("#HaveSeatResult\""));
		 assertTrue(pf.contains("#GetDesiredFlightDetails_FlightsFound"));
		 assertTrue(pf.contains("#TheParentPerform"));
		 assertTrue(pf.contains("#PerformBookFlight\""));
		 assertTrue(pf.contains("#AlwaysTrue\""));
		 assertTrue(pf.contains("swrl:propertyPredicate"));		// works fine, but < & > are encoded as &lt; &gt; 
		 assertTrue(pf.contains("#hasPassword"));
		 
	}
	
	static protected String writeProcessList(ProcessList pl, String base, String [] importsList, OntModel submodel) throws OWLSWriterException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		OWLSProcessWriter.write(pl, base, importsList, submodel, bos);
		logger.info(bos.toString());
		return bos.toString();
	}
	
	public static void main(String[] str) throws Exception {
		
		
		// increase jvm memory
		_testReadFromURL();
//		testWriteConstructedProcess();
	}
}