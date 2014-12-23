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
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.parser.OWLSGroundingParser;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestGroundingWriter extends TestCase {
	
	private static final Logger logger = Logger.getLogger(TestGroundingWriter.class);
	
	public static void _testWriteFromURL() throws IOException, NotInstanceOfWsdlGroundingException,
	IndexOutOfBoundsException, OWLSWriterException {
	    
		URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
		PropertyConfigurator.configure(configURL);

		OntModel m = ModelFactory.createOntologyModel();
		m.setDynamicImports(true);
		
		OWLSGroundingParser owlsGroundingParser = new OWLSGroundingParser(m);
//		String url = "http://www.daml.ri.cmu.edu/owls/BravoAirGrounding.owl";
		String url = "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl";
		
		OWLSGroundingModel gndModel = owlsGroundingParser.read(url);
		WsdlGroundingList gndList = gndModel.getWsdlGroundingList();
		
		logger.info("#################################");
		
		String pf = writeWSDLGroundingList(gndList, url, new String[] { "http://www.daml.org/services/owl-s/1.1/Concepts.owl" }, null);
		
		// tests

        assertTrue(pf.contains("Grounding_BravoAir_ReservationAgent"));
        assertTrue(pf.contains("BravoAir_ReservationAgent"));
        assertTrue(pf.contains("WsdlGrounding_GetDesiredFlightDetails"));
        assertTrue(pf.contains("WsdlGrounding_SelectAvailableFlight"));
        assertTrue(pf.contains("GetDesiredFlightDetails_OutboundDate"));
        assertTrue(pf.contains("GetDesiredFlightDetails_RoundTrip"));
        assertTrue(pf.contains("ConfirmReservation_SelectedFlight"));
        assertTrue(pf.contains("ConfirmReservation_operation"));
        assertTrue(pf.contains("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl"));
		
	}
	
    static protected String writeWSDLGroundingList(WsdlGroundingList gl, String base, String [] importsList, OntModel submodel) throws OWLSWriterException {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	
        OWLSGroundingWriter.write(gl, base, importsList, submodel, bos);
        logger.info(bos.toString());
    	return bos.toString();
	}

	public static void main(String[] str) throws IOException, NotInstanceOfWsdlGroundingException,
	IndexOutOfBoundsException, OWLSWriterException {
		
		_testWriteFromURL();
		
	}
	
}