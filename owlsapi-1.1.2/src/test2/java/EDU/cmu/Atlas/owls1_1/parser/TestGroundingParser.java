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
package EDU.cmu.Atlas.owls1_1.parser;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;

/**
 * Grounding Parser example
 * 
 * @author Naveen Srinivasan
 */
public class TestGroundingParser extends TestCase {

	private static final Logger logger = Logger.getLogger(TestGroundingParser.class);

    public static void parseWithErrorHandler(String uri) {

        DefaultOWLSErrorHandler errHandler = new DefaultOWLSErrorHandler();
        OWLSGroundingParser owlsGroundingParser = new OWLSGroundingParser();

        OWLSGroundingModel gndModel = owlsGroundingParser.read(uri, errHandler);
        logger.info(errHandler);
        logger.info(gndModel.getWsdlGroundingList());

    }

    public static void _testParse() throws Exception {
    	
    	String uri = "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl";

    	OWLSGroundingParser owlsGroundingParser = new OWLSGroundingParser();
    	
    	OWLSGroundingModel gndModel = owlsGroundingParser.read(uri);
    	
    	WsdlGroundingList gl = gndModel.getWsdlGroundingList();
    	assertEquals(gl.size(), 1);

    	WsdlGrounding g = gl.getNthWsdlGrounding(0);
    	assertEquals(g.getURI(), "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl#Grounding_BravoAir_ReservationAgent");
    	assertEquals(g.getSupportedBy(), null);
    	assertEquals(g.getWsdlAtomicProcessGroundingList().size(), 4);
    	WsdlAtomicProcessGroundingList agl = g.getWsdlAtomicProcessGroundingList();
    	assertEquals(agl.size(), 4);
    	assertEquals(agl.getNth(0).getURI(), "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl#WsdlGrounding_ConfirmReservation");
    	logger.info(gndModel.getWsdlGroundingList());
    	
    }

    public static void main(String[] str) throws IOException, NotInstanceOfWsdlGroundingException {

//    	URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
//    	PropertyConfigurator.configure(configURL);
    	
    	//String url = "http://www.daml.ri.cmu.edu/owls/BravoAirGrounding.owl";
    	String url = "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl";
    	
    	try { 
    		_testParse();
    		parseWithErrorHandler(url);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
    }
}